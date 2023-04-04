package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilActivity extends AppCompatActivity {



        private TextView emailTextView, usernameTextView, rankTextView;
        private FirebaseAuth firebaseAuth;
        private FirebaseFirestore firestoreDB;
        private String userId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profil);
            getSupportActionBar().hide();

            emailTextView = findViewById(R.id.Email);
            usernameTextView = findViewById(R.id.Username);
            rankTextView = findViewById(R.id.Rank);

            firebaseAuth = FirebaseAuth.getInstance();
            firestoreDB = FirebaseFirestore.getInstance();

            userId = firebaseAuth.getCurrentUser().getUid();

            DocumentReference userDocRef = firestoreDB.collection("users").document(userId);
            userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String email = documentSnapshot.getString("email");
                        String username = documentSnapshot.getString("username");
                        Object rank = documentSnapshot.get("rank");
                          String r= rank.toString();
                        emailTextView.setText(email);
                        usernameTextView.setText(username);
                        rankTextView.setText(r);
                    } else {
                        Log.d("ProfileActivity", "No such document");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ProfileActivity", "Error getting document: " + e.getMessage());
                }
            });
        }
    }