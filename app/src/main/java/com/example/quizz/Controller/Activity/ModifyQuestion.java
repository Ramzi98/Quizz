package com.example.quizz.Controller.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quizz.Controller.Adapters.Add_PropostionsAdapter;
import com.example.quizz.Model.DataBase.AppDatabase;
import com.example.quizz.Model.DataBase.Proposition;
import com.example.quizz.Model.DataBase.Question;
import com.example.quizz.R;

import java.util.ArrayList;

public class ModifyQuestion extends AppCompatActivity {
    //Declaration des variables
    TextView textView;
    EditText ETquestion, ETreponse;
    Button btn;
    Proposition proposition;
    Question qst;
    String type, question, reponse1,proposition1;
    int reponse,quizz_id,question_id;
    ArrayList<String> propositions= new ArrayList<>();;
    RecyclerView recyclerView;
    Add_PropostionsAdapter Add_PropositionsAdapter;
    AppDatabase DB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);
        ETquestion = (EditText) findViewById(R.id.question_add);
        ETreponse = (EditText) findViewById(R.id.reponse_add);
        textView = findViewById(R.id.tv_add);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_addquestion);
        Add_PropositionsAdapter = new Add_PropostionsAdapter(this,propositions);
        btn = findViewById(R.id.btn);
        btn.setText("Modifier");
        textView.setText("Modifier une question");

        //Récuperation de la base de données
        DB = Room.databaseBuilder(getApplicationContext() , AppDatabase.class , "quizz.db")
                .allowMainThreadQueries()
                .build();

        //Récuperer les données envoyer par l intent d'avant
        Intent intent = getIntent();

        //Vérifier si l'Intent a envoyer les deux variable quizz_type et quizz_id et question_id et les récuperer
        if (intent.hasExtra("quizz_id") && intent.hasExtra("question_id"))
        {
            quizz_id = Integer.parseInt(intent.getStringExtra("quizz_id"));
            question_id = Integer.parseInt(intent.getStringExtra("question_id"));
        }


        //Récuperer les données depuis la BDD et l'afficher dans l'ecrans de modification
        proposition = DB.propositionDao().getPropositionByQuestionid(question_id);
        qst = DB.questionDao().getQuestionByid(question_id);
        ETquestion.setText(qst.getQuestion());
        ETreponse.setText(String.valueOf(qst.getReponse()));
        propositions = proposition.getPropositions();
        UpdatePropositionsFiled();
        recyclerView.setAdapter(Add_PropositionsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);


    }

    //Click button Modifier Question
    public void AddNewQuestion(View view) {
        //Récuperation des valeurs depuis les champs
        question = ETquestion.getText().toString();
        reponse1 = ETreponse.getText().toString();
        reponse = Integer.parseInt(reponse1);

        //Vérifier si les champs ne sont pas vide
        if(!TextUtils.isEmpty(question) && !TextUtils.isEmpty(reponse1) && propositions.size()>1)
        {
            //Vérifier que la réponse et logique (de 1er proposition jusqu'a la dernière)
            if(reponse > 0 && reponse <= propositions.size())
            {
                //Faire update de la question dans la BDD
                DB.questionDao().updateQuestion(question,reponse,propositions.size(),question_id);
                //Faire update des propositions de la question dans la BDD
                Proposition newproposition= new Proposition(propositions,question_id,proposition.getId());
                DB.propositionDao().updateProposition(newproposition);
                //Afficher message que l'operation à été bien effectuer
                Toast.makeText(this,"La Question a été modifier avec succès", Toast.LENGTH_LONG).show();
                //Fermer l'activity ModifyQuestion
                finish();

            }
            else
            {
                //Afficher message d'erreur au user pour que la reponse doit etre entre 1 et nombre_propositions
                Toast.makeText(this,"Reponse doit etre " +
                        "Nombre_propositions => reponse => 1", Toast.LENGTH_LONG).show();
            }

        }
        //Afficher message d'erreur pour l'user et lui demander de remplir tous les champs
        else
        {
            Toast.makeText(this,"Remplir tous les champs SVP !", Toast.LENGTH_LONG).show();
        }
    }

    //Click button Ajouter Proposition
    public void AddProposition(View view) {
        //custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_proposition);
        dialog.setTitle("Title");
        Button AjouterButton = (Button) dialog.findViewById(R.id.dialogButtonAjouter);
        Button EffacerButton = (Button) dialog.findViewById(R.id.dialogButtonEffacer);
        //Ajouter un Click listner pour la button Ajouter
        AjouterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Récuperer la proposition et lajouter dans arraylist propositions puis faire une mise a jour de recylerview
                proposition1 = ((EditText)dialog.findViewById(R.id.proposition_dialog)).getText().toString();
                System.out.println(proposition1);
                propositions.add(proposition1);
                UpdatePropositionsFiled();
                dialog.dismiss();
            }
        });
        //Ajouter un Click listner pour la button Effacer
        EffacerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText)dialog.findViewById(R.id.proposition_dialog)).setText("");
            }
        });
        //Afficher le dialog
        dialog.show();
    }

    //Faire une mise à jour de recyclerview pour ajouter nouvelle proposition ajouter
    public void UpdatePropositionsFiled() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_addquestion);
        Add_PropositionsAdapter = new Add_PropostionsAdapter(this,propositions);
        recyclerView.setAdapter(Add_PropositionsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    //OnSwip listner pour supprimer proposition on swipe
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            propositions.remove(viewHolder.getAdapterPosition());
            Add_PropositionsAdapter.notifyDataSetChanged();
        }
    };

}
