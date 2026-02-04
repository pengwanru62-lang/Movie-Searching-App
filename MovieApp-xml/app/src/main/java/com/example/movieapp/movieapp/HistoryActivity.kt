package com.example.movieapp.movieapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.Movie
import com.example.movieapp.R
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.db.AppDatabase
import com.example.movieapp.repo.MovieRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var repo: MovieRepository
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val db = AppDatabase.getInstance(applicationContext)
        repo = MovieRepository(db)

        recycler = findViewById(R.id.historyList)
        adapter = MovieAdapter(emptyList(), Glide.with(this))
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        lifecycleScope.launch {
            repo.getAllHistory().collectLatest { list ->
                val movies = list.map { e ->
                    Movie(
                        title = e.title,
                        year = "",
                        posterUrl = e.posterUrl ?: "",
                        imdbID = e.imdbID
                    )
                }
                adapter.updateData(movies)
            }
        }
    }
}