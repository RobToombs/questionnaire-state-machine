package com.toombs.questionnaire.models.tree;

import java.util.LinkedHashMap;

public class QuestionnaireTree {

    private LinkedHashMap<Double, SectionTree> sections = new LinkedHashMap<>();

    public LinkedHashMap<Double, SectionTree> getSections() {
        return sections;
    }

    public void addSection(SectionTree section) {
        sections.put(section.getId(), section);
    }

    public void removeSection(Double id) { sections.remove(id); }
}
