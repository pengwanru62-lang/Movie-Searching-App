package com.example.movieapp

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.util.Constants
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var searchInput: EditText
    private lateinit var movieListView: RecyclerView
    private lateinit var adapter: MovieAdapter
    private var apiService: MovieApiService? = null

    private var currentQuery: String = ""
    private var currentPage: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchInput = findViewById(R.id.searchInput)
        movieListView = findViewById(R.id.movieList)

        // 设置列表
        adapter = MovieAdapter(emptyList<Movie>(), Glide.with(this))
        movieListView.layoutManager = LinearLayoutManager(this)
        movieListView.adapter = adapter

        // Retrofit 初始化（加入 OkHttp 日志与超时）
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.OMDB_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(MovieApiService::class.java)

        // 点击回车键搜索
        searchInput.setOnEditorActionListener { _, _, _ ->
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                searchMovies(query)
            } else {
                Toast.makeText(this, "请输入电影名", Toast.LENGTH_SHORT).show()
            }
            true
        }

        // 底部导航：历史
        findViewById<LinearLayout>(R.id.navHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        // 滚动到底部自动加载下一页
        movieListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val visibleItemCount = lm.childCount
                val totalItemCount = lm.itemCount
                val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 2 && firstVisibleItemPosition >= 0) {
                        // 触发下一页
                        currentPage += 1
                        loadPage(currentPage)
                    }
                }
            }
        })
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(ConnectivityManager::class.java)
        val nw = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(nw) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    private fun searchMovies(query: String) {
        if (!isOnline()) {
            Toast.makeText(this, "当前无网络，请检查连接", Toast.LENGTH_SHORT).show()
            return
        }
        currentQuery = query
        currentPage = 1
        isLastPage = false
        loadPage(currentPage)
    }

    private fun loadPage(page: Int) {
        val service = apiService ?: return
        isLoading = true
        lifecycleScope.launch {
            try {
                val response = service.searchMovies(Constants.OMDB_API_KEY, currentQuery, page)
                val movies: List<Movie> = response.Search ?: emptyList()
                if (page == 1) {
                    adapter.updateData(movies)
                } else {
                    adapter.appendData(movies)
                }
                // 补充详情以显示评分/简介
                enrichWithDetails(movies)
                // OMDb 默认每页 10 条，依据返回数量判断是否最后一页
                if (movies.size < Constants.PAGE_SIZE) {
                    isLastPage = true
                }
            } catch (e: Exception) {
                val msg = when (e) {
                    is HttpException -> "HTTP ${e.code()} ${e.message()}"
                    else -> e.message ?: "未知错误"
                }
                Toast.makeText(this@MainActivity, "网络请求失败: $msg", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    private fun enrichWithDetails(movies: List<Movie>) {
        val service = apiService ?: return
        for (m in movies) {
            val call: Call<MovieDetailResponse> = service.getMovieDetail(Constants.OMDB_API_KEY, m.imdbID)
            call.enqueue(object : Callback<MovieDetailResponse> {
                override fun onResponse(
                    call: Call<MovieDetailResponse>,
                    response: Response<MovieDetailResponse>
                ) {
                    val body = response.body() ?: return
                    adapter.updateItemDetails(m.imdbID, body.imdbRating, body.Plot)
                }
                override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) { /* ignore per-item */ }
            })
        }
    }
}