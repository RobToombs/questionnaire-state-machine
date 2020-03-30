package com.toombs.questionnaire;

import com.toombs.questionnaire.models.QuestionType;
import com.toombs.questionnaire.models.tree.QuestionTree;
import com.toombs.questionnaire.models.tree.QuestionnaireTree;
import com.toombs.questionnaire.models.tree.ResponseTree;
import com.toombs.questionnaire.models.tree.SectionTree;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

public class TemplateReader {

    private static final String ID = "id";
    private static final String QUESTIONS = "questions";
    private static final String TYPE = "type";
    private static final String PROMPT = "prompt";
    private static final String RESPONSES = "responses";
    private static final String DISPLAY = "display";
    private static final String CHILDREN = "children";
    private static final String SECTIONS = "sections";
    private static final String TITLE = "title";

    public QuestionnaireTree generateQuestionnaireTemplate(String fileLocation) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Path path = Paths.get(Objects.requireNonNull(classLoader.getResource(fileLocation)).toURI());
            String questionnaireTemplate = new String(Files.readAllBytes(path));
            return convertToQuestionnaire(questionnaireTemplate);
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return null;
    }

    private QuestionnaireTree convertToQuestionnaire(String templateJSON) {
        QuestionnaireTree questionnaire = new QuestionnaireTree();

        try {
            Object parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(templateJSON);
            JSONObject obj = (JSONObject) parser;
            HashMap<Double, QuestionTree> questionPool = buildQuestionPool(obj);
            buildSections(questionnaire, obj, questionPool);
        }
        catch(Exception e) {
            System.err.println(e.toString());
        }

        return questionnaire;
    }

    private HashMap<Double, QuestionTree> buildQuestionPool(JSONObject obj) {
        HashMap<Double, QuestionTree> questionPool = new HashMap<>();

        JSONArray questionListJSON = (JSONArray) obj.get(QUESTIONS);
        for (int i = questionListJSON.size() - 1; i >= 0; i--) {
            JSONObject questionJSON = (JSONObject) questionListJSON.get(i);

            QuestionTree question = new QuestionTree();

            question.setId((Double) questionJSON.get(ID));
            question.setType(QuestionType.valueOf(String.valueOf(questionJSON.get(TYPE))));
            question.setPrompt(String.valueOf(questionJSON.get(PROMPT)));

            setQuestionResponseOptions(question, questionPool, questionJSON);

            questionPool.put(question.getId(), question);
        }

        return questionPool;
    }

    private static void setQuestionResponseOptions(QuestionTree question, HashMap<Double, QuestionTree> questionPool, JSONObject questionJSON) {
        if(questionJSON.containsKey(RESPONSES)) {
            JSONArray responseListJSON = (JSONArray) questionJSON.get(RESPONSES);

            for (Object o : responseListJSON) {
                JSONObject responseJSON = (JSONObject) o;

                ResponseTree responseOption = new ResponseTree();
                responseOption.setId((Integer) responseJSON.get(ID));
                responseOption.setDisplay(String.valueOf(responseJSON.get(DISPLAY)));

                if(responseJSON.containsKey(CHILDREN)) {
                    JSONArray childIds = (JSONArray) responseJSON.get(CHILDREN);
                    childIds.forEach(id -> {
                        if (questionPool.containsKey(id)) {
                            responseOption.addChild(questionPool.get(id));
                        }
                    });
                }

                question.addResponse(responseOption);
            }
        }
    }

    private static void buildSections(QuestionnaireTree questionnaire, JSONObject obj, HashMap<Double, QuestionTree> questionPool) {
        JSONArray sectionsJSON = (JSONArray) obj.get(SECTIONS);
        for (Object o : sectionsJSON) {
            JSONObject sectionJSON = (JSONObject) o;
            SectionTree section = new SectionTree();
            section.setId((Double) sectionJSON.get(ID));
            section.setTitle(String.valueOf(sectionJSON.get(TITLE)));

            JSONArray questionIds = (JSONArray) sectionJSON.get(QUESTIONS);
            questionIds.forEach(id -> {
                double idDouble = (Double) id;
                if (idDouble > 0) {
                    section.addQuestion(questionPool.get(idDouble));
                }
            });

            questionnaire.addSection(section);
        }
    }
}
