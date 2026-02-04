package com.example.movieappcompose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.movieappcompose.data.local.HistoryEntity
import com.example.movieappcompose.viewmodel.HistoryViewModel
import com.example.movieappcompose.viewmodel.ViewModelFactory

@Composable
fun HistoryScreen(factory: ViewModelFactory, onBack: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val vm: HistoryViewModel = ViewModelProvider(
        activity ?: throw IllegalStateException("No ComponentActivity"),
        factory
    ).get(HistoryViewModel::class.java)

    val history by vm.history.collectAsState()

    LaunchedEffect(Unit) { vm.loadHistory() }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("History") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                TextButton(onClick = { vm.clearHistory() }) {
                    Text("Clear")
                }
            }
        )
    }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(history) { item: HistoryEntity ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* optionally open detail */ }
                        .padding(8.dp)
                ) {
                    if (item.poster != null && item.poster != "N/A") {
                        AsyncImage(
                            model = item.poster,
                            contentDescription = item.title,
                            modifier = Modifier.size(80.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier.size(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No Image")
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(item.title)
                        Text(
                            "Viewed at: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(item.viewedAt))}",
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
                Divider()
            }
        }
    }
}
