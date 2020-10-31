package com.example.quizz;

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

import com.example.quizz.Adapter.DisplayScoreAdapter;
import com.example.quizz.Adapter.GestionAdapter;
import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Question;
import com.example.quizz.DataBase.Score;

import java.util.List;

public class DisplayScoresOfQuizz extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Score> scores;
    DisplayScoreAdapter displayScoreAdapter;
    AppDatabase DB;
    int quizz_id;
    String quizz_type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_of_quizz);

        Intent intent = getIntent();
        if (intent.hasExtra("quizz_id"))
        {
            quizz_id = Integer.parseInt(intent.getStringExtra("quizz_id"));
        }

        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();

        quizz_type = DB.quizzDAO().getQuizzById(quizz_id).getType();
        if(!quizz_type.equals(null))
        {
            TextView textView = (TextView)findViewById(R.id.quizz_type_display);
            textView.setText(quizz_type);
        }


        scores = DB.scoreDao().getScoreByQuizzId(quizz_id);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_scores);
        displayScoreAdapter = new DisplayScoreAdapter(this,scores);
        recyclerView.setAdapter(displayScoreAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            DB.scoreDao().deleteScore(scores.get(viewHolder.getAdapterPosition()));
            scores.remove(viewHolder.getAdapterPosition());
            displayScoreAdapter.notifyDataSetChanged();
        }
    };


}
