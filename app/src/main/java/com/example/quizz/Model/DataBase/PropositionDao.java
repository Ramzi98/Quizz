package com.example.quizz.Model.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

//Interface Pour faire les requetes sql sur la table Proposition avec Room Database
@Dao
public interface PropositionDao {
    //Selectionner toutes les propositions
    @Query("SELECT * FROM Proposition")
    List<Proposition> getAllPropositions();

    //Selectionner une proposition avec l'id
    @Query("SELECT * FROM Proposition WHERE id = :id")
    Proposition getPropositionByid(int id);

    //Selectionner toutes les propositions avec l'id de question
    @Query("SELECT * FROM Proposition WHERE question_id = :question_id")
    Proposition getPropositionByQuestionid(int question_id);

    //Inserer des propositions
    @Insert(onConflict = REPLACE)
    void insertAll(Proposition... propositions);

    //Inserer une proposition
    @Insert(onConflict = REPLACE)
    void insert(Proposition proposition);

    //Supprimer une proposition
    @Delete
    void deleteQuizz(Proposition proposition);

    //Supprimer toutes les propositions
    @Query("DELETE FROM Proposition")
    void deleteAll();

    //Modifier une proposition
    @Update
    void updateProposition(Proposition proposition);
}
