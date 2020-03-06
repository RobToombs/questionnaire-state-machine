package com.toombs.questionnaire.models.flat;

import java.util.ArrayList;

public class Section {

    private double id = 0.0;
    private String title = "";
    private ArrayList<Question> questions = new ArrayList<>();

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

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
