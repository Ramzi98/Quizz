package com.example.quizz.Controller.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizz.R;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialiser le contentview
        setContentView(R.layout.home);
    }

    //Click button Choisir Quizz
    public void ChoisirQuizz(View view) {
        //Crée nouvelle intent QuizzsList pour afficher la liste des quizzs
        Intent choisir_quizz_intent = new Intent(this,QuizzsList.class);
        //Démarer l'activity
        startActivity(choisir_quizz_intent);
    }

    //Click button Gestion Quizz
    public void GestionQuizzs(View view) {
        //Crée nouvelle intent QuizzsList pour afficher le menu de gestion
        Intent gestion_quizzs_intent = new Intent(this,QuizzsGestion.class);
        //Démarer l'activity
        startActivity(gestion_quizzs_intent);
    }

    public void Quitter(View view) {
        this.finish();
    }
}
