package com.toombs.questionnaire.models.flat;

import com.toombs.questionnaire.models.QuestionType;

import java.util.ArrayList;

public class Question {

    private double id = 0.0;
    private QuestionType questionType = QuestionType.PROMPT;
    private String prompt = "";
    private ArrayList<Response> responses = new ArrayList<>();

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public ArrayList<Response> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<Response> responses) {
        this.responses = responses;
    }
}
