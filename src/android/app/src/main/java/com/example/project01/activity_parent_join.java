package com.example.project01;

import android.annotation.SuppressLint;
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

public class activity_parent_join extends AppCompatActivity {

    private EditText txtId, txtPw, txtName, txtPhone, txtCode;
    private Button signBtn;
    private FirebaseDatabase db;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_join);


        reference = FirebaseDatabase.getInstance().getReference();

        db = FirebaseDatabase.getInstance();

        txtId = findViewById(R.id.txtId);
        txtPw = findViewById(R.id.txtPw);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtCode = findViewById(R.id.txtCode);
        signBtn = findViewById(R.id.signBtn);

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
    }


    private void saveUserData() {
        // Get user input
        String id = txtId.getText().toString().trim();
        String password = txtPw.getText().toString().trim();
        String name = txtName.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String code = txtCode.getText().toString().trim();

        if (id.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || code.isEmpty()) {
            Toast.makeText(activity_parent_join.this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 자녀 코드 (code)가 Firebase의 `tag` 속성 중 `uid`와 일치하는지 확인
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tag");
        databaseReference.orderByChild("uid").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // 자녀 정보가 존재하는 경우
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String sname = childSnapshot.child("sname").getValue(String.class);

                        // 부모 정보를 Firebase에 저장
                        Parent parent = new Parent(id, password, name, phone, code);
                        reference.child("parent").push().setValue(parent)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(activity_parent_join.this, sname + "의 부모로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity_parent_join.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    // 자녀 코드가 존재하지 않는 경우
                    Toast.makeText(activity_parent_join.this, "입력한 코드에 해당하는 자녀가 없습니다. 회원가입이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity_parent_join.this, "데이터베이스 오류: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    // User class to represent user data
    public static class Parent {
        public String id;
        public String password;
        public String name;
        public String phone;
        public String code;

        public Parent() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Parent(String id, String password, String name, String phone, String code) {
            this.id = id;
            this.password = password;
            this.name = name;
            this.phone = phone;
            this.code = code;
        }
    }
}