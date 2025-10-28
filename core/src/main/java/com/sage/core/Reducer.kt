package com.sage.core

interface Reducer<State : Reducer.UiState, Event : Reducer.UiEvent, Effect : Reducer.UiEffect> {

    interface UiState

    interface UiEvent

    interface UiEffect

    fun reduce(previous: State, event: Event): Pair<State, Effect?>
}