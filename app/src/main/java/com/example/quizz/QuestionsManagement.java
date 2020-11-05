package com.example.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Adapter.Add_PropostionsAdapter;
import com.example.quizz.Adapter.GestionAdapter;
import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Question;

import java.util.List;

public class QuestionsManagement extends AppCompatActivity {
    //Declaration des variable
    private RecyclerView recyclerView;
    List<Question> Questions;
    GestionAdapter gestionAdapter;
    AppDatabase DB;
    int quizz_id;
    String quizz_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialiser le contentview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_questions);
        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
        //Récuperer les données envoyer par l intent d'avant
        Intent intent = getIntent();

        //Vérifier si l'Intent a envoyer le variable id et le récuperer
        if (intent.hasExtra("id"))
        {
            quizz_id = Integer.parseInt(intent.getStringExtra("id"));
        }

        //Récuperer la question depuis la base de donnée
        Questions = DB.questionDao().getQuestionByQuizzid(quizz_id);
        //Récuperer le recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //Création d'adapter gestion adapter et affecter a recyclerview deja définie
        gestionAdapter = new GestionAdapter(this,Questions);
        recyclerView.setAdapter(gestionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

    }


    //OnSwiplistner pour supprimer Question on swipe
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //Supprimer la question depuis la base de donnée
            DB.questionDao().deleteQuestion(Questions.get(viewHolder.getAdapterPosition()));
            //Suprrimer la question depuis l'ArrayList
            Questions.remove(viewHolder.getAdapterPosition());
            //Notifier l'adapter qu'il ya un changement
            gestionAdapter.notifyDataSetChanged();
        }
    };

    //Controleur de la button add pour ajouter une question
    public void btn_add_question(View view) {
        //Récuperer le quizz_type de quizz courant
        quizz_type = DB.quizzDAO().getQuizzById(quizz_id).getType();
        Intent intent = new Intent(this,AddQuestion.class);
        //Envoyer deux parameter(quizz_type,quizz_id) pour lintent AddQuestion
        intent.putExtra("quizz_type",quizz_type);
        intent.putExtra("quizz_id",String.valueOf(quizz_id));
        //démarer l'activity
        startActivity(intent);
    }

    //Faire une mise à jour de recyclerview pour ajouter la nouvelle question
    public void UpdateQuestions() {
        //Récuperer les question depuis la BDD et faire une mise à jour de recyclerview
        Questions = DB.questionDao().getQuestionByQuizzid(quizz_id);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gestionAdapter = new GestionAdapter(this,Questions);
        recyclerView.setAdapter(gestionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Methode en Resume pour faire update pour le recyclerview
    @Override
    protected void onResume() {
        super.onResume();
        UpdateQuestions();
    }

}
