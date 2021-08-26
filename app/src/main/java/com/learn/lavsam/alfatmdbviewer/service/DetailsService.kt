package com.learn.lavsam.alfatmdbviewer.service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.learn.lavsam.alfatmdbviewer.model.data.MovieDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000
const val ID_MOVIE = "ID"
private const val API_KEY = "3d4eed70b3bf0c001506c22b79833ff1"
private const val LANGUAGE = "en-US"
private const val DEFAULT_VALUE = 0

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
            val uri = URL("https://api.themoviedb.org/3/movie/${id}?api_key=$API_KEY&language=$LANGUAGE")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.apply {
                    urlConnection.requestMethod = REQUEST_GET
                    urlConnection.readTimeout = REQUEST_TIMEOUT
                }
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val movieDTO: MovieDTO = Gson().fromJson(getLines(bufferedReader), MovieDTO::class.java)
                onResponse(movieDTO)
            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
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
            onSuccessResponse(movieDTO.id, movieDTO.original_title, movieDTO.overview, movieDTO.poster_path, movieDTO.backdrop_path,
                movieDTO.release_date, movieDTO.title, movieDTO.vote_average, movieDTO.runtime)
        }
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }


    private fun onSuccessResponse(
        id: Int?, original_title: String?, overview: String?, poster_path: String?,
        backdrop_path: String?, release_date: String?, title: String?,
        vote_average: Double?, runtime: Int?,
    ) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_ID_MOVIE, id)
        broadcastIntent.putExtra(DETAILS_ORIGINAL_TITLE, original_title)
        broadcastIntent.putExtra(DETAILS_TITLE, title)
        broadcastIntent.putExtra(DETAILS_OVERVIEW, overview)
        broadcastIntent.putExtra(DETAILS_RELEASE_DATE, release_date)
        broadcastIntent.putExtra(DETAILS_VOTE_AVERAGE, vote_average)
        broadcastIntent.putExtra(DETAILS_RUNTIME, runtime)
        broadcastIntent.putExtra(DETAILS_POSTER_PATH, poster_path)
        broadcastIntent.putExtra(DETAILS_BACKDROP_PATH, backdrop_path)
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