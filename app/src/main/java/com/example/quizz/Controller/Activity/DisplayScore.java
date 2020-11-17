package com.example.quizz.Controller.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.quizz.Model.DataBase.AppDatabase;
import com.example.quizz.Model.DataBase.Score;
import com.example.quizz.R;

public class DisplayScore extends AppCompatActivity {
    //Déclaration des variable
    private int score,quizz_id,nbr_questions;
    private TextView tv_score;
    AppDatabase DB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialiser le contentview
        setContentView(R.layout.score);
        //Récuprer le Text view la ou on affiche le score
        tv_score = (TextView) findViewById(R.id.tv_score);

        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "quizz.db")
                .allowMainThreadQueries()
                .build();
        //Récuperer les données envoyer par l activity d'avant
        Intent intent = getIntent();
        //Vérifier si l'activity a envoyer le variable score et le variable quizz_id et nbr_question puis les récuperers
        if (intent.hasExtra("score") && intent.hasExtra("quizz_id") && intent.hasExtra("nbr_questions"))
        {
            //Récuprer le variable score depuis l'activity d'avant
            score = Integer.parseInt(intent.getStringExtra("score"));
            //Récuprer le variable quizz_id depuis l'activity d'avant
            quizz_id = Integer.parseInt(intent.getStringExtra("quizz_id"));
            //Récuprer le variable nbr_question depuis l'activity d'avant
            nbr_questions = Integer.parseInt(intent.getStringExtra("nbr_questions"));
        }
        //Afficher le score et nombre question sur le text view de score
        tv_score.setText(score + " / " + nbr_questions);

        //Création d'un dialog pour demander ou utilisateur de rentrer son nom pour sauvgarder le score dans la BDD
        final Dialog dialog = new Dialog(this);
        //Récuprer l'iterface de dialog depuis les layouts
        dialog.setContentView(R.layout.dialog_add_score);
        //Mettre un titre pour le dialog
        dialog.setTitle("Entrer votre Nom");
        //Récuperer le button ajouter depuis le dialog
        Button AjouterButton = (Button) dialog.findViewById(R.id.dialogButtonAjouter_quizz);
        //Récuperer le button annuler depuis le dialog
        Button AnnulerButton = (Button) dialog.findViewById(R.id.dialogButtonEffacer_quizz);
        //Récuprer l'edit text depuis le dialog
        final EditText edt = (EditText)dialog.findViewById(R.id.score_type_dialog);
        //Créer un click listener pour le button ajouter
        AjouterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Récuprer le nom introduit par l'utilisateur
                final String player_name= edt.getText().toString();
                //Vérifier si nom est pas vide
                if(!player_name.equals(""))
                {
                    //Sauvgarder le score avec le nom user dans la BDD et ferermer le dialog
                    DB.scoreDao().insert(new Score(player_name,score,nbr_questions,quizz_id));
                    dialog.dismiss();
                }
                else
                {
                    //Informer l'utilisateur qu'il doit introduit un nom pour sauvegarder le score
                    Toast.makeText(getApplicationContext(), "Remplir votre Nom SVP !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Créer un click listener pour le button Annuler et fermer le dialog lors de click sur le button
        AnnulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //Afficher le dialog
        dialog.show();
    }

    //Button quitter pour fermer l'activity DisplayScore
    public void Quitter_score(View view) {
        //fermer l'activity
        finish();
    }
}
