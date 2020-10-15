package com.example.quizz;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Proposition;
import com.example.quizz.DataBase.Question;
import com.example.quizz.DataBase.Quizz;

import java.util.ArrayList;

public class QuizzsGestion extends AppCompatActivity {

    DOMQuizz DOMQuizz;
    AppDatabase DB;
    Quizz Quizz;
    Question Question;
    Proposition Propositions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_quizzs);

        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "quizz")
                .allowMainThreadQueries()
                .build();
    }

    //Récuperations des données XML depuis le site
    public void TelechargerQuizzs(View view) {
        DOMQuizz = new DOMQuizz(this);
        DOMQuizz.execute();
        Toast.makeText(this, "Vous avez Récuperer les quizz depuis le serveur", Toast.LENGTH_LONG).show();
    }

    public void EditionQuizz(View view) {
    }

    public void CreationQuizz(View view) {
    }

    public void Add_New_Quizz(ArrayList<Quizz> quizz, ArrayList<Question> questions, ArrayList<Proposition> propositions) {
        String question;
        int nombre_propositions;
        ArrayList<String> propositions1;
        String type;
        int reponse;
        Long id1,id2;
        int quizz_id,question_id;
        for (int i = 0; i < quizz.size(); i++) {
            question = questions.get(i).getQuestion();
            nombre_propositions = questions.get(i).getNombre_proposition();
            reponse = questions.get(i).getReponse();
            propositions1 = propositions.get(i).getPropositions();
            type = quizz.get(i).getType();
            Quizz = new Quizz(type);
            Quizz q = DB.quizzDAO().getQuizzByType(type);
            if(q == null)
            {
                id1 = DB.quizzDAO().insert(Quizz);
                quizz_id = id1.intValue();
            }
            else
            {
                quizz_id = q.getId();
            }
            Question = new Question(question,reponse,nombre_propositions,quizz_id);
            Question qs = DB.questionDao().getQuestion(question);
            if(qs == null)
            {
                id2 = DB.questionDao().insert(Question);
                question_id = id2.intValue();
                Propositions = new Proposition(propositions1,question_id);
                DB.propositionDao().insert(Propositions);
            }

        }
    }

    public void DeleteAllQuizzs(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        supprimerAll();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vous etes sur?").setPositiveButton("Oui", dialogClickListener)
                .setNegativeButton("Non", dialogClickListener).show();
    }

    public void supprimerAll() {
        DB.quizzDAO().deleteAll();
        DB.questionDao().deleteAll();
        DB.propositionDao().deleteAll();
        Toast.makeText(this, "Vous avez Supprimer tous les quizz !", Toast.LENGTH_LONG).show();
    }
}
