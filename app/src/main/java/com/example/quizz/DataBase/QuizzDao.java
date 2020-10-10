package com.example.quizz.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface QuizzDao {
    @Query("SELECT * FROM Quizz")
    List<Quizz> getAllQuizzs();
    @Insert(onConflict = REPLACE)
    void insertAll(Quizz... quizzs);
    @Insert(onConflict = REPLACE)
    void insert(Quizz quizz);
    @Delete
    void deleteQuizz(Quizz quizz);
    @Query("DELETE FROM Quizz")
    void deleteAll();

}
