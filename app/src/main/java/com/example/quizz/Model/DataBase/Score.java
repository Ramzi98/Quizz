package com.example.quizz.Model.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

//La table Proposition dans la BDD
@Entity(foreignKeys = @ForeignKey(entity = Quizz.class,
        parentColumns = "id",
        childColumns = "quizz_id",
        onDelete = CASCADE))
public class Score {

    //Declaration des colonnes de la table avec id comme clé primaire et question id comme clé étrangère
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "player_name")
    private String player_name;
    @ColumnInfo(name = "score")
    private int score;
    @ColumnInfo(name = "nombre_question")
    private int nombre_question;
    @ColumnInfo(name = "quizz_id")
    private int quizz_id;

    //Constructeurs et setters et getters
    public Score(String player_name, int score, int nombre_question, int quizz_id) {
        this.player_name = player_name;
        this.score = score;
        this.nombre_question = nombre_question;
        this.quizz_id = quizz_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public int getQuizz_id() {
        return quizz_id;
    }

    public void setQuizz_id(int quizz_id) {
        this.quizz_id = quizz_id;
    }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    public int getNombre_question() {
        return nombre_question;
    }

    public void setNombre_question(int nombre_question) {
        this.nombre_question = nombre_question;
    }
}
