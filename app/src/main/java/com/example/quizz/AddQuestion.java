package com.example.quizz;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddQuestion extends AppCompatActivity {
    EditText type,question,reponse;
    String type1,question1,reponse1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);
        type = (EditText) findViewById(R.id.type_question_add);
        question = (EditText) findViewById(R.id.question_add);
        reponse = (EditText) findViewById(R.id.reponse_add);
    }

    public void AddNewQuestion(View view) {
        type1 = type.getText().toString();
        question1 = question.getText().toString();
        reponse1 = reponse.getText().toString();
        if(!TextUtils.isEmpty(type1) && !TextUtils.isEmpty(question1) && !TextUtils.isEmpty(reponse1))
        {

        }
        else
        {
            Toast.makeText(this,"Remplir toutes les champs SVP !", Toast.LENGTH_LONG).show();
        }
    }
}
