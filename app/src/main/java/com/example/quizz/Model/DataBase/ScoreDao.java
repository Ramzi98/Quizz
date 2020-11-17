package com.example.quizz.Model.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

//Interface Pour faire les requetes sql sur la table Score avec Room Database
@Dao
public interface ScoreDao {

    //Selectionner toutes les Scores
    @Query("SELECT * FROM Score")
    List<Score> getAllScores();

    //Selectionner toutes les Scores par score id
    @Query("SELECT * FROM Score WHERE quizz_id = :quizz_id ORDER BY score DESC")
    List<Score> getScoreByQuizzId(int quizz_id);

    //Inserer un Score
    @Insert(onConflict = REPLACE)
    Long insert(Score score);

    //Supprimer un score
    @Delete
    void deleteScore(Score score);

    //Supprimer toutes les scores
    @Query("DELETE FROM Score")
    void deleteAll();

}
