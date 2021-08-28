package com.learn.lavsam.alfatmdbviewer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learn.lavsam.alfatmdbviewer.model.AppState
import com.learn.lavsam.alfatmdbviewer.model.repository.Repository
import com.learn.lavsam.alfatmdbviewer.model.repository.RepositoryImpl

class MainViewModel(private val repository: Repository = RepositoryImpl()) : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun getData(): LiveData<AppState> = liveDataToObserve

    fun getMovieFromWebSource() = getDataFromWebSource()

    private fun getDataFromWebSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            liveDataToObserve.postValue(AppState.Success(repository.getMovieFromWeb()))
        }.start()
    }
}