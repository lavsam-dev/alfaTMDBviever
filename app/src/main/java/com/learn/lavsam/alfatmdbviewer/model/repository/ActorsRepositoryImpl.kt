package com.learn.lavsam.alfatmdbviewer.model.repository

import com.learn.lavsam.alfatmdbviewer.model.data.Actor
import com.learn.lavsam.alfatmdbviewer.model.data.getActorsList

class ActorsRepositoryImpl : ActorsRepository {
    override fun getActorsFromWeb(): List<Actor> = getActorsList()
}