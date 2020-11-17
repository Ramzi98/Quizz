package com.example.quizz.Controller.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizz.Controller.Adapters.OnitemQuizzTouchListener;
import com.example.quizz.Controller.Adapters.QuizzsListAdapter;
import com.example.quizz.R;

public class QuizzsList extends Activity {
    //Déclaration des variables
    QuizzsListAdapter quizzsListAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialiser le contentview
        setContentView(R.layout.quizzs_list);
        //Récuperartion recyclerview depuis le View
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_quizzlist);
        //Initialisation de l'adapter
        quizzsListAdapter = new QuizzsListAdapter(this,onitemQuizzTouchListener,this.findViewById(android.R.id.content));
        //Affecter l'adapter à recyclerview
        recyclerView.setAdapter(quizzsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    //Click sur un item Quizz
    OnitemQuizzTouchListener onitemQuizzTouchListener = new OnitemQuizzTouchListener() {
        @Override
        public void onQuizzClick(int id) {
            //Crée nouvelle intent QuizzChoosed pour afficher le menu de quizz choisi
            Intent quizz_choosed_intent = new Intent(getApplicationContext(),QuizzChoosed.class);
            //Envoyer un extra (variable) id pour l'activity  QuizzChoosed
            quizz_choosed_intent.putExtra("id",String.valueOf(id));
            //Démarer l'activity
            startActivity(quizz_choosed_intent);
        }
    };
}
