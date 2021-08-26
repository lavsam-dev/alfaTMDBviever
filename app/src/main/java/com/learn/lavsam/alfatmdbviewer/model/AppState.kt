package com.learn.lavsam.alfatmdbviewer.model

import com.learn.lavsam.alfatmdbviewer.model.data.Movie

sealed class AppState {
    data class Success(val movieData: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
