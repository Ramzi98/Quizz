package com.example.quizz.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
@Entity
public class Quizz {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "question")
    private String question;
    @ColumnInfo(name = "reponse")
    private int reponse;
    @ColumnInfo(name = "nombre_proposition")
    private int nombre_proposition;
    @ColumnInfo(name = "propositions")
    private ArrayList<String> propositions;
    @ColumnInfo(name = "type")
    private String type;

    public Quizz(int id, String question, int reponse, int nombre_proposition, ArrayList<String> propositions, String type) {
        this.id = id;
        this.question = question;
        this.reponse = reponse;
        this.nombre_proposition = nombre_proposition;
        this.propositions = propositions;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setReponse(int reponse) {
        this.reponse = reponse;
    }

    public void setNombre_proposition(int nombre_proposition) {
        this.nombre_proposition = nombre_proposition;
    }

    public void setPropositions(ArrayList<String> propositions) {
        this.propositions = propositions;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public int getReponse() {
        return reponse;
    }

    public int getNombre_proposition() {
        return nombre_proposition;
    }

    public ArrayList<String> getPropositions() {
        return propositions;
    }
}
