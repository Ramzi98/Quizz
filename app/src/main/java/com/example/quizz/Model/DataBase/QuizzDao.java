package com.example.quizz.Model.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

//Interface Pour faire les requetes sql sur la table Quizz avec Room Database
@Dao
public interface QuizzDao {

    //Selectionner toutes les Quizz
    @Query("SELECT * FROM Quizz")
    List<Quizz> getAllQuizzs();

    //Selectionner un Quizz par id
    @Query("SELECT * FROM Quizz WHERE id = :id")
    Quizz getQuizzById(int id);

    //Selectionner un Quizz par type
    @Query("SELECT * FROM Quizz WHERE type = :type")
    Quizz getQuizzByType(String type);

    //Inserer des Quizz
    @Insert(onConflict = REPLACE)
    void insertAll(Quizz... quizzs);

    //Inserer un Quizz
    @Insert(onConflict = REPLACE)
    Long insert(Quizz quizz);

    //Supprimer un Quizz
    @Delete
    void deleteQuizz(Quizz quizz);

    //Supprimer toutes les Quizz
    @Query("DELETE FROM Quizz")
    void deleteAll();

    //Modifier un Quizz
    @Query("UPDATE Quizz SET type = :type WHERE id = :id")
    void updateQuizz(String type,int id);

}
