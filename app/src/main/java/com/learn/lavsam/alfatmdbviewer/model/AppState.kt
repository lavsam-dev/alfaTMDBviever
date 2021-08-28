package com.learn.lavsam.alfatmdbviewer.model

import com.learn.lavsam.alfatmdbviewer.model.data.Actor
import com.learn.lavsam.alfatmdbviewer.model.data.Movie

sealed class AppState {
    data class Success(val movieData: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}

sealed class AppStateActor {
    data class Success(val actor: List<Actor>) : AppStateActor()
    data class Error(val error: Throwable) : AppStateActor()
    object Loading : AppStateActor()
}
