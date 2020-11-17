package com.example.quizz.Model.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

//La base de données de l'application
@Database(entities = {Quizz.class,Question.class,Proposition.class,Score.class},version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuizzDao quizzDAO();
    public abstract QuestionDao questionDao();
    public abstract PropositionDao propositionDao();
    public abstract ScoreDao scoreDao();
}
