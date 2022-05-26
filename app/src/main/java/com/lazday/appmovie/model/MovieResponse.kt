package com.lazday.appmovie.model

data class MovieResponse (
    val total_pages: Int?,
    val results: List<MovieModel>
)