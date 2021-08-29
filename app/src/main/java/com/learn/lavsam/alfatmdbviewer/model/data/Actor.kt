package com.learn.lavsam.alfatmdbviewer.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Actor(
    val id: Int = 0,
    val name: String? = "",
    val gender: Int = 0,
    val birthDay: String? = "",
    val deathDay: String? = "",
    val placeOfBirth: String? = "",
    val profilePath: String? = "",
    val popularity: Double = 0.0,
) : Parcelable

fun getActorsList() = listOf(
    Actor(2461, "Mel Gibson", 2, "1956-01-03", "",
        "Peekskill, New York, USA", "/uHldNnlyi73x9hoZBGjISGoh908.jpg", 18.032),
    Actor(18897, "Jackie Chan", 2, "1954-04-07", "",
        "Victoria Peak, Hong Kong", "/nraZoTzwJQPHspAVsKfgl3RXKKa.jpg", 34.606),
    Actor(6384, "Keanu Reeves", 2, "1964-09-02", "",
        "Beirut, Lebanon", "/rRdru6REr9i3WIHv2mntpcgxnoY.jpg", 35.002),
    Actor(1318905, "Lyubov Aksyonova", 1, "1990-03-15", "",
        "Moscow, USSR (Russia)", "/yOQX4JPwX1uhMyVT1jJigAk7iUr.jpg", 25.334),
    Actor(63, "Milla Jovovich", 1, "1975-12-17", "",
        "Kiev, Ukraine", "/usWnHCzbADijULREZYSJ0qfM00y.jpg", 20.022),
    Actor(2460109, "Misato Morita", 1, "1996-09-13", "",
        "Kanagawa Prefecture, Japan", "/rS8y85AhCkQm5L13Qzn2dWT5Yfe.jpg", 11.89)
)