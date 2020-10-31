package com.example.quizz.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Quizz;
import com.example.quizz.DataBase.Score;
import com.example.quizz.QuestionsManagement;
import com.example.quizz.R;

import java.util.List;

public class DisplayScoreAdapter extends RecyclerView.Adapter<DisplayScoreAdapter.MyViewHolder>{
    //Declaration des variables de l'adapter
    Context context;
    List<Score> scores;

    //constructeur de l'adapter
    public DisplayScoreAdapter(Context context,List<Score> scores)
    {
        this.scores = scores;
        this.context=context;
    }


    @NonNull
    @Override
    public DisplayScoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.scores_of_quizz_row,parent,false);
        return new DisplayScoreAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DisplayScoreAdapter.MyViewHolder holder, final int position) {
        int s= scores.get(position).getScore();
        holder.sc.setText(String.valueOf(s));
        holder.player_name.setText(scores.get(position).getPlayer_name());
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView player_name;
        TextView sc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            player_name = itemView.findViewById(R.id.player_name);
            sc = itemView.findViewById(R.id.score_tv);
        }
    }
}
