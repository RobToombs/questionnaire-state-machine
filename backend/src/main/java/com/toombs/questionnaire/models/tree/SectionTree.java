package com.toombs.questionnaire.models.tree;

import java.util.LinkedHashMap;

public class SectionTree {

    private double id = 0.0;
    private String title = "";
    private LinkedHashMap<Double, QuestionTree> questions = new LinkedHashMap<>();

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LinkedHashMap<Double, QuestionTree> getQuestions() {
        return questions;
    }

    public void setQuestions(LinkedHashMap<Double, QuestionTree> questions) {
        this.questions = questions;
    }

    public void addQuestion(QuestionTree question) {
        questions.put(question.getId(), question);
    }
}
