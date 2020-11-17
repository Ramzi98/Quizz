package com.example.quizz.Controller.Adapters;

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

import com.example.quizz.Model.DataBase.AppDatabase;
import com.example.quizz.Model.DataBase.Quizz;
import com.example.quizz.Controller.Activity.QuestionsManagement;
import com.example.quizz.R;

import java.util.List;

public class QuizzsMangementAdapter extends RecyclerView.Adapter<QuizzsMangementAdapter.MyViewHolder> {
    //Declaration des variables de l'adapter
    Context context;
    AppDatabase DB;
    List<Quizz> quizzs;

    //constructeur de l'adapter
    public QuizzsMangementAdapter(Context context,List<Quizz> quizzs)
    {
        this.quizzs = quizzs;
        this.context=context;
        //Récuperation de la base de données
        DB = Room.databaseBuilder(context, AppDatabase.class , "quizz.db")
                .allowMainThreadQueries()
                .build();
    }


    @NonNull
    @Override
    public QuizzsMangementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Récuperation de row View pour l'afficher
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.liste_quizzs_row,parent,false);
        return new QuizzsMangementAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuizzsMangementAdapter.MyViewHolder holder, final int position) {
        //Afficher le quizz type dans textView type
        holder.Type.setText(quizzs.get(position).getType());
        //Ajouter click listner sur quizz pour voir la liste de ces questions
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Récuperer le id de quizz toucher
                int id = quizzs.get(position).getId();

                //Crée nouvelle intent question_management pour afficher la liste des question de quizz spécifier
                Intent question_managment_intent = new Intent(context, QuestionsManagement.class);
                //Envoyer un extra (variable) id pour l'activity  QuestionManagement
                question_managment_intent.putExtra("id",String.valueOf(id));
                //Démarer l'activity
                context.startActivity(question_managment_intent);
            }
        });
        //Ajouter onLongclicklistner pour modifier un quizz
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Création dialog
                final Dialog dialog = new Dialog(context);
                //Récuperer le view de dialog
                dialog.setContentView(R.layout.dialog_add_quizz);
                //Ajouter un titre pour le dialog
                dialog.setTitle("Modifier un Quizz");
                //Récuperer l'edittext de dialog
                final EditText edt = (EditText)dialog.findViewById(R.id.quizz_type_dialog);
                //Afficher l'ancien Nom de quizz
                edt.setText(quizzs.get(position).getType());
                //Récuperer le button modifier depuis le view
                Button ModifierButton = (Button) dialog.findViewById(R.id.dialogButtonAjouter_quizz);
                ModifierButton.setText("Modifier");
                //Récuperer le button Annuler depuis le view
                Button AnnulerButton = (Button) dialog.findViewById(R.id.dialogButtonEffacer_quizz);
                //Ajouter Click listner sur le button modifier
                ModifierButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Récuperer le nouveau nom de quizz
                        final String quizz_type= edt.getText().toString();
                        //Récuperer l'id de quizz
                        int id = quizzs.get(position).getId();
                        if(!quizz_type.equals(""))
                        {
                            //Si le nom n'est pas vide donc on modifier le nom de quizz et on ferme le dialog
                            DB.quizzDAO().updateQuizz(quizz_type,id);
                            holder.Type.setText(quizz_type);
                            dialog.dismiss();

                        }
                        else
                        {
                            //Si le nom est vide on affiche un message d'erreur pour l'user
                            Toast.makeText(context, "Remplir le type de Quizz SVP !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //Onclick Listner pour button Annuler
                AnnulerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //lors de clique sur button annuler on ferme le dialog
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    //Récuperer le nombre des quizzs
    @Override
    public int getItemCount() {
        return quizzs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Type;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Récuperation de view type textviex et le linearlayout parent
            Type = itemView.findViewById(R.id.quizz_type_gestion);
            linearLayout = itemView.findViewById(R.id.linear_layout_quizz_row);
        }
    }
}

