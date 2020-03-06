package com.toombs.questionnaire;

import com.toombs.questionnaire.models.flat.Questionnaire;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @GetMapping("/initial")
    public Questionnaire getInitialQuestionnaire() {
        return questionnaireService.generateNextQuestionnaire(null);
    }

    @PostMapping("/next")
    public Questionnaire getNextQuestionnaire(@RequestBody Questionnaire currentQuestionnaire) {
        return questionnaireService.generateNextQuestionnaire(currentQuestionnaire);
    }

}

