package com.lazday.appmovie.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.lazday.appmovie.R
import com.lazday.appmovie.activity.DetailActivity
import com.lazday.appmovie.adapter.MainAdapter
import com.lazday.appmovie.model.Constant
import com.lazday.appmovie.model.MovieModel
import com.lazday.appmovie.model.MovieResponse
import com.lazday.appmovie.retrofit.ApiService
import kotlinx.android.synthetic.main.fragment_popular.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PopularFragment : Fragment() {

//    cara penggunaan sama percis dengan di MainActivity cuman beda nya inflate layout dia
//    membutuhkan v adalah layout yang di sini yang mewakili inflater oncreate view
//    di mana ketika ingin mengambil id kita menggunakan v

    lateinit var v: View
    lateinit var mainAdapter: MainAdapter
    private var isScrolling = false
    private var currentPage = 1
    private var totalPages = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_popular, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        setupListener()

    }

    override fun onStart() {
        super.onStart()
        getMoviePopular()
        showLoadingNextPage(false)
    }


    private fun setupRecyclerview() {

        mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.onAdapterListener {
            override fun onClick(movie: MovieModel) {
                startActivity(Intent(requireContext(), DetailActivity::class.java))
            }

        })
        v.list_movie.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }

    private fun setupListener() {
        v.scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
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
                            getMoviePopularNextPage()

                        }
                    }
                }
            }

        })
    }




    fun getMoviePopular(){
        currentPage = 1
        showLoading(true)
        ApiService().endpoint.getMoviePopular(Constant.API_KEY, 1)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
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

    fun getMoviePopularNextPage(){
        currentPage += 1
        showLoadingNextPage(true)
        ApiService().endpoint.getMoviePopular(Constant.API_KEY, currentPage)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
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
            true -> v.progress_movie.visibility = View.VISIBLE
            false -> v.progress_movie.visibility = View.GONE
        }
    }

    fun showLoadingNextPage(loading: Boolean) {
        when(loading) {
            true -> {
                isScrolling = true
                v.progress_movie_next_page.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                v.progress_movie_next_page.visibility = View.GONE
            }
        }
    }


    fun showMovie(response: MovieResponse){
        totalPages = response.total_pages!!
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
        totalPages = response.total_pages!!
        mainAdapter.setDataNextPage(response.results)
        Toast.makeText(requireContext(), "Page $currentPage", Toast.LENGTH_SHORT).show()


        //Refrensi

//        Log.d(TAG, "responseMovie: $response")
//        Log.d(TAG, "responseMovie: ${response.total_pages}")
//
//        for (movie in response.results) {
//            Log.d(TAG, "movie_title: ${movie.title}")
//        }

    }


}