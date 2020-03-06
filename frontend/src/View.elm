module View exposing (..)

import Html exposing (Html, code, div, h5, input, label, li, ol, pre, text)
import Html.Attributes exposing (checked, class, name, type_)
import Html.Events exposing (onClick)
import HttpActions exposing (questionnaireEncoder)
import Json.Encode as Encode
import Model exposing (..)


view : Model -> Html Msg
view model =
    let
        questionnaireView =
            case model of
                Failure ->
                    text "I could not load the next questionnaire for some reason. Make sure the server is up."

                Loading questionnaire ->
                    renderQuestionnaire questionnaire

                Success questionnaire ->
                    renderQuestionnaire questionnaire

        jsonView =
            case model of
                Failure ->
                    div [] []

                Loading questionnaire ->
                    renderJson questionnaire

                Success questionnaire ->
                    renderJson questionnaire
    in
    div [ class "row smaller" ]
        [ div [ class "col-4" ] [ questionnaireView ]
        , div [ class "col-8" ] [ jsonView ]
        ]


renderJson : Questionnaire -> Html msg
renderJson questionnaire =
    let
        json =
            Encode.encode 3 (questionnaireEncoder questionnaire)
    in
    pre [ class "p-2" ] [ code [] [ text json ] ]


renderQuestionnaire : Questionnaire -> Html Msg
renderQuestionnaire questionnaire =
    div []
        (List.map (renderSection questionnaire) questionnaire.sections)


renderSection : Questionnaire -> Section -> Html Msg
renderSection questionnaire section =
    div []
        [ h5 [ class "p-2" ] [ text section.title ]
        , ol [] (List.indexedMap (renderQuestion questionnaire section.id) section.questions)
        ]


renderQuestion : Questionnaire -> Float -> Int -> Question -> Html Msg
renderQuestion questionnaire sectionId _ question =
    let
        styling =
            case question.questionType of
                Prompt ->
                    "font-italic font-weight-light"

                Radio ->
                    ""

        tag =
            case question.questionType of
                Prompt ->
                    div

                Radio ->
                    li

        responses =
            case question.questionType of
                Prompt ->
                    List.map generatePromptResponse question.responses

                Radio ->
                    List.map (generateRadioResponse questionnaire sectionId question.id) question.responses
    in
    tag []
        [ div [ class styling ] [ text question.prompt ]
        , div [] responses
        ]


generateRadioResponse : Questionnaire -> Float -> Float -> Response -> Html Msg
generateRadioResponse questionnaire sectionId questionId response =
    label
        [ class "p-2" ]
        [ text response.display
        , input
            [ type_ "radio"
            , class "ml-2"
            , checked response.selected
            , name (String.fromFloat questionId)
            , onClick (NextQuestionnaire questionnaire sectionId questionId response.id)
            ]
            []
        ]


generatePromptResponse : Response -> Html Msg
generatePromptResponse response =
    div [ class "p-2" ]
        [ text response.display ]
