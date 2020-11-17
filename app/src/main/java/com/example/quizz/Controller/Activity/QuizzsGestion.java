package com.example.quizz.Controller.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.quizz.Model.DataBase.AppDatabase;
import com.example.quizz.Model.DataBase.Proposition;
import com.example.quizz.Model.DataBase.Question;
import com.example.quizz.Model.DataBase.Quizz;
import com.example.quizz.Model.DOMQuizz;
import com.example.quizz.R;

import java.util.ArrayList;

public class QuizzsGestion extends AppCompatActivity {
    //Déclaration des variables
    com.example.quizz.Model.DOMQuizz DOMQuizz;
    AppDatabase DB;
    Quizz Quizz;
    Question Question;
    Proposition Propositions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_quizzs);

        //Récuperation de la base de données
        Recuperer_bdd();
    }

    //Récuperations des données XML depuis le site
    public void TelechargerQuizzs(View view) {
        DOMQuizz = new DOMQuizz(this);
        DOMQuizz.execute();
        Toast.makeText(this, "Vous avez Récuperer les quizz depuis le serveur", Toast.LENGTH_LONG).show();
    }

    //Click sur le Button Edition des Quizz
    public void EditionQuizz(View view) {
        //Crée nouvelle intent Edition quizz intent p
        Intent edition_quizz_intent = new Intent(this,DisplayAllQuizzs.class);
        //démarer l'activity
        startActivity(edition_quizz_intent);
    }

    //Click sur le Button Création d'un nouveau Quizz
    public void CreationQuizz(final View view) {
        //Afficher un dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_quizz);
        //Ajouter un titre pour le dialog
        dialog.setTitle("Ajouter un Quizz");
        //Récuperer le button ajouter et Annuler depuis le dialog
        Button AjouterButton = (Button) dialog.findViewById(R.id.dialogButtonAjouter_quizz);
        Button AnnulerButton = (Button) dialog.findViewById(R.id.dialogButtonEffacer_quizz);

        //Créer un click listener pour le button ajouter
        AjouterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Récuprer le nom de quizz introduit par l'utilisateur
                EditText edt = (EditText)dialog.findViewById(R.id.quizz_type_dialog);
                final String quizz_type= edt.getText().toString();
                if(!quizz_type.equals(""))
                {
                    //Si le nom n'est pas vide on sauvgarde le nouveau quiz dans la BDD et on ferme le dialog
                    DB.quizzDAO().insert(new Quizz(quizz_type));
                    dialog.dismiss();
                }
                else
                {
                    //Si le nom est vide on Affiche message d'erreur
                    Toast.makeText(getApplicationContext(), "Remplir le type de Quizz SVP !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Créer un click listener pour le button Annuler et fermer le dialog
        AnnulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //Afficher le dialog
        dialog.show();
    }

    //Method pou l'ajout des Quizz dans la base de données
    public void Add_New_Quizz(ArrayList<Quizz> quizz, ArrayList<Question> questions, ArrayList<Proposition> propositions) {
        //Déclaration des variables
        String question;
        int nombre_propositions;
        ArrayList<String> propositions1;
        String type;
        int reponse;
        Long id1,id2;
        int quizz_id,question_id;
        Quizz q = null;
        Question qs = null;

        //Récuperation de la base de données
        Recuperer_bdd();

        //parcour de la liste des Quizz stocké dans la arrayList quizz
        for (int i = 0; i < quizz.size(); i++) {
            //Récuperation de la question
            question = questions.get(i).getQuestion();
            //Récuperation de nombr_proposition
            nombre_propositions = questions.get(i).getNombre_proposition();
            //Récuperation de la réponse
            reponse = questions.get(i).getReponse();
            //Récuperation des propositions
            propositions1 = propositions.get(i).getPropositions();
            //Récuperation de type de quizz
            type = quizz.get(i).getType();
            Quizz = new Quizz(type);

            //Vérifier si le quizz existe déja dans la BDD
            q = DB.quizzDAO().getQuizzByType(type);

            if(q == null)
            {
                //Si le quizz n'existe pas dans la BDD en va le stocké dans BDD
                id1 = DB.quizzDAO().insert(Quizz);
                //Récupration de l'id de Quizz ajouter
                quizz_id = id1.intValue();
            }
            else
            {
                //Si le quizz existe deja dans la BDD en récuper son id et
                quizz_id = q.getId();
            }
            //Vérifier si la question existe déja dans la BDD
            Question = new Question(question,reponse,nombre_propositions,quizz_id);
            qs = DB.questionDao().getQuestion(question);

            if(qs == null)
            {
                //Si la question n'existe pas dans la BDD en va la sauvegarder
                id2 = DB.questionDao().insert(Question);
                //Récupration de l'id de la Question ajouter
                question_id = id2.intValue();
                //Sauvegarder les propositions dans la base de données
                Propositions = new Proposition(propositions1,question_id);
                DB.propositionDao().insert(Propositions);
            }

        }
    }

    //Click button Supprimer tous les Quizzs
    public void DeleteAllQuizzs(View view) {
        //Récuperation de la base de données
        Recuperer_bdd();

        //Création dialog avec button oui et non pour confirmer la suppression de toutes les Quizz
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //si l'lutilisateur click sur oui en appler la méthode suprrimerAll() et on ferme le dialog
                        dialog.dismiss();
                        supprimerAll();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //Si l'user clique sur non on ferme seulment le dialog sans rien faire
                        dialog.dismiss();
                        break;
                }
            }
        };

        //Build de dialog et affichage
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vous êtes sur ?").setPositiveButton("Oui", dialogClickListener)
                .setNegativeButton("Non", dialogClickListener).show();
    }

    //Method de suppression de toutes les Quizz (Vider la BDD)
    public void supprimerAll() {
        //Récuperation de la base de données
        Recuperer_bdd();

        //Supprimer toutes les Quizz depuis la BDD
        DB.quizzDAO().deleteAll();
        //Supprimer toutes les Questions depuis la BDD
        DB.questionDao().deleteAll();
        //Supprimer toutes les Propositions depuis la BDD
        DB.propositionDao().deleteAll();
        //Supprimer toutes les Socres depuis la BDD
        DB.scoreDao().deleteAll();
        //Afficher message au user pour l'informer que loeration à été bien effectuer
        Toast.makeText(this, "Vous avez Supprimer tous les quizz !", Toast.LENGTH_LONG).show();
    }

    public void Recuperer_bdd()
    {
        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "quizz.db")
                .allowMainThreadQueries()
                .build();
    }
}
