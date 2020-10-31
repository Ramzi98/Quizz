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

import com.example.quizz.Adapter.GestionAdapter;
import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Question;

import java.util.List;

public class QuestionsManagement extends AppCompatActivity {
    private RecyclerView recyclerView;
    List<Question> Questions;
    GestionAdapter gestionAdapter;
    AppDatabase DB;
    int quizz_id;
    String quizz_type;
    private static final String TAG = "LIFECYCLE_DEMO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_questions);
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
        Intent intent = getIntent();
        if (intent.hasExtra("id"))
        {
            quizz_id = Integer.parseInt(intent.getStringExtra("id"));
        }
        Questions = DB.questionDao().getQuestionByQuizzid(quizz_id);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gestionAdapter = new GestionAdapter(this,Questions);
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
            DB.questionDao().deleteQuestion(Questions.get(viewHolder.getAdapterPosition()));
            Questions.remove(viewHolder.getAdapterPosition());
            gestionAdapter.notifyDataSetChanged();
        }
    };

    public void btn_add_question(View view) {
        quizz_type = DB.quizzDAO().getQuizzById(quizz_id).getType();
        Intent intent = new Intent(this,AddQuestion.class);
        intent.putExtra("quizz_type",quizz_type);
        intent.putExtra("quizz_id",String.valueOf(quizz_id));
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "INSIDE: onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "INSIDE: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "INSIDE: onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "INSIDE: onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "INSIDE: onResume");
    }

}
