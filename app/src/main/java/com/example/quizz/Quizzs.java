package com.example.quizz;

import com.example.quizz.DataBase.Proposition;
import com.example.quizz.DataBase.Question;
import com.example.quizz.DataBase.Quizz;

public class Quizzs {
    private Quizz quizz;
    private Question question;
    private Proposition propositions;

    public Quizzs(Quizz quizz, Question question, Proposition propositions) {
        this.quizz = quizz;
        this.question = question;
        this.propositions = propositions;
    }

    public Quizz getQuizz() {
        return quizz;
    }

    public void setQuizz(Quizz quizz) {
        this.quizz = quizz;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Proposition getPropositions() {
        return propositions;
    }

    public void setPropositions(Proposition propositions) {
        this.propositions = propositions;
    }
}
