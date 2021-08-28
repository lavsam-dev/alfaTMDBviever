package com.learn.lavsam.alfatmdbviewer.model.repository

import com.learn.lavsam.alfatmdbviewer.model.data.Movie
import com.learn.lavsam.alfatmdbviewer.model.data.getMovieList

class RepositoryImpl : Repository {
    override fun getMovieFromWeb(): List<Movie> = getMovieList()
}