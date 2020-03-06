package com.toombs.questionnaire.models.flat;

import java.util.ArrayList;

public class Questionnaire {

    private ArrayList<Section> sections = new ArrayList<>();

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }
}
