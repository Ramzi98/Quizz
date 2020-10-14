package com.example.quizz.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Quizz.class,
        parentColumns = "id",
        childColumns = "quizz_id",
        onDelete = CASCADE))
public class Score {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "player_name")
    private String player_name;
    @ColumnInfo(name = "quizz_id")
    private int quizz_id;

    public Score(String player_name, int quizz_id) {
        this.player_name = player_name;
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
}
