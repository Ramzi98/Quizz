package com.example.quizz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Adapter.QuizzsMangementAdapter;
import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Quizz;

import java.util.List;

public class DisplayAllQuizzs extends AppCompatActivity {
    AppDatabase DB;
    List<Quizz> quizzs;
    RecyclerView recyclerView;
    QuizzsMangementAdapter quizzsMangementAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_quizzs);
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
        quizzs = DB.quizzDAO().getAllQuizzs();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_liste_quizzs);
        quizzsMangementAdapter = new QuizzsMangementAdapter(this,quizzs);
        recyclerView.setAdapter(quizzsMangementAdapter);
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
            DB.quizzDAO().deleteQuizz(quizzs.get(viewHolder.getAdapterPosition()));
            quizzs.remove(viewHolder.getAdapterPosition());
            quizzsMangementAdapter.notifyDataSetChanged();
        }

    };
}

