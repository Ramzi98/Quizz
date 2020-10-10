package com.example.quizz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Quizz;
import com.example.quizz.R;

import java.util.List;

public class GestionAdapter extends RecyclerView.Adapter<GestionAdapter.MyViewHolder> {
    List<Quizz> Quizzs;
    Context context;
    AppDatabase DB;
    public GestionAdapter(Context context, List<Quizz> Quizzs)
    {
        DB = Room.databaseBuilder(context , AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();

        this.context=context;
        this.Quizzs=Quizzs;
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
        holder.question.setText(Quizzs.get(position).getQuestion());
        holder.number.setText(" "+String.valueOf(position+1)+" :");
        /*
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context,ModifyQuestion.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)Quizzs);
                intent1.putExtra("BUNDLE",args);
                intent1.putExtra("position",String.valueOf(position));
                context.startActivity(intent1);
            }
        });
         */

    }

    @Override
    public int getItemCount() {
        return Quizzs.size();
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