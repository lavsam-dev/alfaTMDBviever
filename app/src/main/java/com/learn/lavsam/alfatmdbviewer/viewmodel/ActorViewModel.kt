package com.learn.lavsam.alfatmdbviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learn.lavsam.alfatmdbviewer.model.AppStateActor
import com.learn.lavsam.alfatmdbviewer.model.repository.ActorsRepository
import com.learn.lavsam.alfatmdbviewer.model.repository.ActorsRepositoryImpl

class ActorViewModel(private val repository: ActorsRepository = ActorsRepositoryImpl()) :
    ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppStateActor> = MutableLiveData()

    fun getData(): LiveData<AppStateActor> = liveDataToObserve

    fun getActorFromWebSource() = getDataFromWebSource()

    private fun getDataFromWebSource() {
        liveDataToObserve.value = AppStateActor.Loading
        Thread {
            liveDataToObserve.postValue(AppStateActor.Success(repository.getActorsFromWeb()))
        }.start()
    }
}