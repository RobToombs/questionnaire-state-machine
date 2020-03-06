package com.toombs.questionnaire.models.tree;

import java.util.LinkedHashMap;

public class ResponseTree {

    private int id = -1;
    private String display = "";
    private boolean selected = false;
    private LinkedHashMap<Double, QuestionTree> children = new LinkedHashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public LinkedHashMap<Double, QuestionTree> getChildren() {
        return children;
    }

    public void setChildren(LinkedHashMap<Double, QuestionTree> children) {
        this.children = children;
    }

    public void addChild(QuestionTree question) {
        children.put(question.getId(), question);
    }
}
