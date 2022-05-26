package com.lazday.appmovie.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lazday.appmovie.R
import com.lazday.appmovie.model.Constant
import com.lazday.appmovie.model.MovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_main.view.*

class MainAdapter (var movies: ArrayList<MovieModel>, var listener: onAdapterListener): RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    private val TAG: String = "MainAdapter"



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_main, parent, false)

            )
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind( movie )
        val posterPath = Constant.POSTER_PATH + movie.poster_path

        Picasso.get()
            .load(posterPath)
            .placeholder(R.drawable.awal)
            .error(R.drawable.error)
            .into(holder.view.image_poster)

        holder.view.image_poster.setOnClickListener {
            Constant.MOVIE_ID = movie.id!!
            Constant.MOVIE_TITLE = movie.title!!
            listener.onClick(movie)
        }
    }

    override fun getItemCount() = movies.size


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val view = view
        fun bind(movies: MovieModel) {
            view.text_title.text = movies.title

        }
    }
    public fun setData(newMovies: List<MovieModel>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    public fun setDataNextPage(newMovies: List<MovieModel>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }


    interface onAdapterListener{
        fun onClick(movie: MovieModel)
    }
}