package com.example.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_driver_join extends AppCompatActivity {

    private EditText txtId, txtPw, txtName, txtPhone;
    private Button signBtn;
    private FirebaseDatabase db;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_join);


        reference = FirebaseDatabase.getInstance().getReference();

        db = FirebaseDatabase.getInstance();

        txtId = findViewById(R.id.txtId);
        txtPw = findViewById(R.id.txtPw);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
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

        if (id.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(activity_driver_join.this, "다시 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a User object
        Driver driver = new Driver(id, password, name, phone);
        // Save user data to Firebase Realtime Database
        reference.child("users").push().setValue(driver)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity_driver_join.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity_driver_join.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // User class to represent user data
    public static class Driver {
        public String id;
        public String password;
        public String name;
        public String phone;

        public Driver() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Driver(String id, String password, String name, String phone) {
            this.id = id;
            this.password = password;
            this.name = name;
            this.phone = phone;
        }
    }
}