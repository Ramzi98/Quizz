package com.example.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Question;

public class DisplayAnswer extends AppCompatActivity {
    String id1;
    String reponse;
    int id = 1,r = 1;
    TextView Tquestion,Treponse;
    AppDatabase DB;
    Question question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afficher_reponse);
        Tquestion = (TextView) findViewById(R.id.q2);
        Treponse = (TextView) findViewById(R.id.r2);

        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "quizz")
                .allowMainThreadQueries()
                .build();


        //Récuperation des extra
        Intent intent = getIntent();
        if (intent.hasExtra("id"))
        {
            id1 = intent.getStringExtra("id");
            id = Integer.valueOf(id1);
        }
        if (intent.hasExtra("reponse"))
        {
            id1 = intent.getStringExtra("reponse");
            r = Integer.valueOf(id1);
        }

        //Récuperation de question et reponse depuis la base de données
        question = DB.questionDao().getQuestionByid(id);
        reponse = DB.propositionDao().getPropositionByQuestionid(id).getPropositions().get(r);

        //Affichage de la question
        Tquestion.setText(question.getQuestion());
    }

    //Click Button Montrer reponse
    public void montrer_reponse(View view) {
        Treponse.setText(reponse);
    }

    //Click Button Retour
    public void retour(View view) {
        finish();
    }
}