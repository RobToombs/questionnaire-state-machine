module Main exposing (..)

import Browser
import HttpActions exposing (generateInitialQuestionnaire)
import Model exposing (Model(..), Msg, defaultQuestionnaire)
import Update exposing (update)
import View exposing (view)


main =
    Browser.element
        { init = init
        , update = update
        , subscriptions = subscriptions
        , view = view
        }


init : () -> ( Model, Cmd Msg )
init _ =
    ( Loading defaultQuestionnaire, generateInitialQuestionnaire )


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none
