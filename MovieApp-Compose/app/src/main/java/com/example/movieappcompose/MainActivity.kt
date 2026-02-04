package com.example.movieappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.movieappcompose.ui.theme.MovieAppComposeTheme
import androidx.compose.material.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.movieappcompose.ui.screens.DetailScreen
import com.example.movieappcompose.ui.screens.HistoryScreen
import com.example.movieappcompose.ui.screens.SearchScreen
import com.example.movieappcompose.viewmodel.ViewModelFactory
import com.example.movieappcompose.viewmodel.SearchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(applicationContext)
        setContent {
            MovieAppComposeTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "search") {
                        composable("search") {
                            val vm: SearchViewModel = ViewModelProvider(this@MainActivity, factory)
                                .get(SearchViewModel::class.java)
                            SearchScreen(
                                viewModel = vm,
                                onOpenDetail = { imdbId ->
                                    navController.navigate("detail/$imdbId")
                                },
                                onOpenHistory = {
                                    navController.navigate("history")
                                }
                            )
                        }
                        composable(
                            "detail/{imdbId}",
                            arguments = listOf(navArgument("imdbId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val imdbId = backStackEntry.arguments?.getString("imdbId") ?: ""
                            DetailScreen(imdbId = imdbId, factory = factory, onBack = {
                                navController.popBackStack()
                            })
                        }
                        composable("history") {
                            HistoryScreen(factory = factory, onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
