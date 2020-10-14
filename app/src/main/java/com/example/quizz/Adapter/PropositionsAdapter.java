package com.example.quizz.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Question;
import com.example.quizz.R;

import java.util.ArrayList;


public class PropositionsAdapter extends RecyclerView.Adapter<PropositionsAdapter.MyViewHolder> {
    ArrayList<String> propositions;
    Context context;
    AppDatabase DB;
    Question question;
    private OnItemTouchListener onItemTouchListener;
    public PropositionsAdapter(Context context, ArrayList<String> propositions,int question_id,OnItemTouchListener onItemTouchListener)
    {
        DB = Room.databaseBuilder(context , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
        this.context=context;
        this.propositions=propositions;
        this.onItemTouchListener = onItemTouchListener;
        this.question = DB.questionDao().getQuestionByid(question_id);
    }

    @NonNull
    @Override
    public PropositionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.proposition_row,parent,false);
        return new PropositionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PropositionsAdapter.MyViewHolder holder, final int position) {
        String proposition1 = propositions.get(position);
        proposition1 = proposition1.replaceAll("\t", "");
        holder.proposition.setText(proposition1);
    }

    @Override
    public int getItemCount() {
        return propositions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button proposition;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            proposition = itemView.findViewById(R.id.proposition);
            proposition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View v1 = v;
                    new CountDownTimer(50, 50) {

                        public void onTick(long millisUntilFinished) {
                            if(question.getReponse() == (getAdapterPosition()+1))
                            {
                                int i = Color.parseColor("#539A00");
                                proposition.setBackgroundColor(i);
                            }
                            else if(question.getReponse() != (getAdapterPosition()+1))
                            {
                                int i = Color.parseColor("#E80000");
                                proposition.setBackgroundColor(i);
                            }
                        }

                        public void onFinish() {
                            onItemTouchListener.onButton1Click(v1, getAdapterPosition());
                        }
                    }.start();
                }
            });

        }

    }
}
