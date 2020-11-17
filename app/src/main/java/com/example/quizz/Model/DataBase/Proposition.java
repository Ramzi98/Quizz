package com.example.quizz.Model.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import static androidx.room.ForeignKey.CASCADE;

//La table Proposition dans la BDD
@Entity(foreignKeys = @ForeignKey(entity = Question.class,
        parentColumns = "id",
        childColumns = "question_id",
        onDelete = CASCADE))
public class Proposition {
    //Declaration des colonnes de la table avec id comme clé primaire et question id comme clé étrangère
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "propositions")
    private ArrayList<String> propositions;
    @ColumnInfo(name = "question_id")
    private int question_id;

    //Constructeurs et setters et getters
    public Proposition(ArrayList<String> propositions, int question_id) {
        this.propositions = propositions;
        this.question_id = question_id;
    }

    @Ignore
    public Proposition(ArrayList<String> propositions, int question_id,int id) {
        this.id = id;
        this.propositions = propositions;
        this.question_id = question_id;
    }

    @Ignore
    public Proposition(ArrayList<String> propositions) {
        this.propositions = propositions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getPropositions() {
        return propositions;
    }

    public void setPropositions(ArrayList<String> propositions) {
        this.propositions = propositions;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}
