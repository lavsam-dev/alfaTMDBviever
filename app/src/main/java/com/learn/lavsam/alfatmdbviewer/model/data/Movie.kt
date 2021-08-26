package com.learn.lavsam.alfatmdbviewer.model.data

import android.os.Parcelable
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
        615457,
        "Nobody",
        "",
        2021,
        7.6,
        "///Hutch Mansell, a suburban dad, overlooked husband, nothing neighbor — a \"nobody.\" When two thieves break into his home one night, Hutch's unknown long-simmering rage is ignited and propels him on a brutal path that will uncover dark secrets he fought to leave behind.",
        "Drama, Crime, Action, Thriller",
        120
    ),

    Movie(
        680, "Pulp Fiction", "", 1994, 8.5,
        "///A burger-loving hit man, his philosophical partner, a drug-addled gangster's moll and a washed-up boxer converge in this sprawling, comedic crime caper. Their adventures unfurl in three stories that ingeniously trip back and forth in time.",
        "Thriller, Crime", 155
    ),

    Movie(
        9659, "Mad Max", "", 1979, 9.1,
        "///In a dystopian world, a vengeful Australian policeman named Max sets out to stop a violent motorcycle gang.",
        "Adventure, Action, Thriller, Science Fiction", 160
    ),

    Movie(
        27205, "Inception", "", 2010, 7.4,
        "///Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: \"inception\", the implantation of another person's idea into a target's subconscious.",
        "Action, Science Fiction, Adventure", 130
    )
)
