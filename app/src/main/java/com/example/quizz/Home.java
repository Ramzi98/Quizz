package com.example.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }

    public void ChoisirQuizz(View view) {
        Intent choisir_quizz_intent = new Intent(this,QuizzsList.class);
        startActivity(choisir_quizz_intent);
    }

    public void GestionQuizzs(View view) {
        Intent gestion_quizzs_intent = new Intent(this,QuizzsGestion.class);
        startActivity(gestion_quizzs_intent);
    }
}
