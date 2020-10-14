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

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        timer = (TextView) findViewById(R.id.timer);
        GestionAdapter = new GestionAdapter(this, questionList);

        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "quizz")
                .allowMainThreadQueries()
                .build();


        //Récuperation des Quizz depuis la base de données DB
        quizzList = DB.quizzDAO().getAllQuizzs();
        questionList =DB.questionDao().getAllQuestions();
        propositionList = DB.propositionDao().getAllPropositions();

        //Récuperations des données XML depuis le site
        if (questionList.size() == 0 ) {
            DOMQuizz = new DOMQuizz(this);
            DOMQuizz.execute();
        } else {
            UpdateQuestion();
        }

        // Initialisation du ArrayList Reponse_Consulter par false

        for (int i = 0 ; i < questionList.size() ; i++) {
            Reponse_Consulter.add(false);
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        RecupererQuizz();
    }

    public void RecupererQuizz() {
        quizzList = DB.quizzDAO().getAllQuizzs();
        questionList =DB.questionDao().getAllQuestions();
        propositionList = DB.propositionDao().getAllPropositions();
    }

    public void UpdateQuestion() {
        if (questionList.size() != 0)
        {
            questionList = DB.questionDao().getAllQuestions();
            question = findViewById(R.id.question_main);
            question.setText(questionList.get(CurrentQuestion).getQuestion());
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_propositions);
            PropositionAdapter = new PropositionsAdapter(this, propositionList.get(CurrentQuestion).getPropositions(),questionList.get(CurrentQuestion).getId(),itemTouchListener);
            recyclerView.setAdapter(PropositionAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void supprimerAllQuizz() {
        if (supprimer) {
            DB.quizzDAO().deleteAll();
            DB.questionDao().deleteAll();
            DB.propositionDao().deleteAll();
            Toast.makeText(this, "Vous avez Supprimer tous les quizz !", Toast.LENGTH_LONG).show();
            //UpdateQuestion();
            supprimer = false;
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


        if (questionList.size() == 0)
        {
            quizzList = DB.quizzDAO().getAllQuizzs();
            questionList = DB.questionDao().getAllQuestions();
            propositionList = DB.propositionDao().getAllPropositions();
            UpdateQuestion();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                RecupererQuizz();
                UpdateQuestion();
                Reponse_Consulter.add(false);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
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
                startActivityForResult(intent_add,ADD_ACTIVITY);
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
        ScorePrint = (TextView) findViewById(R.id.score);
        ScorePrint.setText("Votre Score :"+score+"/"+ questionList.size());
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
        Reponse_Consulter.set(CurrentQuestion,true);
        Intent AnswerIntent = new Intent(this,DisplayAnswer.class);
        AnswerIntent.putExtra("id", String.valueOf(questionList.get(CurrentQuestion).getId()));
        AnswerIntent.putExtra("reponse", String.valueOf(questionList.get(CurrentQuestion).getReponse()));
        Log.d("TAG", "AfficherReponse: "+ questionList.get(CurrentQuestion).getId());
        startActivity(AnswerIntent);
    }

    public  void ColoredPropositions() {

    }
}