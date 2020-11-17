package com.example.quizz.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizz.Model.DataBase.Score;
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
        //Récuperation de row View pour l'afficher
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.scores_of_quizz_row,parent,false);
        return new DisplayScoreAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DisplayScoreAdapter.MyViewHolder holder, final int position) {
        //Récuperation score depuis l'arraylist
        int s = scores.get(position).getScore();
        //Récuperation nombre de question depuis l'arraylist
        int nombre_question = scores.get(position).getNombre_question();
        //Afficher score / nombre_dequestion dans le text view dcore_tv
        holder.sc.setText(String.valueOf(s) + "/" + String.valueOf(nombre_question));
        //Afficher le nom du joueur dans textView correspondent
        holder.player_name.setText(scores.get(position).getPlayer_name());
    }

    //Récuperer le nombre d'élement Scores
    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView player_name;
        TextView sc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Récuperation View player name et score tv text views
            player_name = itemView.findViewById(R.id.player_name);
            sc = itemView.findViewById(R.id.score_tv);
        }
    }
}
