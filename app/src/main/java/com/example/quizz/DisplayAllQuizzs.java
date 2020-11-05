package com.example.quizz;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Adapter.QuizzsMangementAdapter;
import com.example.quizz.DataBase.AppDatabase;
import com.example.quizz.DataBase.Quizz;

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
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz")
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

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //Supprimer le quizz depuis la base de donnée
            DB.quizzDAO().deleteQuizz(quizzs.get(viewHolder.getAdapterPosition()));
            //Suprrimer le quizz depuis l'ArrayList
            quizzs.remove(viewHolder.getAdapterPosition());
            //Notifier l'adapter qu'il ya un changement
            quizzsMangementAdapter.notifyDataSetChanged();
        }

    };


}

