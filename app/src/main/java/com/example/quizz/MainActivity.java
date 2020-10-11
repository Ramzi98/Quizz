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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizz.Adapter.GestionAdapter;
import com.example.quizz.Adapter.OnItemTouchListener;
import com.example.quizz.Adapter.PropositionsAdapter;
import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Quizz;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    RecyclerView.Adapter GestionAdapter;
    RecyclerView.Adapter PropositionAdapter;
    com.example.quizz.DataBase.Quizz Quizz;
    AppDatabase DB;
    List<com.example.quizz.DataBase.Quizz> Quizzs;
    DOMQuizz DOMQuizz;
    private int CurrentQuestion = 0;
    private int score = 0;
    RecyclerView recyclerView;
    TextView question, timer;
    Boolean supprimer = false;
    TextView ScorePrint;
    int LAUNCH_RESPONSE_ACTIVITY = 2;
    Boolean[] Reponse_Consulter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        timer = (TextView) findViewById(R.id.timer);
        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "quizz")
                .allowMainThreadQueries()
                .build();
        GestionAdapter = new GestionAdapter(this, Quizzs);
        Quizzs = new ArrayList<>();
        //Récuperation des Quizz depuis la base de données DB
        Quizzs = DB.quizzDAO().getAllQuizzs();
        //Récuperations des données XML depuis le site
        if (Quizzs.size() == 0) {
            DOMQuizz = new DOMQuizz(this);
            DOMQuizz.execute();
        } else {
            UpdateQuestion();
        }
        Reponse_Consulter = new Boolean[Quizzs.size()];
        for (int i = 0; i < Reponse_Consulter.length; i++) {
            Reponse_Consulter[i] = false;
        }
    }

    public void RecupererQuizz() {
        Quizzs = DB.quizzDAO().getAllQuizzs();
    }

    public void UpdateQuestion() {
        if (Quizzs.size() != 0) {
            Quizzs = DB.quizzDAO().getAllQuizzs();
        }
        question = findViewById(R.id.question_main);
        question.setText(Quizzs.get(CurrentQuestion).getQuestion());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_propositions);
        PropositionAdapter = new PropositionsAdapter(this, Quizzs.get(CurrentQuestion).getPropositions(),itemTouchListener);
        recyclerView.setAdapter(PropositionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void supprimerAllQuizz() {
        if (supprimer) {
            DB.quizzDAO().deleteAll();
            Toast.makeText(this, "Vous avez Supprimer tous les quizz !", Toast.LENGTH_LONG).show();
            //UpdateQuestion();
            supprimer = false;
        }
    }

    public void Question_Suivante(View view) {

        if (CurrentQuestion < Quizzs.size() - 1) {
            CurrentQuestion++;
            UpdateQuestion();
        }
        else if(CurrentQuestion == Quizzs.size()-1)
        {
            PrintScore();
        }

    }

    public void Add_New_Quizz(ArrayList<com.example.quizz.DataBase.Quizz> quizz) {
        String question;
        int nombre_propositions;
        ArrayList<String> propositions;
        String type;
        int reponse;
        for (int i = 0; i < quizz.size(); i++) {
            question = quizz.get(i).getQuestion();
            nombre_propositions = quizz.get(i).getNombre_proposition();
            reponse = quizz.get(i).getReponse();
            propositions = quizz.get(i).getPropositions();
            type = quizz.get(i).getType();
            Quizz = new Quizz(i + 1, question, reponse, nombre_propositions, propositions, type);
            DB.quizzDAO().insert(Quizz);
        }
        if (Quizzs.size() == 0) {
            Quizzs = DB.quizzDAO().getAllQuizzs();
            UpdateQuestion();
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
                Intent intent_add = new Intent(this, AddQuestion.class);
                startActivity(intent_add);
                return true;
            case R.id.modifier:
                Intent intent_gestion = new Intent(this, QuestionsManagement.class);
                startActivity(intent_gestion);
                return true;
            case R.id.delete_all:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                supprimer = true;
                                dialog.dismiss();
                                supprimerAllQuizz();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                supprimer = false;
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
                Toast.makeText(this, "Vous avez Récuperer les quizz depuis xml", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void CalculateScore(View view,Boolean Reponse) {
        if(Reponse && !Reponse_Consulter[CurrentQuestion])
        {
            score++;
        }
        else if (Reponse_Consulter[CurrentQuestion])
        {
            Toast.makeText(this, "Vous avez Consulter la réponse !" +
                    "le point de cette Question ne sera pas prise en compte", Toast.LENGTH_LONG).show();
        }
        Question_Suivante(view);
    }

    public void PrintScore() {
        ScorePrint = (TextView) findViewById(R.id.score);
        ScorePrint.setText("Votre Score :"+score+"/"+Quizzs.size());
    }

    OnItemTouchListener itemTouchListener = new OnItemTouchListener() {
        @Override
        public void onButton1Click(View view, int position) {
            Log.d("TAG", "onButton1Click: "+position);
            if(position+1 == Quizzs.get(CurrentQuestion).getReponse())
            {
                CalculateScore(view,true);
            }
            else
            {
                CalculateScore(view,false);
            }
        }
    };

    public void AfficherReponse(View view) {
        Reponse_Consulter[CurrentQuestion] = true;
        Intent intent = new Intent(this,DisplayAnswer.class);
        intent.putExtra("id", String.valueOf(Quizzs.get(CurrentQuestion).getId()));
        Log.d("TAG", "AfficherReponse: "+Quizzs.get(CurrentQuestion).getId());
        startActivityForResult(intent, LAUNCH_RESPONSE_ACTIVITY);
    }
}