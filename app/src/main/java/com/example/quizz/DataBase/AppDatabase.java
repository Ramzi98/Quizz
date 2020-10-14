package com.example.quizz.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Quizz.class,Question.class,Proposition.class,Score.class},version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuizzDao quizzDAO();
    public abstract QuestionDao questionDao();
    public abstract PropositionDao propositionDao();
    public abstract ScoreDao scoreDao();
}
