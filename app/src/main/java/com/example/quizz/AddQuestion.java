package com.example.quizz;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Adapter.Add_PropostionsAdapter;
import com.example.quizz.Adapter.PropositionsAdapter;
import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Quizz;

import java.util.ArrayList;

public class AddQuestion extends AppCompatActivity {
    EditText type,question,reponse;
    String type1,question1,reponse1,proposition1;
    int reponse2;
    ArrayList<String> propositions= new ArrayList<>();;
    RecyclerView recyclerView;
    Add_PropostionsAdapter Add_PropositionsAdapter;
    AppDatabase DB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);
        type = (EditText) findViewById(R.id.type_question_add);
        question = (EditText) findViewById(R.id.question_add);
        reponse = (EditText) findViewById(R.id.reponse_add);
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
    }

    public void AddNewQuestion(View view) {
        type1 = type.getText().toString();
        question1 = question.getText().toString();
        reponse1 = reponse.getText().toString();
        reponse2 = Integer.parseInt(reponse1);
        if(!TextUtils.isEmpty(type1) && !TextUtils.isEmpty(question1) && !TextUtils.isEmpty(reponse1) && propositions.size()>1)
        {
            if(reponse2 > 0 && reponse2 <= propositions.size())
            {
                Quizz quizz =new Quizz(question1,reponse2,propositions.size(),propositions,type1);
                DB.quizzDAO().insert(quizz);
                Toast.makeText(this,"La Question a été ajouté avec succès", Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(this,"Reponse doit etre " +
                        "Nombre_propositions => reponse => 1", Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            Toast.makeText(this,"Remplir toutes les champs SVP !", Toast.LENGTH_LONG).show();
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
