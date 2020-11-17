package com.example.quizz.Model.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

//Interface Pour faire les requetes sql sur la table Question avec Room Database
@Dao
public interface QuestionDao {

    //Selectionner toutes les questions
    @Query("SELECT * FROM Question")
    List<Question> getAllQuestions();

    //Selectionner une question avec l'id
    @Query("SELECT * FROM Question WHERE id = :id")
    Question getQuestionByid(int id);

    //Selectionner une question avec l'id_quizz
    @Query("SELECT * FROM Question WHERE quizz_id = :quizz_id")
    List<Question> getQuestionByQuizzid(int quizz_id);

    //Selectionner une question avec la question
    @Query("SELECT * FROM Question WHERE question = :question")
    Question getQuestion(String question);

    //Inserer des questions
    @Insert(onConflict = REPLACE)
    void insertAll(Question... questions);

    //Inserer une question
    @Insert(onConflict = REPLACE)
    Long insert(Question question);

    //Supprimer une question
    @Delete
    void deleteQuestion(Question question);

    //Supprimer toutes les questions
    @Query("DELETE FROM Question")
    void deleteAll();

    //Modifier une question
    @Query("UPDATE Question SET question = :question , reponse = :reponse , nombre_proposition = :nombre_proposition WHERE id = :id")
    void updateQuestion(String question,int reponse,int nombre_proposition,int id);
}
