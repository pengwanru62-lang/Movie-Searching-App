package com.example.movieappcompose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.movieappcompose.viewmodel.DetailViewModel
import com.example.movieappcompose.viewmodel.ViewModelFactory
import com.example.movieappcompose.util.NetworkResult

@Composable
fun DetailScreen(imdbId: String, factory: ViewModelFactory, onBack: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val vm: DetailViewModel = ViewModelProvider(
        activity ?: throw IllegalStateException("No ComponentActivity"),
        factory
    ).get(DetailViewModel::class.java)

    val detailState by vm.detail.collectAsState()

    LaunchedEffect(imdbId) {
        vm.loadDetail(imdbId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (detailState) {
                is NetworkResult.Loading, null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                is NetworkResult.Error -> {
                    Text(
                        text = "Error: ${(detailState as NetworkResult.Error).message}",
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is NetworkResult.Success -> {
                    val d = (detailState as NetworkResult.Success).data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        if (!d.poster.isNullOrEmpty() && d.poster != "N/A") {
                            AsyncImage(
                                model = d.poster,
                                contentDescription = d.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = d.title ?: "", style = MaterialTheme.typography.h5)
                        Text(text = "Year: ${d.year ?: "N/A"}")
                        Text(text = "Runtime: ${d.runtime ?: "N/A"}")
                        Text(text = "Genre: ${d.genre ?: "N/A"}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Director: ${d.director ?: "N/A"}")
                        Text(text = "Actors: ${d.actors ?: "N/A"}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Plot:", style = MaterialTheme.typography.subtitle1)
                        Text(text = d.plot ?: "N/A")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "IMDB Rating: ${d.imdbRating ?: "N/A"}")
                    }
                }
            }
        }
    }
}
