package com.example.quizz.Controller.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Controller.Adapters.DisplayScoreAdapter;
import com.example.quizz.Model.DataBase.AppDatabase;
import com.example.quizz.Model.DataBase.Score;
import com.example.quizz.R;

import java.util.List;

public class DisplayScoresOfQuizz extends AppCompatActivity {
    //Déclaration des variable
    RecyclerView recyclerView;
    List<Score> scores;
    DisplayScoreAdapter displayScoreAdapter;
    AppDatabase DB;
    int quizz_id;
    String quizz_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialiser le contentview
        setContentView(R.layout.scores_of_quizz);

        //Récuperer les données envoyer par l activity d'avant
        Intent intent = getIntent();
        //Vérifier si l'Intent a envoyer le variable id et le récuperer
        if (intent.hasExtra("quizz_id"))
        {
            //Récuprer le variable quizz_id depuis l'activity d'avant
            quizz_id = Integer.parseInt(intent.getStringExtra("quizz_id"));
        }

        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz.db")
                .allowMainThreadQueries()
                .build();

        //Récuperation de quizz depuis la BDD avec le quizz id récuperer depuis l'activity d'avant
        quizz_type = DB.quizzDAO().getQuizzById(quizz_id).getType();
        //Vérifier si le quizz existe dans la BDD
        if(!quizz_type.equals(null))
        {
            //Si le quizz existe on va récuprer le text view quizz_type_display et on affiche type de quizz dans le textview
            TextView textView = (TextView)findViewById(R.id.quizz_type_display);
            textView.setText(quizz_type);
        }

        //Récuprer les scores de quizz spécifier avec le quizz id
        scores = DB.scoreDao().getScoreByQuizzId(quizz_id);
        //Récuprer le recycler View
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_scores);
        //Créer un adapter Display score pour afficher tt les scores
        displayScoreAdapter = new DisplayScoreAdapter(this,scores);
        //Liée l'adapter avec le recycler view
        recyclerView.setAdapter(displayScoreAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Créer un itemtouchHelper
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

    }

    //Itemtouche helper pour supprimer le score avec le swipe
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        //Lorsque l'utilisateur fait un Swap
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //suppression du score depuis la bdd
            DB.scoreDao().deleteScore(scores.get(viewHolder.getAdapterPosition()));
            //Enlver le score depuis l'arrayList
            scores.remove(viewHolder.getAdapterPosition());
            //Notify l'adapter q'il ya un changement
            displayScoreAdapter.notifyDataSetChanged();
        }
    };


}
