package com.example.quizz.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizz.R;

import java.util.ArrayList;

public class Add_PropostionsAdapter extends RecyclerView.Adapter<Add_PropostionsAdapter.MyViewHolder> {
    //Déclaration des variables
    ArrayList<String> propositions;
    Context context;

    //Constructeur
    public Add_PropostionsAdapter(Context context, ArrayList<String> propositions)
    {
        this.context=context;
        this.propositions=propositions;
    }


    @NonNull
    @Override
    public Add_PropostionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Récuperation de row View pour l'afficher
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_propositions_row,parent,false);
        return new Add_PropostionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Add_PropostionsAdapter.MyViewHolder holder, int position) {
        //Recuperation de proposition a afficher
        String proposition1 = propositions.get(position);
        //Affichage de la proposition dans text view
        holder.proposition.setText(proposition1);

    }

    //Récuperer le nombre d'élement Propositions
    @Override
    public int getItemCount() {
        return propositions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //Button
        Button proposition;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Récuperation View Proposition Button
            proposition = itemView.findViewById(R.id.proposition_row_button);
        }

    }
}
