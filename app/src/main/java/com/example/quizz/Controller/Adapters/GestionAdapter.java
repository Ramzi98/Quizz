package com.example.quizz.Controller.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Model.DataBase.AppDatabase;
import com.example.quizz.Controller.Activity.ModifyQuestion;
import com.example.quizz.Model.DataBase.Question;
import com.example.quizz.R;

import java.util.List;

public class GestionAdapter extends RecyclerView.Adapter<GestionAdapter.MyViewHolder> {
    //Déclaration des variables
    List<Question> Questions;
    Context context;
    AppDatabase DB;

    //Constructeur

    public GestionAdapter(Context context, List<Question> Questions)
    {
        //Récuperation de la base de données
        DB = Room.databaseBuilder(context , AppDatabase.class , "quizz.db")
                .allowMainThreadQueries()
                .build();

        this.context=context;
        this.Questions=Questions;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Récuperation de row View pour l'afficher
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gestion_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //Afficher la question dans le textView Question
        holder.question.setText(Questions.get(position).getQuestion());
        //Afficher le numero de la question dans le textView correspandant
        holder.number.setText(" "+String.valueOf(position+1)+" :");
        //Onclicklistner
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Modification d'une question
                //Récuperation de quizz_id et question_id
                int question_id = Questions.get(position).getId();
                int quizz_id = Questions.get(position).getQuizz_id();

                //Crée nouvelle intent Modify Question pour afficher la question a modifier
                Intent question_managment_intent = new Intent(context, ModifyQuestion.class);
                //Envoyer des extra (variable) quizz_id et question_id pour l'activity  ModifyQuestion
                question_managment_intent.putExtra("quizz_id",String.valueOf(quizz_id));
                question_managment_intent.putExtra("question_id",String.valueOf(question_id));

                //Démmarer l'activity
                context.startActivity(question_managment_intent);

            }
        });
    }

    //Récuperer le nombre d'élement Question
    @Override
    public int getItemCount() {
        return Questions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView question;
        TextView number;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Récuperation des Views
            question = itemView.findViewById(R.id.question);
            number = itemView.findViewById(R.id.number);
            linearLayout = itemView.findViewById(R.id.linear_layout);

        }
    }
}