package com.learn.lavsam.alfatmdbviewer.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.learn.lavsam.alfatmdbviewer.BuildConfig.BASE_URL_CONST
import com.learn.lavsam.alfatmdbviewer.BuildConfig.IMAGE_SIZE_CONST
import com.learn.lavsam.alfatmdbviewer.databinding.FragmentDetailedMovieBinding
import com.learn.lavsam.alfatmdbviewer.model.data.Movie
import com.learn.lavsam.alfatmdbviewer.model.data.MovieDTO
import com.learn.lavsam.alfatmdbviewer.service.DetailsService
import com.learn.lavsam.alfatmdbviewer.service.ID_MOVIE
import com.squareup.picasso.BuildConfig
import com.squareup.picasso.Picasso

const val DETAILS_INTENT_FILTER = "DETAILS_INTENT_FILTER"
const val DETAILS_INTENT_EMPTY_EXTRA = "DETAILS_INTENT_EMPTY_EXTRA"
const val DETAILS_LOAD_RESULT_EXTRA = "DETAILS_LOAD_RESULT_EXTRA"
const val DETAILS_DATA_EMPTY_EXTRA = "DETAILS_DATA_EMPTY_EXTRA"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "DETAILS_RESPONSE_SUCCESS_EXTRA"
const val DETAILS_TITLE = "DETAILS_TITLE"
const val DETAILS_OVERVIEW = "DETAILS_OVERVIEW"
const val DETAILS_RELEASE_DATE = "DETAILS_RELEASE_DATE"
const val DETAILS_VOTE_AVERAGE = "DETAILS_VOTE_AVERAGE"
const val DETAILS_RUNTIME = "DETAILS_RUNTIME"
const val DETAILS_GENRE = "DETAILS_GENRE"
const val DETAILS_POSTER_PATH = "DETAILS_POSTER_PATH"
const val DETAILS_BACKDROP_PATH = "DETAILS_BACKDROP_PATH"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "DETAILS_RESPONSE_EMPTY_EXTRA"
const val DETAILS_REQUEST_ERROR_EXTRA = "DETAILS_REQUEST_ERROR_EXTRA"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "DETAILS_REQUEST_ERROR_MESSAGE_EXTRA"
const val DETAILS_ID_MOVIE = "DETAILS_ID_MOVIE"
private const val PROCESS_ERROR = "Обработка ошибки"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
private const val INVALID_PROPERTY = 0
private const val INVALID_PROPERTY_STRING = "null"
const val DEFAULT_VALUE = 0
const val DEFAULT_DOUBLE_VALUE = 0.0

private const val IMAGE_SIZE = "w500"
private const val BASE_URL = "https://image.tmdb.org/t/p/"

class DetailMovieFragment : Fragment() {
    private lateinit var binding: FragmentDetailedMovieBinding
    private lateinit var movieBundle: Movie

    companion object {
        const val BUNDLE_EXTRA = "movie"

        fun newInstance(bundle: Bundle): DetailMovieFragment {
            val fragment = DetailMovieFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
                    MovieDTO(
                        intent.getIntExtra(ID_MOVIE, INVALID_PROPERTY),
                        intent.getStringExtra(DETAILS_TITLE),
                        intent.getStringExtra(DETAILS_POSTER_PATH),
                        intent.getStringExtra(DETAILS_RELEASE_DATE),
                        intent.getDoubleExtra(DETAILS_VOTE_AVERAGE, DEFAULT_DOUBLE_VALUE),
                        intent.getStringExtra(DETAILS_OVERVIEW),
                        intent.getStringExtra(DETAILS_BACKDROP_PATH),
                        intent.getStringExtra(DETAILS_GENRE),
                        intent.getIntExtra(DETAILS_RUNTIME, DEFAULT_VALUE)
                    )
                )
                else -> TODO(PROCESS_ERROR)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDetailedMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieBundle = arguments?.getParcelable<Movie>(BUNDLE_EXTRA) ?: Movie()
        getMovie()
    }

    private fun getMovie() {
        with(binding) {
            detailedMovieView.visibility = View.GONE
            detailedLoadingLayout.visibility = View.VISIBLE
        }
        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(
                    ID_MOVIE, movieBundle.id
                )
            })
        }
    }

    private fun renderData(movieDTO: MovieDTO) = with(binding) {
        detailedMovieView.visibility = View.VISIBLE
        detailedLoadingLayout.visibility = View.GONE

        val title = movieDTO.title
        val overview = movieDTO.overview
        val releaseDate = movieDTO.release_date
        val runtime = movieDTO.runtime
        val voteAverage = movieDTO.vote_average.toString()
        if (title == INVALID_PROPERTY_STRING || overview == INVALID_PROPERTY_STRING ||
            releaseDate == INVALID_PROPERTY_STRING || runtime == INVALID_PROPERTY || voteAverage == INVALID_PROPERTY_STRING) {
            TODO("Обработка ошибки")
        } else {
            val id = movieBundle.id
            textViewTitle.text = movieDTO.title
            textViewPlot.text = movieDTO.overview
            textViewReleased.text = movieDTO.release_date.toString()
            textViewRating.text = movieDTO.vote_average.toString()
            textViewGenres.text = movieDTO.genre
            textViewRuntime.text = movieDTO.runtime.toString()
            Picasso
                .get()
                .load(BASE_URL + IMAGE_SIZE + movieDTO.poster_path)
                .into(binding.imageViewPoster)

            Picasso
                .get()
                .load(BASE_URL + IMAGE_SIZE + movieDTO.backdrop_path)
                .into(binding.imageViewBackgroundPoster)
        }
    }

    override fun onDestroyView() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroyView()
    }
}