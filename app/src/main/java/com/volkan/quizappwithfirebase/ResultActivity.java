package com.volkan.quizappwithfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.volkan.quizappwithfirebase.databinding.ActivityResultBinding;

import java.lang.reflect.Field;

public class ResultActivity extends AppCompatActivity {
ActivityResultBinding binding;


int POINTS=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int correctAnswers=getIntent().getIntExtra("correct",0);
        int totalQuestions=getIntent().getIntExtra("correct",0);
        long points=correctAnswers*POINTS;
        binding.score.setText(String.format("%d/%d",correctAnswers,totalQuestions));
        binding.earnevCoins.setText(String.valueOf(points));
  FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid()).update("coins", FieldValue.increment(points));
binding.restartBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(ResultActivity.this, MainActivity.class));
        finishAffinity();
    }
});

    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this,MainActivity.class));
        super.onBackPressed();
    }
}