package com.example.quizz.Controller.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Controller.Adapters.QuizzsMangementAdapter;
import com.example.quizz.Model.DataBase.AppDatabase;
import com.example.quizz.Model.DataBase.Quizz;
import com.example.quizz.R;

import java.util.List;

public class DisplayAllQuizzs extends AppCompatActivity {
    //Déclaration des variables
    AppDatabase DB;
    List<Quizz> quizzs;
    RecyclerView recyclerView;
    QuizzsMangementAdapter quizzsMangementAdapter;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Initialiser le contentview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_quizzs);
        //recuperation de textview liste_quizzs
        textView = findViewById(R.id.txt_liste_quizzs);
        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz.db")
                .allowMainThreadQueries()
                .build();
        //récuperartions de tout les quizz depuis la BDD
        quizzs = DB.quizzDAO().getAllQuizzs();
        //Initialisation de Recyclerview
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_liste_quizzs);
        //Initialisation de l'adapter quizzManagement et l'affecter our le recyclerview
        quizzsMangementAdapter = new QuizzsMangementAdapter(this,quizzs);
        recyclerView.setAdapter(quizzsMangementAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
        if(quizzs.isEmpty())
        {
            textView.setText("Base de données Vide \n(Svp télecharger les quizz depuis le serveur ou bien ajouter vos propre quizz)");
        }
    }

    //OnSwiplistner pour supprimer Quizz on swipe
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        //Lorsque l'user fait un swap
        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            //Afficher un dialog pour confirmer la supprision d'un element lors d'un swap
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.dismiss();
                            //Supprimer le quizz depuis la base de donnée
                            DB.quizzDAO().deleteQuizz(quizzs.get(viewHolder.getAdapterPosition()));
                            //Suprrimer le quizz depuis l'ArrayList
                            quizzs.remove(viewHolder.getAdapterPosition());
                            //Notifier l'adapter qu'il ya un changement
                            quizzsMangementAdapter.notifyDataSetChanged();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            //Notifier l'adapter qu'il ya pas de changement
                            quizzsMangementAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(DisplayAllQuizzs.this);
            builder.setMessage("Vous êtes sur ?").setPositiveButton("Oui", dialogClickListener)
                    .setNegativeButton("Non", dialogClickListener).show();
            
        }

    };


}

