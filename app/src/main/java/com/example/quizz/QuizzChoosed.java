package com.example.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Proposition;
import com.example.quizz.DataBase.Question;
import com.example.quizz.DataBase.Quizz;

import java.util.ArrayList;

public class QuizzChoosed extends AppCompatActivity {

    int quizz_id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizz_choisi);
        Intent quizzList_intent = getIntent();
        if (quizzList_intent.hasExtra("id"))
        {
            quizz_id = Integer.parseInt(quizzList_intent.getStringExtra("id"));
        }

    }

    public void JouerPartie(View view) {
        Intent jouer_partie_intent = new Intent(this,Partie.class);
        jouer_partie_intent.putExtra("id",String.valueOf(quizz_id));
        startActivity(jouer_partie_intent);
    }

    public void AfficherScore(View view) {
        Intent display_score_intent = new Intent(this,DisplayScoresOfQuizz.class);
        display_score_intent.putExtra("quizz_id",String.valueOf(quizz_id));
        startActivity(display_score_intent);
    }

    public void ChoisirAutreQuizz(View view) {
        finish();
    }


}
