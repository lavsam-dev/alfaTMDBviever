package com.learn.lavsam.alfatmdbviewer.model.data

data class MovieDTO(
    val id: Int?,
    val original_title: String?,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?,
    val title: String?,
    val vote_average: Double?,
    val runtime: Int?
)
