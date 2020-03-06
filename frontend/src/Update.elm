module Update exposing (..)

import HttpActions exposing (generateInitialQuestionnaire, generateNextQuestionnaire)
import Model exposing (Model(..), Msg(..), Question, QuestionType(..), Questionnaire, Response, Section, defaultQuestion, defaultQuestionnaire, defaultResponse, defaultSection)


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        InitialQuestionnaire ->
            ( Loading defaultQuestionnaire, generateInitialQuestionnaire )

        NextQuestionnaire questionnaire sectionId questionId responseId ->
            let
                currentQuestionnaire =
                    updateQuestionnaireSelection questionnaire sectionId questionId responseId
            in
            ( Loading currentQuestionnaire, generateNextQuestionnaire currentQuestionnaire )

        ReceivedQuestionnaire result ->
            case result of
                Ok questionnaire ->
                    ( Success questionnaire, Cmd.none )

                Err _ ->
                    ( Failure, Cmd.none )


updateQuestionnaireSelection : Questionnaire -> Float -> Float -> Int -> Questionnaire
updateQuestionnaireSelection questionnaire sectionId questionId responseId =
    let
        selectedSection =
            findSectionById questionnaire sectionId

        selectedQuestion =
            findQuestionById selectedSection questionId

        selectedResponse =
            findResponseById selectedQuestion responseId

        updatedQuestion =
            updateQuestionResponses selectedQuestion selectedResponse

        updatedSection =
            replaceQuestionInSection selectedSection updatedQuestion

        updatedQuestionnaire =
            replaceSectionInQuestionnaire questionnaire updatedSection
    in
    updatedQuestionnaire


updateQuestionResponses : Question -> Response -> Question
updateQuestionResponses question response =
    case question.questionType of
        Radio ->
            updateRadioQuestion question response

        Prompt ->
            question


updateRadioQuestion : Question -> Response -> Question
updateRadioQuestion question updatedResponse =
    let
        updatedResponses =
            List.map
                (\response ->
                    if response.id == updatedResponse.id then
                        { updatedResponse | selected = True }

                    else
                        { response | selected = False }
                )
                question.responses
    in
    { question | responses = updatedResponses }


replaceQuestionInSection : Section -> Question -> Section
replaceQuestionInSection section updatedQuestion =
    let
        updatedQuestions =
            List.map
                (\question ->
                    if question.id == updatedQuestion.id then
                        updatedQuestion

                    else
                        question
                )
                section.questions
    in
    { section | questions = updatedQuestions }


replaceSectionInQuestionnaire : Questionnaire -> Section -> Questionnaire
replaceSectionInQuestionnaire questionnaire updatedSection =
    let
        updatedSections =
            List.map
                (\section ->
                    if section.id == updatedSection.id then
                        updatedSection

                    else
                        section
                )
                questionnaire.sections
    in
    { questionnaire | sections = updatedSections }


findSectionById : Questionnaire -> Float -> Section
findSectionById questionnaire sectionId =
    List.filter (\section -> section.id == sectionId) questionnaire.sections
        |> List.head
        |> Maybe.withDefault defaultSection


findQuestionById : Section -> Float -> Question
findQuestionById section questionId =
    List.filter (\question -> question.id == questionId) section.questions
        |> List.head
        |> Maybe.withDefault defaultQuestion


findResponseById : Question -> Int -> Response
findResponseById question responseId =
    List.filter (\response -> response.id == responseId) question.responses
        |> List.head
        |> Maybe.withDefault defaultResponse
