package com.example.quizz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Quizz;
import com.example.quizz.DataBase.Score;

public class DisplayScore extends AppCompatActivity {
    private int score,quizz_id,nbr_questions;
    private TextView tv_score;
    AppDatabase DB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);
        tv_score = (TextView) findViewById(R.id.tv_score);

        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "quizz")
                .allowMainThreadQueries()
                .build();

        Intent intent = getIntent();
        if (intent.hasExtra("score") && intent.hasExtra("quizz_id") && intent.hasExtra("nbr_questions"))
        {
            score = Integer.parseInt(intent.getStringExtra("score"));
            quizz_id = Integer.parseInt(intent.getStringExtra("quizz_id"));
            nbr_questions = Integer.parseInt(intent.getStringExtra("nbr_questions"));
        }
        tv_score.setText(score + " / " + nbr_questions);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_score);
        dialog.setTitle("Entrer votre Nom");
        Button AjouterButton = (Button) dialog.findViewById(R.id.dialogButtonAjouter_quizz);
        Button AnnulerButton = (Button) dialog.findViewById(R.id.dialogButtonEffacer_quizz);
        final EditText edt = (EditText)dialog.findViewById(R.id.score_type_dialog);
        AjouterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String player_name= edt.getText().toString();
                if(!player_name.equals(""))
                {
                    DB.scoreDao().insert(new Score(player_name,score,quizz_id));
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Remplir votre Nom SVP !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AnnulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
