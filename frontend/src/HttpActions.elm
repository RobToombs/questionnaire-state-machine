module HttpActions exposing (generateInitialQuestionnaire, generateNextQuestionnaire, questionnaireEncoder)

import Http exposing (Body, Expect, Header)
import Json.Decode as Decode
import Json.Decode.Pipeline exposing (required)
import Json.Encode as Encode
import Model exposing (Msg(..), Question, QuestionType(..), Questionnaire, Response, Section)


generateInitialQuestionnaire : Cmd Msg
generateInitialQuestionnaire =
    let
        url_ =
            "/questionnaire/initial"

        expect_ =
            Http.expectJson ReceivedQuestionnaire questionnaireDecoder
    in
    Http.get { url = url_, expect = expect_ }


generateNextQuestionnaire : Questionnaire -> Cmd Msg
generateNextQuestionnaire questionnaire =
    let
        url_ =
            "/questionnaire/next"

        body_ =
            Http.jsonBody (questionnaireEncoder questionnaire)

        expect_ =
            Http.expectJson ReceivedQuestionnaire questionnaireDecoder
    in
    Http.post { url = url_, body = body_, expect = expect_ }



-- ENCODERS


questionnaireEncoder : Questionnaire -> Encode.Value
questionnaireEncoder questionnaire =
    Encode.object
        [ ( "sections", Encode.list sectionEncoder questionnaire.sections )
        ]


sectionEncoder : Section -> Encode.Value
sectionEncoder section =
    Encode.object
        [ ( "id", Encode.float section.id )
        , ( "title", Encode.string section.title )
        , ( "questions", Encode.list questionEncoder section.questions )
        ]


questionEncoder : Question -> Encode.Value
questionEncoder question =
    Encode.object
        [ ( "id", Encode.float question.id )
        , ( "questionType", fieldTypeEncoder question.questionType )
        , ( "prompt", Encode.string question.prompt )
        , ( "responses", Encode.list responseEncoder question.responses )
        ]


responseEncoder : Response -> Encode.Value
responseEncoder response =
    Encode.object
        [ ( "id", Encode.int response.id )
        , ( "display", Encode.string response.display )
        , ( "selected", Encode.bool response.selected )
        ]


fieldTypeEncoder : QuestionType -> Encode.Value
fieldTypeEncoder questionType =
    let
        value =
            case questionType of
                Radio ->
                    "RADIO"

                Prompt ->
                    "PROMPT"
    in
    Encode.string value



-- DECODERS


questionnaireDecoder : Decode.Decoder Questionnaire
questionnaireDecoder =
    Decode.succeed Questionnaire
        |> required "sections" (Decode.list sectionDecoder)


sectionDecoder : Decode.Decoder Section
sectionDecoder =
    Decode.succeed Section
        |> required "id" Decode.float
        |> required "title" Decode.string
        |> required "questions" (Decode.list questionDecoder)


questionDecoder : Decode.Decoder Question
questionDecoder =
    Decode.succeed Question
        |> required "id" Decode.float
        |> required "questionType" fieldTypeDecoder
        |> required "prompt" Decode.string
        |> required "responses" (Decode.list responseOptionsDecoder)


fieldTypeDecoder : Decode.Decoder QuestionType
fieldTypeDecoder =
    Decode.string
        |> Decode.andThen
            (\value ->
                case value of
                    "RADIO" ->
                        Decode.succeed Radio

                    "PROMPT" ->
                        Decode.succeed Prompt

                    _ ->
                        Decode.succeed Prompt
            )


responseOptionsDecoder : Decode.Decoder Response
responseOptionsDecoder =
    Decode.succeed Response
        |> required "id" Decode.int
        |> required "display" Decode.string
        |> required "selected" Decode.bool
