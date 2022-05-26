package com.lazday.appmovie.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.lazday.appmovie.R
import com.lazday.appmovie.adapter.MainAdapter
import com.lazday.appmovie.model.Constant
import com.lazday.appmovie.model.MovieModel
import com.lazday.appmovie.model.MovieResponse
import com.lazday.appmovie.retrofit.ApiService
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val moviePopular = 0
const val movieNowPlaying = 1

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    //untuk menginialisasi adapter

    lateinit var mainAdapter: MainAdapter
    private var movieCategory = 0
    private val api = ApiService().endpoint
    private var isScrolling = false
    private var currentPage = 1
    private var totalPages = 0
   

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) 
        setSupportActionBar(findViewById(R.id.toolbar))
        setupRecyclerview()
        setupListener()



       

    }

    override fun onStart() {
        super.onStart()
        getMovie()
        showLoadingNextPage(false)
    }


    private fun setupRecyclerview() {

        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.onAdapterListener {
            override fun onClick(movie: MovieModel) {
                startActivity(Intent(applicationContext, DetailActivity::class.java))
            }

        })
        list_movie.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }

    private fun setupListener() {
        scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                    if (!isScrolling) {
                        if ( currentPage <= totalPages ) {
                            getMovieNextPage()

                        }
                    }
                }
            }

        })
    }

    private fun getMovie(){


        scrollView.scrollTo(0,0)

        currentPage = 1
        showLoading(true)

        var apiCall: Call<MovieResponse>? = null
        when(movieCategory) {
            moviePopular -> {
                apiCall = api.getMoviePopular(Constant.API_KEY, 1)
            }
            movieNowPlaying -> {
                apiCall = api.getMovieNowPlaying(Constant.API_KEY, 1)
            }
        }

        apiCall!!
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d(TAG, "errorResponse: $t")
                    showLoading(false)
                }

                //onResponse kalau berhasil dia akan muncul disini

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful){
                        showMovie( response.body()!! )
                    }
                }
                //onFailure kalau gagal



            })

    }


    private fun getMovieNextPage(){

        currentPage += 1
        showLoadingNextPage(true)

        var apiCall: Call<MovieResponse>? = null
        when(movieCategory) {
            moviePopular -> {
                apiCall = api.getMoviePopular(Constant.API_KEY, currentPage)
            }
            movieNowPlaying -> {
                apiCall = api.getMovieNowPlaying(Constant.API_KEY, currentPage)
            }
        }

        apiCall!!
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d(TAG, "errorResponse: $t")
                    showLoadingNextPage(false)
                }

                //onResponse kalau berhasil dia akan muncul disini

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoadingNextPage(false)
                    if (response.isSuccessful){
                        showMovieNextPage( response.body()!! )
                    }
                }
                //onFailure kalau gagal



            })

    }

    fun showLoading(loading: Boolean) {
        when(loading) {
            true -> progress_movie.visibility = View.VISIBLE
            false -> progress_movie.visibility = View.GONE
        }
    }


    fun showLoadingNextPage(loading: Boolean) {
        when(loading) {
            true -> {
                isScrolling = true
                progress_movie_next_page.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                progress_movie_next_page.visibility = View.GONE
            }
        }
    }


    fun showMovie(response: MovieResponse){
        totalPages = response.total_pages!!.toInt()
        mainAdapter.setData(response.results)


        //Refrensi

//        Log.d(TAG, "responseMovie: $response")
//        Log.d(TAG, "responseMovie: ${response.total_pages}")
//
//        for (movie in response.results) {
//            Log.d(TAG, "movie_title: ${movie.title}")
//        }

    }

    fun showMovieNextPage(response: MovieResponse){
        totalPages = response.total_pages!!.toInt()
        mainAdapter.setDataNextPage(response.results)
        showMessage("Page $currentPage")


        //Refrensi

//        Log.d(TAG, "responseMovie: $response")
//        Log.d(TAG, "responseMovie: ${response.total_pages}")
//
//        for (movie in response.results) {
//            Log.d(TAG, "movie_title: ${movie.title}")
//        }

    }

    fun showMessage(msg: String){
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_popular -> {
                showMessage("movie popular selected")
                movieCategory = moviePopular
                getMovie()
                true
            }
            R.id.action_now_playing -> {
                showMessage("movie now playing selected")
                movieCategory = movieNowPlaying
                getMovie()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

   
}