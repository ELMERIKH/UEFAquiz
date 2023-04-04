package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView mQuestionTextView;
    private RadioButton mOptionARadioButton;
    private RadioButton mOptionBRadioButton;
    private RadioButton mOptionCRadioButton;
    private RadioButton mOptionDRadioButton;
    private RadioGroup mOptionsRadioGroup;
    private Button mSubmitButton;
    private ProgressBar mProgressBar;

    private FirebaseFirestore db;
    private List<Question> mQuestionList;
    private static int mScore;
    private int mQuestionIndex;
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().hide();

        mQuestionTextView = findViewById(R.id.question);
        mOptionARadioButton = findViewById(R.id.option_a);
        mOptionBRadioButton = findViewById(R.id.option_b);
        mOptionCRadioButton = findViewById(R.id.option_c);
        mOptionDRadioButton = findViewById(R.id.option_d);
        mOptionsRadioGroup = findViewById(R.id.options_radio_group);
        mSubmitButton = findViewById(R.id.submitB);
        mProgressBar = findViewById(R.id.progress_bar);

        db = FirebaseFirestore.getInstance();
        mQuestionList = new ArrayList<>();
        mScore = 0;
        mQuestionIndex = 0;

        loadQuestionsFromFirestore();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = mOptionsRadioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    // no option selected
                    Toast.makeText(QuizActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedOption = findViewById(selectedId);
                String selectedText = selectedOption.getText().toString();

                Question currentQuestion = mQuestionList.get(mQuestionIndex);
                if (selectedText.equals(currentQuestion.getAnswer())) {
                    // correct answer
                     mScore++;
                    Toast.makeText(QuizActivity.this, "Correct!", Toast.LENGTH_SHORT).show();

                } else {
                    // incorrect answer
                    Toast.makeText(QuizActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                }

                mQuestionIndex++;

                if (mQuestionIndex < mQuestionList.size()) {
                    loadQuestion();
                } else {
                    endQuiz();
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference userDocRef = mFirestore.collection("users").document(userId);
                    userDocRef.update("rank", mScore);
                }
            }
        });

    }

    private void loadQuestionsFromFirestore() {
        db.collection("questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Question question = document.toObject(Question.class);
                                mQuestionList.add(question);
                            }

                            loadQuestion();
                        } else {
                            Toast.makeText(QuizActivity.this, "Error getting questions", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadQuestion() {
        Question question = mQuestionList.get(mQuestionIndex);

        mQuestionTextView.setText(question.getQuestion());
        mOptionARadioButton.setText(question.getOptionA());
        mOptionBRadioButton.setText(question.getOptionB());
        mOptionCRadioButton.setText(question.getOptionC());
        mOptionDRadioButton.setText(question.getOptionD());

        mOptionsRadioGroup.clearCheck();

        mProgressBar.setProgress((mQuestionIndex + 1) * 100 / mQuestionList.size());
    }
    private void checkanswer(){


    }

    private void endQuiz() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_SCORE, mScore);
        intent.putExtra(ResultActivity.EXTRA_TOTAL_QUESTIONS, mQuestionList.size());
        startActivity(intent);
        finish();
    }
}

