package com.example.quizz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
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
import com.example.quizz.DataBase.Proposition;
import com.example.quizz.DataBase.Question;
import com.example.quizz.DataBase.Quizz;
import com.example.quizz.DataBase.Score;

import java.util.ArrayList;
import java.util.List;


public class Partie extends AppCompatActivity {
    RecyclerView.Adapter GestionAdapter;
    RecyclerView.Adapter PropositionAdapter;

    RecyclerView recyclerView;

    List<Quizz> quizzList = new ArrayList<>();;
    List<Question> questionList = new ArrayList<>();;
    List<Proposition> propositionList = new ArrayList<>();
    AppDatabase DB;
    Quizz Quizz;
    Question Question;
    Proposition Propositions;
    Score Score;
    DOMQuizz DOMQuizz;

    TextView question, timer;
    Boolean supprimer = false;
    TextView ScorePrint;

    int ADD_ACTIVITY = 1;
    int CurrentQuestion = 0;
    int score = 0;
    ArrayList<Boolean> Reponse_Consulter =new ArrayList<>();

    int quizz_id=0;
    int question_id=0;
    Proposition proposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = (TextView) findViewById(R.id.timer);
        GestionAdapter = new GestionAdapter(this, questionList);

        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "quizz")
                .allowMainThreadQueries()
                .build();

        //Récuperer les données passer vers cette intent
        Intent quizzList_intent = getIntent();
        if (quizzList_intent.hasExtra("id"))
        {
            quizz_id = Integer.parseInt(quizzList_intent.getStringExtra("id"));
        }


        //Récuperation des Quizz depuis la base de données DB
        questionList =DB.questionDao().getQuestionByQuizzid(quizz_id);
        //propositionList = DB.propositionDao().getAllPropositions();


        UpdateQuestion();


        // Initialisation du ArrayList Reponse_Consulter par false
        for (int i = 0 ; i < questionList.size() ; i++) {
            Reponse_Consulter.add(false);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        RecupererQuizz();
    }

    public void RecupererQuizz() {
        questionList =DB.questionDao().getQuestionByQuizzid(quizz_id);
        propositionList = DB.propositionDao().getAllPropositions();
    }

    public void UpdateQuestion() {
        if (questionList.size() != 0)
        {
            questionList =DB.questionDao().getQuestionByQuizzid(quizz_id);
            question = findViewById(R.id.question_main);
            question.setText(questionList.get(CurrentQuestion).getQuestion());
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_propositions);

            question_id = questionList.get(CurrentQuestion).getId();
            proposition = DB.propositionDao().getPropositionByQuestionid(question_id);

            PropositionAdapter = new PropositionsAdapter(this, proposition.getPropositions(),question_id,itemTouchListener);
            recyclerView.setAdapter(PropositionAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }



    public void Question_Suivante(View view) {
        if (CurrentQuestion < questionList.size()-1) {
            CurrentQuestion++;
            UpdateQuestion();
        }
        else if(CurrentQuestion == questionList.size()-1)
        {
            PrintScore();
        }

    }


    public void CalculateScore(View view,Boolean Reponse) {
        if(Reponse && !Reponse_Consulter.get(CurrentQuestion) && CurrentQuestion < questionList.size())
        {
            score++;
        }
        else if (Reponse_Consulter.get(CurrentQuestion))
        {
            Toast.makeText(this, "Vous avez Consulter la réponse !" +
                    "le point de cette Question ne sera pas prise en compte", Toast.LENGTH_LONG).show();
        }

        Question_Suivante(view);
    }

    public void PrintScore() {
        Intent score_intent = new Intent(this,DisplayScore.class);
        score_intent.putExtra("score",String.valueOf(score));
        score_intent.putExtra("quizz_id",String.valueOf(quizz_id));
        score_intent.putExtra("nbr_questions",String.valueOf(questionList.size()));
        startActivity(score_intent);
        finish();
        //ScorePrint = (TextView) findViewById(R.id.score);
        //ScorePrint.setText("Votre Score :"+score+"/"+ questionList.size());
    }

    OnItemTouchListener itemTouchListener = new OnItemTouchListener() {

        @Override
        public void onButton1Click(View view, int position) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(position+1 == questionList.get(CurrentQuestion).getReponse())
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
        //Vérifier si la liste des questions donc afficher la reponse
        if(!questionList.isEmpty()){
            //Mettre le variable Reponse_consulter true pour ne pas calculer cette question dans le score
            Reponse_Consulter.set(CurrentQuestion,true);
            //Créer new Intent DisplayAnswer la ou on va afficher la réponse
            Intent AnswerIntent = new Intent(this,DisplayAnswer.class);
            //Envoyer deux extra id et reponse pour l'intent Displayanswer
            AnswerIntent.putExtra("id", String.valueOf(questionList.get(CurrentQuestion).getId()));
            AnswerIntent.putExtra("reponse", String.valueOf(questionList.get(CurrentQuestion).getReponse()));
            Log.d("TAG", "AfficherReponse: "+ questionList.get(CurrentQuestion).getId());
            //Démarer l'activity
            startActivity(AnswerIntent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("score", score);
        savedInstanceState.putInt("CurrentQuestion", CurrentQuestion);


    }

    //onRestoreInstanceState
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        score = savedInstanceState.getInt("score");
        CurrentQuestion = savedInstanceState.getInt("CurrentQuestion");
        UpdateQuestion();

    }


}