package com.learn.lavsam.alfatmdbviewer.service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.learn.lavsam.alfatmdbviewer.BuildConfig
import com.learn.lavsam.alfatmdbviewer.R
import com.learn.lavsam.alfatmdbviewer.model.data.MovieDTO
import com.learn.lavsam.alfatmdbviewer.view.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000
const val ID_MOVIE = "ID"
private const val API_KEY = BuildConfig.TMDB_API_KEY
private const val LANGUAGE = BuildConfig.LANGUAGE_CONST
private const val DEFAULT_VALUE = 0
private const val TMDB_URL_MOVIE_CONST = BuildConfig.TMDB_URL_MOVIE_CONST

class DetailsService(name: String = "DetailService") : IntentService(name) {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val id = intent.getIntExtra(ID_MOVIE, DEFAULT_VALUE)
            if (id == 0) {
                onEmptyData()
            } else {
                loadMovie(id)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadMovie(id: Int) {
        try {
            val uri =
                URL("$TMDB_URL_MOVIE_CONST${id}?api_key=$API_KEY&language=$LANGUAGE")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.apply {
                    urlConnection.requestMethod = REQUEST_GET
                    urlConnection.readTimeout = REQUEST_TIMEOUT
                }
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val movieDTO: MovieDTO =
                    Gson().fromJson(getLines(bufferedReader), MovieDTO::class.java)
                onResponse(movieDTO)
            } catch (e: Exception) {
                onErrorRequest(e.message ?: getString(R.string.tmdb_empty_error))
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onResponse(movieDTO: MovieDTO) {
        if (movieDTO == null) {
            onEmptyResponse()
        } else {
            onSuccessResponse(
                movieDTO.id, movieDTO.title, movieDTO.poster_path, movieDTO.release_date,
                movieDTO.vote_average, movieDTO.overview, movieDTO.backdrop_path, movieDTO.genre,
                movieDTO.runtime
            )
        }
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }


    private fun onSuccessResponse(
        id: Int?, title: String?, poster_path: String?, release_date: String?, vote_average: Double?,
        overview: String?, backdrop_path: String?, genre: String?, runtime: Int?,
    ) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_ID_MOVIE, id)
        broadcastIntent.putExtra(DETAILS_TITLE, title)
        broadcastIntent.putExtra(DETAILS_POSTER_PATH, poster_path)
        broadcastIntent.putExtra(DETAILS_RELEASE_DATE, release_date)
        broadcastIntent.putExtra(DETAILS_VOTE_AVERAGE, vote_average)
        broadcastIntent.putExtra(DETAILS_OVERVIEW, overview)
        broadcastIntent.putExtra(DETAILS_BACKDROP_PATH, backdrop_path)
        broadcastIntent.putExtra(DETAILS_GENRE, genre)
        broadcastIntent.putExtra(DETAILS_RUNTIME, runtime)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }
}