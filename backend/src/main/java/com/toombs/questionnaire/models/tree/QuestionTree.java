package com.toombs.questionnaire.models.tree;

import com.toombs.questionnaire.models.QuestionType;

import java.util.LinkedHashMap;

public class QuestionTree {

    private double id = 0.0;
    private QuestionType type = QuestionType.PROMPT;
    private String prompt = "";
    private LinkedHashMap<Integer, ResponseTree> responses = new LinkedHashMap<>();

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public LinkedHashMap<Integer, ResponseTree> getResponses() {
        return responses;
    }

    public void setResponses(LinkedHashMap<Integer, ResponseTree> responses) {
        this.responses = responses;
    }

    public void addResponse(ResponseTree response) {
        this.responses.put(response.getId(), response);
    }
}
