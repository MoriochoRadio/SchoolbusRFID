package com.example.project01;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OperationStartActivity extends AppCompatActivity {

    // Firebase에서 탑승/하차를 표시할 TextView 선언
    private TextView boardingCountTextView;
    private TextView alightingCountTextView;
    private Button endOperationButton;
    private Button btnReset;

    private int boardingCount = 0; // 탑승자 수
    private int alightingCount = 0; // 하차자 수

    // Firebase Database 참조
    private DatabaseReference tagRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_start);

        endOperationButton = findViewById(R.id.endOperationButton);
        btnReset = findViewById(R.id.btnReset);

        // Edge-to-Edge 활성화
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // System Bars 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_operation_start), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 상태 바와 내비게이션 바의 아이콘 색상 조정
        WindowInsetsControllerCompat insetsController = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        insetsController.setAppearanceLightStatusBars(true);  // 상태 바 아이콘 색상 (밝게 설정)
        insetsController.setAppearanceLightNavigationBars(true);  // 내비게이션 바 아이콘 색상 (밝게 설정)

        // TextView UI 요소 초기화 (activity_operation_start.xml의 TextView ID와 일치시켜야 합니다)
        boardingCountTextView = findViewById(R.id.boarding_count);
        alightingCountTextView = findViewById(R.id.alighting_count);

        // Firebase Realtime Database 참조 가져오기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tagRef = database.getReference("tag");

        // Firebase에서 RFID 상태 변경 감지 및 처리
        tagRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 탑승자와 하차자 수 초기화
                boardingCount = 0;
                alightingCount = 0;

                for (DataSnapshot tagSnapshot : dataSnapshot.getChildren()) {
                    Boolean boarding = tagSnapshot.child("boarding").getValue(Boolean.class);

                    if (boarding != null && boarding) {
                        // boarding이 true인 경우 (탑승한 학생)
                        boardingCount++;
                    } else if (boarding != null && !boarding) {
                        // boarding이 false인 경우 (하차한 학생)
                        alightingCount++;
                    }
                }

                // UI에 갱신된 값 반영
                boardingCountTextView.setText(String.valueOf(boardingCount));
                alightingCountTextView.setText(String.valueOf(alightingCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Firebase 데이터베이스 오류 처리
            }
        });
        endOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OperationStartActivity", "End operation button clicked");
                AlertDialog.Builder d = new AlertDialog.Builder(OperationStartActivity.this);
                if (boardingCount != 0) {
                    d.setTitle("운행 종료")
                            .setMessage("탑승자가 남아있습니다.\n다시 한 번 확인해주세요")
                            .setPositiveButton("확인", (dialog, which) -> {
                                // btnReset 활성화
                                btnReset.setVisibility(View.VISIBLE);
                            })
                            .show();
                } else {
                    d.setTitle("운행 종료")
                            .setMessage("운행이 종료됩니다.")
                            .setPositiveButton("확인", null)
                            .show();
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 값 초기화
                boardingCount = 0;
                alightingCount = 6;

                // UI에 갱신된 값 반영
                boardingCountTextView.setText(String.valueOf(boardingCount));
                alightingCountTextView.setText(String.valueOf(alightingCount));
            }
        });
    }
}
