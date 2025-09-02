package com.example.project01;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_parent_login extends AppCompatActivity {

    private EditText txtId, txtPw;
    private Button btnLogin;
    private Button btnSign;
    private DatabaseReference parentReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        txtId = findViewById(R.id.txtId);
        txtPw = findViewById(R.id.txtPw);
        btnLogin = findViewById(R.id.btnLogin);
        btnSign = findViewById(R.id.btnSign);

        parentReference = FirebaseDatabase.getInstance().getReference("parent");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = txtId.getText().toString().trim();
                String password = txtPw.getText().toString().trim();

                if (id.isEmpty() || password.isEmpty()) {
                    Toast.makeText(activity_parent_login.this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 로그인 검증
                parentReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String storedPassword = snapshot.child("password").getValue(String.class);
                                if (storedPassword != null && storedPassword.equals(password)) {
                                    String parentId = snapshot.getKey();
                                    Intent intent = new Intent(activity_parent_login.this, ParentsActivity.class);
                                    intent.putExtra("parentId", parentId);
                                    startActivity(intent);
                                    return;
                                } else {
                                    Toast.makeText(activity_parent_login.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(activity_parent_login.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(activity_parent_login.this, "로그인 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 회원가입 버튼 클릭 리스너 설정
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_parent_login.this, activity_parent_join.class);
                startActivity(intent);
            }
        });
    }
}
