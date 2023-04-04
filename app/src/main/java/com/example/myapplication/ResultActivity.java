package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "com.example.myapplication.EXTRA_SCORE";
    public static final String EXTRA_TOTAL_QUESTIONS = "com.example.myapplication.EXTRA_TOTAL_QUESTIONS";
    private TextView mScoreTextView;
    private Button mRestartButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().hide();

        mScoreTextView = findViewById(R.id.score);
        mRestartButton = findViewById(R.id.restartB);

        int score = getIntent().getIntExtra(ResultActivity.EXTRA_SCORE, 0);
        mScoreTextView.setText("Score: " + score);

        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}