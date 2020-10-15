package com.example.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Adapter.GestionAdapter;
import com.example.quizz.Adapter.OnitemQuizzTouchListener;
import com.example.quizz.Adapter.PropositionsAdapter;
import com.example.quizz.Adapter.QuizzsListAdapter;
import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Quizz;

import java.util.List;

public class QuizzsList extends AppCompatActivity {

    QuizzsListAdapter quizzsListAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizzs_list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_quizzlist);
        quizzsListAdapter = new QuizzsListAdapter(this,onitemQuizzTouchListener);
        recyclerView.setAdapter(quizzsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    OnitemQuizzTouchListener onitemQuizzTouchListener = new OnitemQuizzTouchListener() {
        @Override
        public void onQuizzClick(int id) {
            Intent quizz_choosed_intent = new Intent(getApplicationContext(),QuizzChoosed.class);
            quizz_choosed_intent.putExtra("id",String.valueOf(id));
            startActivity(quizz_choosed_intent);
        }
    };
}
