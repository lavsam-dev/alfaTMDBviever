package com.learn.lavsam.alfatmdbviewer.model.repository

import com.learn.lavsam.alfatmdbviewer.model.data.Movie

interface Repository {
    fun getMovieFromWeb() : List<Movie>
}