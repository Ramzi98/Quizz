package com.example.quizz.Controller.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Model.DataBase.AppDatabase;
import com.example.quizz.Model.DataBase.Question;
import com.example.quizz.R;

import java.util.ArrayList;


public class PropositionsAdapter extends RecyclerView.Adapter<PropositionsAdapter.MyViewHolder> {

    //Declaration des variables de l'adapter
    ArrayList<String> propositions;
    Context context;
    AppDatabase DB;
    Question question;
    private OnItemTouchListener onItemTouchListener;

    public PropositionsAdapter(Context context, ArrayList<String> propositions,int question_id,OnItemTouchListener onItemTouchListener)
    {
        //Récuperation de la base de données
        DB = Room.databaseBuilder(context , AppDatabase.class , "quizz.db")
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
        //Récuperation de row View pour l'afficher
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.proposition_row,parent,false);
        return new PropositionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PropositionsAdapter.MyViewHolder holder, final int position) {
        //Récuperation proposition depuis l'arraylist et enlver les espaces de plus
        String proposition1 = propositions.get(position);
        proposition1 = proposition1.replaceAll("\t", "");
        //Afficher la proposition dans le textView correspondent
        holder.proposition.setText(proposition1);
    }

    //Récuperer le nombre des propositions
    @Override
    public int getItemCount() {
        return propositions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button proposition;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Récuperation de itemview proposition
            proposition = itemView.findViewById(R.id.proposition);
            //Ajouter clicklistner sur le button proposition
            proposition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View v1 = v;
                    //Sleep 1 Seconde lors de selection de la réponse pour que le user peut voir si la r&ponse juste ou bien fausse
                    new CountDownTimer(50, 50) {

                        public void onTick(long millisUntilFinished) {
                            if(question.getReponse() == (getAdapterPosition()+1))
                            {
                                //Si la réponse est juste on colorer le button avec le vert
                                int i = Color.parseColor("#539A00");
                                proposition.setBackgroundColor(i);
                            }
                            else if(question.getReponse() != (getAdapterPosition()+1))
                            {
                                //Si la réponse est fausse on colorer le button avec le rouge
                                int i = Color.parseColor("#E80000");
                                proposition.setBackgroundColor(i);
                            }
                        }

                        public void onFinish() {
                            //A la fin en appel TouchListner pour avancer vers la question suivante
                            onItemTouchListener.onButton1Click(v1, getAdapterPosition());
                        }
                    }.start();
                }
            });

        }

    }
}
