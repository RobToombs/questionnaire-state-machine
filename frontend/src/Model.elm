module Model exposing (..)

import Http


type Model
    = Failure
    | Loading Questionnaire
    | Success Questionnaire


type QuestionType
    = Prompt
    | Radio


type Msg
    = InitialQuestionnaire
    | NextQuestionnaire Questionnaire Float Float Int
    | ReceivedQuestionnaire (Result Http.Error Questionnaire)


type alias Questionnaire =
    { sections : List Section }


type alias Section =
    { id : Float
    , title : String
    , questions : List Question
    }


type alias Question =
    { id : Float
    , questionType : QuestionType
    , prompt : String
    , responses : List Response
    }


type alias Response =
    { id : Int
    , display : String
    , selected : Bool
    }


defaultQuestionnaire : Questionnaire
defaultQuestionnaire =
    Questionnaire []


defaultSection : Section
defaultSection =
    Section 0.0 "" []


defaultQuestion : Question
defaultQuestion =
    Question 0.0 Prompt "" []


defaultResponse : Response
defaultResponse =
    Response 0 "" False
