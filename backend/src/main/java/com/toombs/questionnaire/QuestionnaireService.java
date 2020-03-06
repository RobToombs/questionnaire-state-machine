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
        template = reader.generateQuestionnaireTemplate(QUESTIONNAIRE_FILE);
    }

    public Questionnaire generateNextQuestionnaire(Questionnaire currentQuestionnaire) {
        QuestionnaireTree qtClone = cloner.deepClone(template);

        if (currentQuestionnaire != null) {
            updateTreeSectionsFromFlatSections(qtClone, currentQuestionnaire.getSections());
        }

        return flattenTreeSections(qtClone);
    }

    private void updateTreeSectionsFromFlatSections(QuestionnaireTree wcClone, ArrayList<Section> sections) {
        wcClone.getSections().forEach((id, treeSection) -> {
            Section section = sections.stream()
                    .filter(s -> s.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (section != null) {
                LinkedHashMap<Double, Question> questionMap = new LinkedHashMap<>();
                for (Question question : section.getQuestions()) {
                    questionMap.put(question.getId(), question);
                }

                updateTreeQuestionsFromFlatQuestions(treeSection.getQuestions(), questionMap);
            }
        });
    }

    private void updateTreeQuestionsFromFlatQuestions(LinkedHashMap<Double, QuestionTree> treeQuestions, LinkedHashMap<Double, Question> questionMap) {
        treeQuestions.forEach((id, treeQuestion) -> {
            if (questionMap.containsKey(id)) {
                LinkedHashMap<Integer, Response> responseMap = new LinkedHashMap<>();
                for (Response response : questionMap.get(id).getResponses()) {
                    responseMap.put(response.getId(), response);
                }

                updateTreeResponsesFromFlatResponses(treeQuestion.getResponses(), responseMap, questionMap);
            }
        });
    }

    private void updateTreeResponsesFromFlatResponses(LinkedHashMap<Integer, ResponseTree> treeResponses, LinkedHashMap<Integer, Response> responseMap, LinkedHashMap<Double, Question> questionMap) {
        treeResponses.forEach((id, treeResponse) -> {
            if(responseMap.containsKey(id)) {
                Response flatResponse = responseMap.get(id);
                treeResponse.setSelected(flatResponse.getSelected());

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
