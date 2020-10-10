package com.example.quizz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizz.Adapter.GestionAdapter;
import com.example.quizz.Adapter.PropositionsAdapter;
import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Quizz;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    RecyclerView.Adapter GestionAdapter;
    com.example.quizz.DataBase.Quizz Quizz;
    AppDatabase DB;
    List<com.example.quizz.DataBase.Quizz> Quizzs;
    DOMQuizz DOMQuizz;
    private int CurrentQuestion = 0;
    private int score = 0;
    RecyclerView.Adapter PropositionAdapter;
    RecyclerView recyclerView;
    TextView question,timer;
    Boolean supprimer = false;
    int prop = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        timer =(TextView) findViewById(R.id.timer);
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
        GestionAdapter = new GestionAdapter(this,Quizzs);
        RecupererQuizz();
        //Récuperations des données XML depuis le site
        if(Quizzs.size() == 0)
        {
            DOMQuizz = new DOMQuizz(this);
            DOMQuizz.execute();
        }
        else
        {
            UpdateQuestion();
        }
    }

    private void RecupererQuizz()
    {
        Quizzs = DB.quizzDAO().getAllQuizzs();
    }
    private void UpdateQuestion() {
        question =findViewById(R.id.question_main);
        question.setText(Quizzs.get(CurrentQuestion).getQuestion());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_propositions);
        PropositionAdapter = new PropositionsAdapter(this,Quizzs.get(CurrentQuestion).getPropositions(),Quizzs.get(CurrentQuestion).getNombre_proposition(),prop);
        recyclerView.setAdapter(PropositionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prop += Quizzs.get(CurrentQuestion).getNombre_proposition();
    }

    public void supprimerAllQuizz() {
        if(supprimer)
        {
            DB.quizzDAO().deleteAll();
            Toast.makeText(this,"Vous avez Supprimer tous les quizz !", Toast.LENGTH_LONG).show();
            UpdateQuestion();
            supprimer = false;
        }
    }
    public void Question_Suivante(View view) {

        if(CurrentQuestion < Quizzs.size()-1)
        {
            CurrentQuestion++;
            UpdateQuestion();
        }

    }
    public void Add_New_Quizz(ArrayList<com.example.quizz.DataBase.Quizz> quizz) {
        String question ;
        int nombre_propositions;
        ArrayList<String> propositions;
        String type;
        int reponse;

        for(int i=0;i<quizz.size();i++)
        {
            question = quizz.get(i).getQuestion();
            nombre_propositions = quizz.get(i).getNombre_proposition();
            reponse = quizz.get(i).getReponse();
            propositions = quizz.get(i).getPropositions();
            type = quizz.get(i).getType();
            Quizz = new Quizz(i+1,question,reponse,nombre_propositions,propositions,type);
            DB.quizzDAO().insert(Quizz);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent_add = new Intent(this,AddQuestion.class);
                startActivity(intent_add);
                return true;
            case R.id.modifier:
                Intent intent_gestion = new Intent(this,GestionQuestions.class);
                startActivity(intent_gestion);
                return true;
            case R.id.delete_all:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                supprimer=true;
                                dialog.dismiss();
                                supprimerAllQuizz();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                supprimer=false;
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Vous etes sur?").setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).show();
                return true;
            case R.id.add_quizz:
                DOMQuizz = new DOMQuizz(this);
                DOMQuizz.execute();
                Toast.makeText(this,"Vous avez Récuperer les quizz depuis xml", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}