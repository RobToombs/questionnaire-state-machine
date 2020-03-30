package com.toombs.questionnaire;

import com.rits.cloning.Cloner;
import com.toombs.questionnaire.models.flat.Question;
import com.toombs.questionnaire.models.flat.Questionnaire;
import com.toombs.questionnaire.models.flat.Response;
import com.toombs.questionnaire.models.flat.Section;
import com.toombs.questionnaire.models.tree.QuestionTree;
import com.toombs.questionnaire.models.tree.QuestionnaireTree;
import com.toombs.questionnaire.models.tree.ResponseTree;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionnaireService {

    private static final String QUESTIONNAIRE_FILE = "Questionnaire.json";
    private static final Cloner cloner = new Cloner();
    private final QuestionnaireTree template;

    public QuestionnaireService() {
        TemplateReader reader = new TemplateReader();

        // Generate our static in-memory Questionnaire template from the json representation
        template = reader.generateQuestionnaireTemplate(QUESTIONNAIRE_FILE);
    }

    public Questionnaire generateNextQuestionnaire(Questionnaire currentQuestionnaire) {
        // Create a deep copy of our questionnaire template
        QuestionnaireTree templateCopy = cloner.deepClone(template);

        if (currentQuestionnaire != null) {

            // Create a mapping of section id/section for O(1) access time
            LinkedHashMap<Double, Section> sectionMap = new LinkedHashMap<>();
            for (Section section : currentQuestionnaire.getSections()) {
                sectionMap.put(section.getId(), section);
            }

            // Generate the next questionnaire iteration based off our template and the questionnaire we're receiving
            updateTreeSectionsFromFlatSections(templateCopy, sectionMap);
        }

        // Flatten the questionnaire for simplified frontend consumption
        return flattenTreeSections(templateCopy);
    }

    private void updateTreeSectionsFromFlatSections(QuestionnaireTree template,  LinkedHashMap<Double, Section> sectionMap) {
        template.getSections().forEach((id, treeSection) ->
        {
            // Does the incoming questionnaire contain this template section?
            if (sectionMap.containsKey(id)) {

                // Create a mapping of question id/question for O(1) access time
                LinkedHashMap<Double, Question> questionMap = new LinkedHashMap<>();
                for (Question question : sectionMap.get(id).getQuestions()) {
                    questionMap.put(question.getId(), question);
                }

                updateTreeQuestionsFromFlatQuestions(treeSection.getQuestions(), questionMap);
            }
        });
    }

    private void updateTreeQuestionsFromFlatQuestions(LinkedHashMap<Double, QuestionTree> treeQuestions, LinkedHashMap<Double, Question> questionMap) {
        treeQuestions.forEach((id, treeQuestion) ->
        {
            // Does the incoming questionnaire contain this template question?
            if (questionMap.containsKey(id)) {

                // Create a mapping of response id/response for O(1) access time
                LinkedHashMap<Integer, Response> responseMap = new LinkedHashMap<>();
                for (Response response : questionMap.get(id).getResponses()) {
                    responseMap.put(response.getId(), response);
                }

                updateTreeResponsesFromFlatResponses(treeQuestion.getResponses(), responseMap, questionMap);
            }
        });
    }

    private void updateTreeResponsesFromFlatResponses(LinkedHashMap<Integer, ResponseTree> treeResponses, LinkedHashMap<Integer, Response> responseMap, LinkedHashMap<Double, Question> questionMap) {
        treeResponses.forEach((id, treeResponse) ->
        {
            // Does the incoming responses contain this template response?
            if(responseMap.containsKey(id)) {
                Response flatResponse = responseMap.get(id);

                // Update the template response option with the incoming response selection
                treeResponse.setSelected(flatResponse.getSelected());

                // Update the child questions if selected
                if(treeResponse.getSelected()) {
                    updateTreeQuestionsFromFlatQuestions(treeResponse.getChildren(), questionMap);
                }
            }
        });
    }

    private Questionnaire flattenTreeSections(QuestionnaireTree treeQuestionnaire) {
        Questionnaire result = new Questionnaire();

        if(treeQuestionnaire != null) {
            ArrayList<Section> sections = new ArrayList<>();
            treeQuestionnaire.getSections()
                    .values()
                    .forEach(treeSection -> {
                        Section section = new Section();
                        section.setId(treeSection.getId());
                        section.setTitle(treeSection.getTitle());

                        ArrayList<Question> questions = new ArrayList<>();
                        flattenTreeQuestions(questions, treeSection.getQuestions().values(), new HashSet<>());
                        section.setQuestions(questions);

                        sections.add(section);
                    });

            result.setSections(sections);
        }

        return result;
    }

    private void flattenTreeQuestions(ArrayList<Question> result, Collection<QuestionTree> questions, HashSet<Double> previouslyAddedQuestionIds) {
        questions.forEach(treeQuestion -> {
            if (!previouslyAddedQuestionIds.contains(treeQuestion.getId())) {
                Question question = new Question();
                question.setId(treeQuestion.getId());
                question.setQuestionType(treeQuestion.getType());
                question.setPrompt(treeQuestion.getPrompt());
                question.setResponses(flattenTreeResponses(treeQuestion));

                result.add(question);
                previouslyAddedQuestionIds.add(treeQuestion.getId());

                treeQuestion.getResponses()
                        .values()
                        .stream()
                        .filter(ResponseTree::getSelected)
                        .forEachOrdered(treeResponse -> {
                                Collection<QuestionTree> workableQuestions = treeResponse.getChildren().values();
                                flattenTreeQuestions(result, workableQuestions, previouslyAddedQuestionIds);
                    });
            }
        });
    }

    private ArrayList<Response> flattenTreeResponses(QuestionTree treeQuestion) {
        ArrayList<Response> result = new ArrayList<>();

        treeQuestion.getResponses()
                .values()
                .forEach(wro -> {
            Response response = new Response();
            response.setId(wro.getId());
            response.setDisplay(wro.getDisplay());
            response.setSelected(wro.getSelected());
            result.add(response);
        });

        return result;
    }

}
