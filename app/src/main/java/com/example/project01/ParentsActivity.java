package com.example.project01;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentsActivity extends AppCompatActivity {

    private TextView childNameTextView;
    private TextView boardingStatusTextView;
    private DatabaseReference parentReference;
    private DatabaseReference tagReference;
    private Button btnCheckVehicleLocation; // 차량 위치 확인 버튼 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Edge-to-Edge 활성화
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_parents);

        // System Bars 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_parents_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // UI 요소 초기화
        childNameTextView = findViewById(R.id.child_name_text_view);
        boardingStatusTextView = findViewById(R.id.boarding_status_text_view);
        btnCheckVehicleLocation = findViewById(R.id.btnCheckVehicleLocation); // 차량 위치 확인 버튼 초기화

        // 차량 위치 확인 버튼 클릭 시 ParentsMapActivity로 이동
        btnCheckVehicleLocation.setOnClickListener(v -> {
            Intent intent = new Intent(ParentsActivity.this, ParentsMapActivity.class);
            startActivity(intent);
        });

        // Firebase 데이터베이스 참조 초기화
        parentReference = FirebaseDatabase.getInstance().getReference("parent");
        tagReference = FirebaseDatabase.getInstance().getReference("tag");

        // 부모 로그인 정보 가져오기
        Intent intent = getIntent();
        String parentId = intent.getStringExtra("parentId"); // 부모 로그인 후 전달된 ID

        if (parentId == null || parentId.isEmpty()) {
            Toast.makeText(this, "유효한 부모 ID가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 부모의 자녀 코드 가져오기
        getParentCode(parentId);
    }

    private void getParentCode(String parentId) {
        parentReference.child(parentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String childCode = dataSnapshot.child("code").getValue(String.class);
                    if (childCode != null) {
                        // 자녀 정보 가져오기
                        getChildInfo(childCode);
                    } else {
                        Toast.makeText(ParentsActivity.this, "부모의 자녀 코드를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ParentsActivity.this, "부모 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ParentsActivity.this, "부모 정보를 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getChildInfo(String childCode) {
        tagReference.child(childCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String sname = dataSnapshot.child("sname").getValue(String.class);
                    Boolean boarding = dataSnapshot.child("boarding").getValue(Boolean.class);

                    // 자녀 이름 설정
                    if (sname != null) {
                        childNameTextView.setText(sname + " 어린이");
                    }

                    // 탑승 상태 설정
                    if (boarding != null) {
                        boardingStatusTextView.setText(boarding ? "탑승" : "미탑승");
                    }
                    else{
                        boardingStatusTextView.setText("정보 없음");
                    }
                } else {
                    Toast.makeText(ParentsActivity.this, "해당 자녀 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ParentsActivity.this, "자녀 정보를 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
