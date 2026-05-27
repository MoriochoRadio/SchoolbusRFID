package com.example.project01;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 부모님
        LinearLayout parentLayout = findViewById(R.id.parentlayout);
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새 액티비티로 이동
                Intent intent = new Intent(MainActivity.this, activity_parent_login.class);
                startActivity(intent);
            }
        });

        // 선생님
        LinearLayout teacherLayout = findViewById(R.id.teacherlayout);
        teacherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새 액티비티로 이동
                Intent intent = new Intent(MainActivity.this, activity_teacher_login.class);
                startActivity(intent);
            }
        });


        // 운전자
        LinearLayout driverLayout = findViewById(R.id.driverlayout);
            driverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_driver_login.class);
                startActivity(intent);
            }
        });
    }
}