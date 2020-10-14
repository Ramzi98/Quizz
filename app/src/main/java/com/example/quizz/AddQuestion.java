package com.example.quizz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Adapter.Add_PropostionsAdapter;
import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Proposition;
import com.example.quizz.DataBase.Question;
import com.example.quizz.DataBase.Quizz;

import java.util.ArrayList;

public class AddQuestion extends AppCompatActivity {
    EditText ETtype, ETquestion, ETreponse;
    String type, question, reponse1,proposition1;
    int reponse;
    ArrayList<String> propositions= new ArrayList<>();;
    RecyclerView recyclerView;
    Add_PropostionsAdapter Add_PropositionsAdapter;
    AppDatabase DB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);
        ETtype = (EditText) findViewById(R.id.type_question_add);
        ETquestion = (EditText) findViewById(R.id.question_add);
        ETreponse = (EditText) findViewById(R.id.reponse_add);

        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
    }

    //Click button Ajouter Question
    public void AddNewQuestion(View view) {
        int quizz_id,question_id;
        Long id,id1;
        //Récuperation des valeurs depuis les champs
        type = ETtype.getText().toString();
        question = ETquestion.getText().toString();
        reponse1 = ETreponse.getText().toString();
        reponse = Integer.parseInt(reponse1);
        if(!TextUtils.isEmpty(type) && !TextUtils.isEmpty(question) && !TextUtils.isEmpty(reponse1) && propositions.size()>1)
        {
            if(reponse > 0 && reponse <= propositions.size())
            {
                Quizz q = DB.quizzDAO().getQuizzByType(type);
                Question qs = DB.questionDao().getQuestion(question);
                if(q == null && qs == null )
                {
                    Quizz quizz =new Quizz(type);
                    id= DB.quizzDAO().insert(quizz);
                    quizz_id = id.intValue();
                    Question qst = new Question(question,reponse,propositions.size(),quizz_id);
                    id1 = DB.questionDao().insert(qst);
                    question_id = id1.intValue();
                    Proposition prop = new Proposition(propositions,question_id);
                    DB.propositionDao().insert(prop);
                    Toast.makeText(this,"La Question a été ajouté avec succès", Toast.LENGTH_LONG).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                else if(q != null)
                {
                    quizz_id = q.getId();
                    Question qst = new Question(question,reponse,propositions.size(),quizz_id);
                    id1 = DB.questionDao().insert(qst);
                    question_id = id1.intValue();
                    Proposition prop = new Proposition(propositions,question_id);
                    DB.propositionDao().insert(prop);
                    Toast.makeText(this,"La Question a été ajouté avec succès", Toast.LENGTH_LONG).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                else  if(qs != null)
                {
                    Toast.makeText(this,"Question Existe déja !", Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                Toast.makeText(this,"Reponse doit etre " +
                        "Nombre_propositions => reponse => 1", Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            Toast.makeText(this,"Remplir tous les champs SVP !", Toast.LENGTH_LONG).show();
        }
    }

    public void AddProposition(View view) {
        //custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_proposition);
        dialog.setTitle("Title");
        Button AjouterButton = (Button) dialog.findViewById(R.id.dialogButtonAjouter);
        Button EffacerButton = (Button) dialog.findViewById(R.id.dialogButtonEffacer);
        //final EditText proposition = (EditText) findViewById(R.id.proposition_dialog);
        AjouterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proposition1 = ((EditText)dialog.findViewById(R.id.proposition_dialog)).getText().toString();
                System.out.println(proposition1);
                propositions.add(proposition1);
                UpdatePropositionsFiled();
                dialog.dismiss();
            }
        });

        EffacerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText)dialog.findViewById(R.id.proposition_dialog)).setText("");
            }
        });
        dialog.show();
    }
    public void UpdatePropositionsFiled() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_addquestion);
        Add_PropositionsAdapter = new Add_PropostionsAdapter(this,propositions);
        recyclerView.setAdapter(Add_PropositionsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
