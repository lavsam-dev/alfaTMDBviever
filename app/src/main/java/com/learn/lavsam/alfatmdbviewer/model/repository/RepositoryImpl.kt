package com.learn.lavsam.alfatmdbviewer.model.repository

import com.learn.lavsam.alfatmdbviewer.model.data.Movie

class RepositoryImpl : Repository {
    override fun getMovieFromWeb() = Movie()
    }
}