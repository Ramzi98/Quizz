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
    //Déclaration des variables
    int quizz_id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialiser le contentview
        setContentView(R.layout.quizz_choisi);

        //Récuperer les données envoyer par l activity d'avant
        Intent quizzList_intent = getIntent();
        //Vérifier si l'activity a envoyer le variable id puis les récuperer
        if (quizzList_intent.hasExtra("id"))
        {
            quizz_id = Integer.parseInt(quizzList_intent.getStringExtra("id"));
        }

    }

    //Click button JOUER UNE PARTIE
    public void JouerPartie(View view) {
        //Crée nouvelle intent Partie pour démarer une partie et afficher ces questions
        Intent jouer_partie_intent = new Intent(this,Partie.class);
        //Envoyer un extra (variable) id pour l'activity  Partie
        jouer_partie_intent.putExtra("id",String.valueOf(quizz_id));
        //Démarer l'activity
        startActivity(jouer_partie_intent);
    }

    //Click button AFFICHER SCORE FINALE
    public void AfficherScore(View view) {
        //Crée nouvelle intent DisplayScoresOfQuizz pour afficher les scores de ce Quizz
        Intent display_score_intent = new Intent(this,DisplayScoresOfQuizz.class);
        //Envoyer un extra (variable) quizz_id pour l'activity  DisplayScoresOfQuizz
        display_score_intent.putExtra("quizz_id",String.valueOf(quizz_id));
        //Démarer l'activity
        startActivity(display_score_intent);
    }

    //Click button CHOISIR UN AUTRE QUIZZ
    public void ChoisirAutreQuizz(View view) {
        //Fermer cette Activity
        finish();
    }


}
