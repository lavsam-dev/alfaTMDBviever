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
    Actor(6384, "Keanu Reeves", 2, "1964-09-02", "",
        "Beirut, Lebanon", "/rRdru6REr9i3WIHv2mntpcgxnoY.jpg", 35.002)
)