package com.learn.lavsam.alfatmdbviewer.model.repository

import com.learn.lavsam.alfatmdbviewer.model.data.Actor

interface ActorsRepository {
    fun getActorsFromWeb() : List<Actor>
}