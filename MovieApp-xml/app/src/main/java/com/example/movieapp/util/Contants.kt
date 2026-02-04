package com.example.movieapp.util

object Constants {
    // 请在这里写你的 OMDb API key，比如 "abcd1234"
    // 或者更安全的做法：在 local.properties 设置 omdb_api_key=xxxx 并在 Gradle 读取到 BuildConfig
    const val OMDB_API_KEY = "ec53e7de" // <-- 把这里替换为你的 key
    // 使用 HTTP 方案（已在 Network Security Config 放行 www.omdbapi.com 的明文流量）
    const val OMDB_BASE_URL = "http://www.omdbapi.com/"
    const val PAGE_SIZE = 10
}
