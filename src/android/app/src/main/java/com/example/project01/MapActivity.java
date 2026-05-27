package com.example.project01;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker; // 현재 위치를 나타내는 마커
    private DatabaseReference driverLocationRef;// Firebase 데이터베이스 참조
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Firebase Database에 운전자의 위치를 저장할 경로 설정
        driverLocationRef = FirebaseDatabase.getInstance().getReference("DriverLocation");

        // 위치 제공자 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // SupportMapFragment를 사용하여 지도를 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 위치 요청 초기화 (5초마다 위치 업데이트)
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // 5초마다 업데이트
        locationRequest.setFastestInterval(2000); // 2초마다 업데이트 가능
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // 위치 업데이트를 처리하는 콜백 설정
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (mMap != null) {
                        // 운전자의 현재 위치 가져오기
                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        // 이전 마커 삭제
                        if (currentLocationMarker != null) {
                            currentLocationMarker.remove();
                        }

                        // 새로운 마커 추가 및 카메라 이동
                        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Driver's Current Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                        // 운전자의 위치를 Firebase에 저장
                        driverLocationRef.setValue(currentLocation);
                    }
                }
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 줌 제스처 활성화
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // 위치 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    // 위치 업데이트 시작
    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    // 위치 업데이트 중단
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    startLocationUpdates();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates(); // 앱이 다시 시작되면 위치 업데이트 재개
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates(); // 앱이 일시정지되면 위치 업데이트 중단
    }
}
