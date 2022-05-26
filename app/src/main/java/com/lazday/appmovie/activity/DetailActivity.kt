package com.lazday.appmovie.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lazday.appmovie.R
import com.lazday.appmovie.model.Constant
import com.lazday.appmovie.model.DetailResponse
import com.lazday.appmovie.retrofit.ApiService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private val TAG: String = "DetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setupView()
        setupListener()

    }

    override fun onStart() {
        super.onStart()
        getMovieDetail()
    }

    private fun setupView(){
        setSupportActionBar(findViewById((R.id.toolbar)))

        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupListener(){
        fab_play.setOnClickListener {
            startActivity(Intent(application, TrailerActivity::class.java))
        }
    }

    private fun getMovieDetail(){
        ApiService().endpoint.getMovieDetail(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<DetailResponse>{
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.isSuccessful) {
                        showMovie(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.d(TAG, t.toString())
                }

            })
    }
    fun showMovie(detail: DetailResponse){

        val backdropPath = Constant.BACKDROP_PATH + detail.backdrop_path
        Picasso.get()
            .load(backdropPath)
            .placeholder(R.drawable.awal)
            .error(R.drawable.error)
            .fit().centerCrop()
            .into(image_poster)

        text_title.text = detail.title
        text_vote.text = detail.vote_average.toString()
        text_overview.text = detail.overview

        for (genre in detail.genres!!) {
            text_genre.text = "${genre.name}"
        }


    }

    //untuk back

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }




}
