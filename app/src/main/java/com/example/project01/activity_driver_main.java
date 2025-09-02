package com.example.project01;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class activity_driver_main extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView NumOfBoarding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Edge-to-Edge 활성화
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_driver_main);

        Button startOperationButton = findViewById(R.id.startOperationButton); // "운행 시작" 버튼 ID
        startOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_driver_main.this, MapActivity.class);
                startActivity(intent); // 새로운 액티비티로 이동
            }
        });

        // System Bars 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_driver_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getNumOfBoarding();
    }

    private void getNumOfBoarding() {
        databaseReference = FirebaseDatabase.getInstance().getReference("NumOfBoarding"); // DB 참조 초기화
        NumOfBoarding = findViewById(R.id.NumOfBoarding);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FirebaseData", "Data changed"); // 데이터 변경 확인
                int numOfBoarding = dataSnapshot.getValue(int.class);
                Log.d("FirebaseData", "Boarding value: " + numOfBoarding); // NumOfBoarding 값 확인
                NumOfBoarding.setText(numOfBoarding + "명");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }
}
