package com.example.quizz.Controller.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizz.Controller.Adapters.GestionAdapter;
import com.example.quizz.Controller.Adapters.OnItemTouchListener;
import com.example.quizz.Controller.Adapters.PropositionsAdapter;
import com.example.quizz.Model.DataBase.AppDatabase;
import com.example.quizz.Model.DataBase.Proposition;
import com.example.quizz.Model.DataBase.Question;
import com.example.quizz.R;

import java.util.ArrayList;
import java.util.List;


public class Partie extends AppCompatActivity {

    //Declaration des variable
    RecyclerView.Adapter GestionAdapter;
    RecyclerView.Adapter PropositionAdapter;

    RecyclerView recyclerView;
    List<Question> questionList = new ArrayList<>();;
    List<Proposition> propositionList = new ArrayList<>();
    AppDatabase DB;

    TextView question;
    TextView question_number;
    TextView timerTextView;

    int CurrentQuestion = 0;
    int score = 0;
    ArrayList<Boolean> Reponse_Consulter =new ArrayList<>();
    int quizz_id=0;
    int question_id=0;
    Proposition proposition;
    CountDownTimer uy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        timerTextView = (TextView) findViewById(R.id.timer);
        GestionAdapter = new GestionAdapter(this, questionList);

        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "quizz.db")
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

        //Faire apparaitre la premier question sur l'ecran
        UpdateQuestion();

        // Initialisation du ArrayList Reponse_Consulter par false
        for (int i = 0 ; i < questionList.size() ; i++) {
            Reponse_Consulter.add(false);
        }


    }

    @Override
    protected void onResume() {
        //Methode en resume pour sauvegarder l'etat lors de rotation d'ecran
        super.onResume();
        RecupererQuizz();
    }

    //Methode pour recuperer les quizz depuis la BDD d'aprés le quizz_id
    public void RecupererQuizz() {
        questionList =DB.questionDao().getQuestionByQuizzid(quizz_id);
        propositionList = DB.propositionDao().getAllPropositions();
    }

    //Methode pour faire modfier la question dans l'affichage
    public void UpdateQuestion() {
        if (questionList.size() != 0)
        {

            //démarrer le timer
            method_timer(true,13000,timerTextView);

            //Récuperation de text view question et affichage de la question
            questionList =DB.questionDao().getQuestionByQuizzid(quizz_id);
            question = findViewById(R.id.question_main);
            question.setText(questionList.get(CurrentQuestion).getQuestion());
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_propositions);

            //Récuperation de text view num question et afficher le numero
            question_number = findViewById(R.id.question_number);
            question_number.setText("Question : " + (CurrentQuestion+1) + " / " + questionList.size());

            //Récuperation des propositions
            question_id = questionList.get(CurrentQuestion).getId();
            proposition = DB.propositionDao().getPropositionByQuestionid(question_id);

            //Création de l'adapter des propositions et le relier avec le recyclerView
            PropositionAdapter = new PropositionsAdapter(this, proposition.getPropositions(),question_id,itemTouchListener);
            recyclerView.setAdapter(PropositionAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }



    //Method qui permet de changer vers la question suivant et appler la method de score a la fin
    public void Question_Suivante(View view) {
        //Arrêter le timer
        method_timer(false,0,view);

        //Si on est pas dans la derniere question on avance vers la question suivante
        if (CurrentQuestion < questionList.size()-1) {
            CurrentQuestion++;
            //Appel methode UpdateQuestion pour afficher la nouvelle question
            UpdateQuestion();
        }
        else if(CurrentQuestion == questionList.size()-1)
        {
            //Si on est dans la dernier question on appel la method Print Score pour afficher le score
            PrintScore();
        }

    }

    //Methode pour calculer le score
    public void CalculateScore(View view,Boolean Reponse) {

        if(Reponse && !Reponse_Consulter.get(CurrentQuestion) && CurrentQuestion < questionList.size())
        {
            //Si la réponse est juste en incrément le score
            score++;
        }
        else if (Reponse_Consulter.get(CurrentQuestion))
        {
            //Si la réponse a été consulté On incrément pas le score et on affiche un message au user
            Toast.makeText(this, "Vous avez Consulter la réponse !" +
                    "le point de cette Question ne sera pas prise en compte", Toast.LENGTH_LONG).show();
        }

        //et on avance vers la question suivante
        Question_Suivante(view);
    }

    //Methode pour afficher l'activity qui va afficher le score finale
    public void PrintScore() {
        //Crée nouvelle intent Displayscore pour afficher le score dans cette activity
        Intent score_intent = new Intent(this,DisplayScore.class);

        //Envoyer le score et le quizz id et le nombre de question
        score_intent.putExtra("score",String.valueOf(score));
        score_intent.putExtra("quizz_id",String.valueOf(quizz_id));
        score_intent.putExtra("nbr_questions",String.valueOf(questionList.size()));

        //Démarage de la nouvelle activity
        startActivity(score_intent);

        //Fermuture de l'activity courante
        finish();
    }

    //Touch listner pour vérifier si user a choisi la bon réponse
    OnItemTouchListener itemTouchListener = new OnItemTouchListener() {

        @Override
        public void onButton1Click(View view, int position) {
            //Un sleep pour voir si la réponse est juste ou pas
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if(position+1 == questionList.get(CurrentQuestion).getReponse())
            {
                //Calculer le nouveau score avec réponse juste
                CalculateScore(view,true);
            }
            else
            {
                //Calculer le nouveau score avec réponse fausse
                CalculateScore(view,false);
            }
        }
    };

    //Click button Afficher reponse
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

    //Sauvegarder l'etat de l'activity
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        super.onSaveInstanceState(savedInstanceState);

        //Sauvegarder le score et la question courante
        savedInstanceState.putInt("score", score);
        savedInstanceState.putInt("CurrentQuestion", CurrentQuestion);


    }

    //onRestoreInstanceState
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        //Récuperer le score et la question
        score = savedInstanceState.getInt("score");
        CurrentQuestion = savedInstanceState.getInt("CurrentQuestion");
        UpdateQuestion();

    }


    //Methode Timer
    void method_timer(boolean u, int max, final View v) {
        //Déclaration des variables
        boolean uu = u;
        final int maxi = max;

        if (uu == true) {
            //Si variable est true donc on commance le timercountdown
            uy = new CountDownTimer(maxi, 1000) {
                public void onFinish() {
                    //A la fin du timer si user a rien choisi en passe vers une autre question
                    Question_Suivante(v);
                }

                //Affichage de temps sur l'ecran de Quizz
                @Override
                public void onTick(long l) {
                    timerTextView.setText(String.valueOf(l/1000));
                }
            }.start();
        } else {
            //Stop timer de la question si le user a cliquer sur suivant ou a reponder sur la question
            uy.cancel();
        }
    }


}