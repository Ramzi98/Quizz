package com.example.quizz.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PropositionDao {
    @Query("SELECT * FROM Proposition")
    List<Proposition> getAllPropositions();
    @Query("SELECT * FROM Proposition WHERE id = :id")
    Proposition getPropositionByid(int id);
    @Query("SELECT * FROM Proposition WHERE question_id = :question_id")
    Proposition getPropositionByQuestionid(int question_id);
    @Insert(onConflict = REPLACE)
    void insertAll(Proposition... propositions);
    @Insert(onConflict = REPLACE)
    void insert(Proposition proposition);
    @Delete
    void deleteQuizz(Proposition proposition);
    @Query("DELETE FROM Proposition")
    void deleteAll();
}
