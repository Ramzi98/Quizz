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
import com.example.quizz.QuestionsManagement;
import com.example.quizz.QuizzsList;
import com.example.quizz.R;

import java.lang.reflect.Modifier;
import java.util.List;

public class QuizzsMangementAdapter extends RecyclerView.Adapter<QuizzsMangementAdapter.MyViewHolder> {
    //Declaration des variables de l'adapter
    Context context;
    AppDatabase DB;
    List<Quizz> quizzs;
    int quizz_id = 0;
    //constructeur de l'adapter
    public QuizzsMangementAdapter(Context context,List<Quizz> quizzs)
    {
        this.quizzs = quizzs;
        this.context=context;
        DB = Room.databaseBuilder(context, AppDatabase.class , "quizz")
                .allowMainThreadQueries()
                .build();
    }


    @NonNull
    @Override
    public QuizzsMangementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.liste_quizzs_row,parent,false);
        return new QuizzsMangementAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuizzsMangementAdapter.MyViewHolder holder, final int position) {
        holder.Type.setText(quizzs.get(position).getType());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = quizzs.get(position).getId();
                Intent question_managment_intent = new Intent(context, QuestionsManagement.class);
                question_managment_intent.putExtra("id",String.valueOf(id));
                context.startActivity(question_managment_intent);
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_add_quizz);
                dialog.setTitle("Modifier un Quizz");
                final EditText edt = (EditText)dialog.findViewById(R.id.quizz_type_dialog);
                edt.setText(quizzs.get(position).getType());
                Button ModifierButton = (Button) dialog.findViewById(R.id.dialogButtonAjouter_quizz);
                ModifierButton.setText("Modifier");
                Button AnnulerButton = (Button) dialog.findViewById(R.id.dialogButtonEffacer_quizz);
                ModifierButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String quizz_type= edt.getText().toString();
                        int id = quizzs.get(position).getId();
                        if(!quizz_type.equals(""))
                        {
                            DB.quizzDAO().updateQuizz(quizz_type,id);
                            holder.Type.setText(quizz_type);
                            dialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(context, "Remplir le type de Quizz SVP !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AnnulerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizzs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Type;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Type = itemView.findViewById(R.id.quizz_type_gestion);
            linearLayout = itemView.findViewById(R.id.linear_layout_quizz_row);
        }
    }
}

