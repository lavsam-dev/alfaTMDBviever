package com.learn.lavsam.alfatmdbviewer.model.data

import android.os.Parcelable
import android.widget.TextView
import com.learn.lavsam.alfatmdbviewer.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int = 0,
    val title: String? = "",
    val poster_path: String? = "",
    val release_date: Int? = 0,
    val vote_average: Double? = 0.0,
    val overview: String? = "",
    val genre: String? = "",
    val runtime: Int? = 0,
) : Parcelable

fun getMovieList() = listOf(

    Movie(
        10401,"The Girl on the Bridge", "",1999,7.6,
        ".",
        "Drama, Comedy, Romance",90
    ),

    Movie(
        680, "Pulp Fiction", "", 1994, 8.5,
        ".",
        "Thriller, Crime", 155
    ),

    Movie(
        9659, "Mad Max", "", 1979, 9.1,
        ".",
        "Adventure, Action, Thriller, Science Fiction", 160
    ),

    Movie(
        19124, "Blind Fury", "", 1989, 9.1,
        ".",
        "Action, Thriller", 86
    ),

    Movie(
        10681, "WALL·E", "", 2008, 7.4,
        ".",
        "Animation, Family, Science Fiction", 98
    )
)
