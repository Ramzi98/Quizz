package com.example.quizz.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM Question")
    List<Question> getAllQuestions();
    @Query("SELECT * FROM Question WHERE id = :id")
    Question getQuestionByid(int id);
    @Query("SELECT * FROM Question WHERE question = :question")
    Question getQuestion(String question);
    @Insert(onConflict = REPLACE)
    void insertAll(Question... questions);
    @Insert(onConflict = REPLACE)
    Long insert(Question question);
    @Delete
    void deleteQuestion(Question question);
    @Query("DELETE FROM Question")
    void deleteAll();
}
