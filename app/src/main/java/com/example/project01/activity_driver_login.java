package com.example.project01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activity_driver_login extends AppCompatActivity {

    private Button btnLogin;
    private Button btnSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnSign = findViewById(R.id.btnSign);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_driver_login.this, activity_driver_main.class);
                startActivity(intent);
            }
        });

        // 회원가입 버튼 클릭 리스너 설정
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_driver_login.this, activity_driver_join.class);
                startActivity(intent);
            }
        });
    }
}