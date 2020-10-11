package com.example.quizz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.R;

import java.util.ArrayList;

public class Add_PropostionsAdapter extends RecyclerView.Adapter<Add_PropostionsAdapter.MyViewHolder> {
    ArrayList<String> propositions;
    Context context;

    public Add_PropostionsAdapter(Context context, ArrayList<String> propositions)
    {
        this.context=context;
        this.propositions=propositions;
    }
    @NonNull
    @Override
    public Add_PropostionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_propositions_row,parent,false);
        return new Add_PropostionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Add_PropostionsAdapter.MyViewHolder holder, int position) {
        String proposition1 = propositions.get(position);
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
            proposition = itemView.findViewById(R.id.proposition_row_button);
        }

    }
}
