package com.example.movieapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.movieapp.util.Constants
import com.example.movieapp.db.AppDatabase
import com.example.movieapp.repo.MovieRepository
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var meta: TextView
    private lateinit var plot: TextView

    private lateinit var apiService: MovieApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        poster = findViewById(R.id.detailPoster)
        title = findViewById(R.id.detailTitle)
        meta = findViewById(R.id.detailMeta)
        plot = findViewById(R.id.detailPlot)

        val imdbID = intent.getStringExtra("imdbID")
        if (imdbID == null) {
            Toast.makeText(this, "无效的电影信息", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.OMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(MovieApiService::class.java)

        fetchMovieDetails(imdbID)
    }

    private fun fetchMovieDetails(id: String) {
        val call = apiService.getMovieDetail(Constants.OMDB_API_KEY, id)
        call.enqueue(object : Callback<MovieDetailResponse> {
            override fun onResponse(
                call: Call<MovieDetailResponse>,
                response: Response<MovieDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val detail = response.body()
                    if (detail == null) {
                        Toast.makeText(this@DetailActivity, "未获取到详情", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val safeTitle = detail.Title ?: ""
                    val safeYear = detail.Year ?: ""

                    title.text = safeTitle
                    meta.text = listOfNotNull(
                        safeYear.takeIf { it.isNotBlank() },
                        detail.Director?.takeIf { it.isNotBlank() }
                    ).joinToString(" · ")
                    plot.text = detail.Plot ?: ""

                    val rawUrl = detail.Poster
                    val validUrl = if (!rawUrl.isNullOrBlank() && rawUrl != "N/A") {
                        if (rawUrl.startsWith("http://")) rawUrl.replaceFirst("http://", "https://") else rawUrl
                    } else null

                    if (validUrl != null) {
                        Glide.with(this@DetailActivity)
                            .load(validUrl)
                            .placeholder(R.drawable.ic_placeholder_poster)
                            .into(poster)
                    } else {
                        poster.setImageResource(R.drawable.ic_placeholder_poster)
                    }

                    // 使用 Room 持久化浏览记录
                    lifecycleScope.launch {
                        val repo = MovieRepository(AppDatabase.getInstance(applicationContext))
                        repo.saveHistory(id, safeTitle, rawUrl)
                    }
                } else {
                    Toast.makeText(this@DetailActivity, "加载详情失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "网络错误: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
