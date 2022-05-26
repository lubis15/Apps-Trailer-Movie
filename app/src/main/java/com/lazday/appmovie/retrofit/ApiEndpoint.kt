package com.lazday.appmovie.retrofit

import com.lazday.appmovie.model.DetailResponse
import com.lazday.appmovie.model.MovieResponse
import com.lazday.appmovie.model.TrailerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndpoint {

    @GET("movie/now_playing")
    fun getMovieNowPlaying(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("movie/popular")
    fun getMoviePopular(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") page: String
    ): Call<DetailResponse>


    @GET("movie/{movie_id}/videos")
    fun getMovieTrailer(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") page: String
    ): Call<TrailerResponse>
}