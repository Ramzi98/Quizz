package com.example.quizz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Quizz;

public class DisplayAnswer extends AppCompatActivity {
    String id1;
    int id = 1;
    TextView question,reponse;
    AppDatabase DB;
    Quizz quizz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afficher_reponse);
        question = (TextView) findViewById(R.id.q2);
        reponse = (TextView) findViewById(R.id.r2);
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "quizz")
                .allowMainThreadQueries()
                .build();
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            try {
                id1 = intent.getStringExtra("id");
                id = Integer.valueOf(id1);
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }

        }
        Log.d("TAG", "onCreate: "+id1);
        quizz = DB.quizzDAO().getQuizz(id);
        String q = quizz.getQuestion();
        question.setText(q);
    }

    public void montrer_reponse(View view) {
        reponse.setText(quizz.getPropositions().get(quizz.getReponse()-1));
    }

    public void retour(View view) {
        //Intent returnIntent = new Intent();
        //returnIntent.putExtra("vu","true");
        //setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
