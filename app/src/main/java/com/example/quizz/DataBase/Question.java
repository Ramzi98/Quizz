package com.example.quizz.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Quizz.class,
        parentColumns = "id",
        childColumns = "quizz_id",
        onDelete = CASCADE))
public class Question {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "question")
    private String question;
    @ColumnInfo(name = "reponse")
    private int reponse;
    @ColumnInfo(name = "nombre_proposition")
    private int nombre_proposition;
    @ColumnInfo(name = "quizz_id")
    private int quizz_id;


    public Question(String question, int reponse, int nombre_proposition, int quizz_id) {
        this.question = question;
        this.reponse = reponse;
        this.nombre_proposition = nombre_proposition;
        this.quizz_id = quizz_id;
    }
    @Ignore
    public Question(String question, int reponse, int nombre_proposition) {
        this.question = question;
        this.reponse = reponse;
        this.nombre_proposition = nombre_proposition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getReponse() {
        return reponse;
    }

    public void setReponse(int reponse) {
        this.reponse = reponse;
    }

    public int getNombre_proposition() {
        return nombre_proposition;
    }

    public void setNombre_proposition(int nombre_proposition) {
        this.nombre_proposition = nombre_proposition;
    }

    public int getQuizz_id() {
        return quizz_id;
    }

    public void setQuizz_id(int quizz_id) {
        this.quizz_id = quizz_id;
    }
}
