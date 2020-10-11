package com.example.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Adapter.GestionAdapter;
import com.example.quizz.DataBase.AppDatabase;

import java.util.List;

public class QuestionsManagement extends AppCompatActivity {
    private RecyclerView recyclerView;
    List<com.example.quizz.DataBase.Quizz> Quizz;
    GestionAdapter gestionAdapter;
    AppDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_questions);
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
        Quizz = DB.quizzDAO().getAllQuizzs();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gestionAdapter = new GestionAdapter(this,Quizz);
        recyclerView.setAdapter(gestionAdapter);
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
            DB.quizzDAO().deleteQuizz(Quizz.get(viewHolder.getAdapterPosition()));
            Quizz.remove(viewHolder.getAdapterPosition());
            gestionAdapter.notifyDataSetChanged();
        }
    };

    public void btn_add_question(View view) {
        Intent intent = new Intent(this,AddQuestion.class);
        startActivity(intent);
    }
}
