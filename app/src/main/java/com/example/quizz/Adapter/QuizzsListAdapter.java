package com.example.quizz.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Question;
import com.example.quizz.DataBase.Quizz;
import com.example.quizz.Partie;
import com.example.quizz.R;

import java.util.ArrayList;
import java.util.List;

public class QuizzsListAdapter extends RecyclerView.Adapter<QuizzsListAdapter.MyViewHolder> {

    //Declaration des variables de l'adapter
    Context context;
    AppDatabase DB;
    List<Quizz> quizzList;
    int quizz_id = 0;
    TextView textView;
    private OnitemQuizzTouchListener onitemQuizzTouchListener;
    //constructeur de l'adapter
    public QuizzsListAdapter(Context context,OnitemQuizzTouchListener onitemQuizzTouchListener,View itemview)
    {
        //Récuperation de la base de données
        DB = Room.databaseBuilder(context , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
        quizzList = DB.quizzDAO().getAllQuizzs();
        this.context=context;
        this.onitemQuizzTouchListener = onitemQuizzTouchListener;
        textView =  itemview.findViewById(R.id.tv_choisir_quizz);
        if(quizzList.isEmpty())
        {
            textView.setText("Base de données Vide \n(Svp télecharger les quizz depuis le serveur ou bien ajouter vos propre quizz)");
        }
    }


    @NonNull
    @Override
    public QuizzsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.quizzs_list_row,parent,false);
        return new QuizzsListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizzsListAdapter.MyViewHolder holder, final int position) {
        holder.button_quizz.setText(quizzList.get(position).getType());
        holder.button_quizz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizz_id = quizzList.get(position).getId();
                onitemQuizzTouchListener.onQuizzClick(quizz_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizzList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Button button_quizz;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button_quizz = itemView.findViewById(R.id.btn_quizz);
        }
    }
}
