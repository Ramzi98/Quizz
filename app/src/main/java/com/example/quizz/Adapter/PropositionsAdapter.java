package com.example.quizz.Adapter;

import android.content.Context;
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
import com.example.quizz.R;

import java.util.ArrayList;

public class PropositionsAdapter extends RecyclerView.Adapter<PropositionsAdapter.MyViewHolder> {
    ArrayList<String> propositions;
    Context context;
    AppDatabase DB;
    private OnItemTouchListener onItemTouchListener;
    public PropositionsAdapter(Context context, ArrayList<String> propositions,OnItemTouchListener onItemTouchListener)
    {
        DB = Room.databaseBuilder(context , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
        this.context=context;
        this.propositions=propositions;
        this.onItemTouchListener = onItemTouchListener;
    }

    @NonNull
    @Override
    public PropositionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.proposition_row,parent,false);
        return new PropositionsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropositionsAdapter.MyViewHolder holder, final int position) {
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
                    onItemTouchListener.onButton1Click(v, getAdapterPosition());
                }
            });
        }

    }
}
