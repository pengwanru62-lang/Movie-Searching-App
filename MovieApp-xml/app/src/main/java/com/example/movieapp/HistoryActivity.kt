package com.example.movieapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.db.AppDatabase
import com.example.movieapp.repo.MovieRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var historyList: RecyclerView
    private lateinit var adapter: MovieAdapter
    private lateinit var repo: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyList = findViewById(R.id.historyList)
        historyList.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(emptyList(), Glide.with(this))
        historyList.adapter = adapter

        repo = MovieRepository(AppDatabase.getInstance(applicationContext))

        lifecycleScope.launch {
            repo.getAllHistory().collectLatest { entities ->
                val movies = entities.map { e ->
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

