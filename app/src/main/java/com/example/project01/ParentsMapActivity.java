package com.example.project01;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference driverLocationRef;// Firebase에서 운전자의 위치를 가져오기 위한 참조
    private LatLng driverLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_map);  // 부모용 지도 레이아웃을 사용

        // Firebase에서 운전자의 위치 정보를 가져올 참조
        driverLocationRef = FirebaseDatabase.getInstance().getReference("DriverLocation");

        // 지도 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.Pmap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 위치 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // 필요한 경우 권한 요청 처리
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // Firebase에서 운전자의 실시간 위치 가져오기
        driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                Double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                if (latitude != null && longitude != null) {
                    driverLocation = new LatLng(latitude, longitude);

                    mMap.clear();  // 기존 마커 제거

                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bus);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 150, 150, true); // 원하는 크기로 조정
                    BitmapDescriptor busIcon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);
                    mMap.addMarker(new MarkerOptions().position(driverLocation)
                            .title("Driver Location")
                            .icon(busIcon));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 15));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Firebase 데이터 가져오는 중 오류 처리
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }
}
