package com.learn.lavsam.alfatmdbviewer.model.data

data class MovieDTO(
    val id: Int?,
    val title: String?,
    val poster_path: String?,
    val release_date: Int?,
    val vote_average: Double?,
    val overview: String?,
    val backdrop_path: String?,
    val genre: String? = "",
    val runtime: Int?
)
