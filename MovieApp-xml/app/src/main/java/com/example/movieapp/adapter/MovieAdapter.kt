package com.example.movieapp.adapter

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.movieapp.DetailActivity
import com.example.movieapp.Movie
import com.example.movieapp.R

class MovieAdapter(
    private var movies: List<Movie>,
    private val glide: RequestManager
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.moviePoster)
        val title: TextView = view.findViewById(R.id.movieTitle)
        val year: TextView = view.findViewById(R.id.movieYear)
        val rating: TextView = view.findViewById(R.id.movieRating)
        val type: TextView = view.findViewById(R.id.movieType)
        val overview: TextView = view.findViewById(R.id.movieOverview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        holder.year.text = movie.year
        holder.type.text = movie.type ?: ""
        holder.rating.text = movie.rating ?: "-"
        holder.overview.text = movie.overview ?: ""

        val rawUrl = movie.posterUrl
        val validUrl = if (!rawUrl.equals("N/A", ignoreCase = true) && !TextUtils.isEmpty(rawUrl)) {
            if (rawUrl.startsWith("http://")) rawUrl.replaceFirst("http://", "https://") else rawUrl
        } else null

        if (validUrl != null) {
            glide
                .load(validUrl)
                .placeholder(R.drawable.ic_placeholder_poster)
                .error(R.drawable.ic_placeholder_poster)
                .into(holder.poster)
        } else {
            holder.poster.setImageResource(R.drawable.ic_placeholder_poster)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("imdbID", movie.imdbID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateData(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    fun appendData(newMovies: List<Movie>) {
        if (newMovies.isEmpty()) return
        movies = movies + newMovies
        notifyDataSetChanged()
    }

    fun updateItemDetails(imdbID: String, rating: String?, overview: String?) {
        val idx = movies.indexOfFirst { it.imdbID == imdbID }
        if (idx >= 0) {
            val current = movies[idx]
            val updated = current.copy(
                rating = rating ?: current.rating,
                overview = overview ?: current.overview
            )
            val mutable = movies.toMutableList()
            mutable[idx] = updated
            movies = mutable
            notifyItemChanged(idx)
        }
    }
}
