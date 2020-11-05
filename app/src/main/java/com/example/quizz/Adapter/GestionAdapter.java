package com.example.quizz.Adapter;

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

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.ModifyQuestion;
import com.example.quizz.DataBase.Question;
import com.example.quizz.R;

import java.util.List;

public class GestionAdapter extends RecyclerView.Adapter<GestionAdapter.MyViewHolder> {
    List<Question> Questions;
    Context context;
    AppDatabase DB;
    public GestionAdapter(Context context, List<Question> Questions)
    {
        DB = Room.databaseBuilder(context , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();

        this.context=context;
        this.Questions=Questions;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gestion_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.question.setText(Questions.get(position).getQuestion());
        holder.number.setText(" "+String.valueOf(position+1)+" :");
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Modification d'une question
                int question_id = Questions.get(position).getId();
                int quizz_id = Questions.get(position).getQuizz_id();

                Intent question_managment_intent = new Intent(context, ModifyQuestion.class);
                question_managment_intent.putExtra("quizz_id",String.valueOf(quizz_id));
                question_managment_intent.putExtra("question_id",String.valueOf(question_id));

                context.startActivity(question_managment_intent);

            }
        });
    }

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
            question = itemView.findViewById(R.id.question);
            number = itemView.findViewById(R.id.number);
            linearLayout = itemView.findViewById(R.id.linear_layout);

        }
    }
}