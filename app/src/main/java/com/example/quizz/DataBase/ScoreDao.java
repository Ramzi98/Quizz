package com.example.quizz.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ScoreDao {
    @Query("SELECT * FROM Score")
    List<Score> getAllScores();
    @Query("SELECT * FROM Score WHERE quizz_id = :quizz_id ORDER BY score DESC")
    List<Score> getScoreByQuizzId(int quizz_id);
    @Insert(onConflict = REPLACE)
    Long insert(Score score);
    @Delete
    void deleteScore(Score score);

}
