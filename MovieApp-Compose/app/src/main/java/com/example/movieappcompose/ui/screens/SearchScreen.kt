package com.example.movieappcompose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieappcompose.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onOpenDetail: (String) -> Unit,
    onOpenHistory: () -> Unit
) {
    val query by viewModel.query.collectAsState()
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("MovieAppCompose") },
            actions = {
                IconButton(onClick = onOpenHistory) {
                    Icon(Icons.Default.History, contentDescription = "History")
                }
            }
        )

        // 搜索栏
        Row(modifier = Modifier.padding(8.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.updateQuery(it) },
                modifier = Modifier.weight(1f),
                label = { Text("Search movies") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.searchNew()
                })
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.searchNew() }, modifier = Modifier.align(Alignment.CenterVertically)) {
                Text("Search")
            }
        }

        if (error != null) {
            Text("Error: $error", color = MaterialTheme.colors.error, modifier = Modifier.padding(8.dp))
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                items(movies) { movie ->
                    MovieListItem(
                        movieTitle = movie.title ?: "No title",
                        year = movie.year ?: "",
                        posterUrl = movie.poster ?: "",
                        onClick = {
                            movie.imdbID?.let { onOpenDetail(it) }
                        }
                    )
                    Divider()
                }

                // 底部加载指示器的占位
                item {
                    if (isLoading) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }

            // 无限滚动触发下一页
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1 }
                    .collectLatest { lastVisibleIndex ->
                        val totalItems = listState.layoutInfo.totalItemsCount
                        if (lastVisibleIndex >= totalItems - 3) {
                            // 靠近底部，触发下一页
                            viewModel.fetchPage()
                        }
                    }
            }

            // 初始空时的小提示
            if (!isLoading && movies.isEmpty()) {
                Text("请输入关键词并搜索", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun MovieListItem(movieTitle: String, year: String, posterUrl: String, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(8.dp)) {

        val imageModifier = Modifier.size(96.dp).padding(end = 8.dp)
        if (posterUrl.isNotEmpty() && posterUrl != "N/A") {
            AsyncImage(model = posterUrl, contentDescription = movieTitle, modifier = imageModifier)
        } else {
            Box(modifier = imageModifier, contentAlignment = Alignment.Center) {
                Text("No Image", style = MaterialTheme.typography.caption)
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = movieTitle, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Year: $year", style = MaterialTheme.typography.body2)
        }
    }
}
