package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private EditText name,emailEdit,passEdit,confirmPass;

    private Button signUpButton;

    private ImageView BackB;



    private FirebaseAuth firebaseAuth;
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.username);
        emailEdit = findViewById(R.id.emailEdit);
        passEdit = findViewById(R.id.passwordEdit);
        confirmPass = findViewById(R.id.Confirmpass);
        signUpButton = findViewById(R.id.signUpB);
        BackB = findViewById(R.id.BackB);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEdit.getText().toString().trim();
                String password = passEdit.getText().toString().trim();
                String confpass =confirmPass.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    emailEdit.setError("Email is required!.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passEdit.setError("Password is required!.");
                    return;
                }
                if (!confpass.equals(password)) {
                    passEdit.setError("Password doesn t match!.");
                    return;
                }



                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    int mscore=0;
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    DocumentReference userDocRef = mFirestore.collection("users").document(userId);
                                    Map<String , Object> user = new HashMap<>();
                                    user.put("email", email);
                                    user.put("username", name.getText().toString());
                                    user.put("rankEng", mscore);
                                    user.put("rankFR", mscore);
                                    user.put("rankRU", mscore);
                                    user.put("rankAR", mscore);// Create an empty rank field
                                    userDocRef.set(user);
                                    Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(RegistrationActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



        BackB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }}
    );

    }}
