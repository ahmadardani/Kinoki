package io.github.ahmadardani.kinoki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.ahmadardani.kinoki.ui.screens.*
import io.github.ahmadardani.kinoki.ui.theme.KinokiTheme
import io.github.ahmadardani.kinoki.ui.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KinokiTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val viewModelFactory = remember { ViewModelFactory(context) }

                val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)

                NavHost(
                    navController = navController,
                    startDestination = "home",
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }
                ) {

                    composable("home") {
                        HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToSettings = { navController.navigate("settings") },
                            onAddDeckClick = { navController.navigate("create_deck") },
                            onDeckClick = { deckId, count, title ->
                                navController.navigate("deck_detail/$deckId/$count/$title")
                            },
                            onEditDeck = { deckId ->
                                navController.navigate("edit_deck/$deckId")
                            }
                        )
                    }

                    composable("settings") {
                        SettingsScreen(onNavigateToHome = { navController.navigate("home") { popUpTo("home") { inclusive = true } } })
                    }

                    composable("create_deck") {
                        CreateDeckScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onCreateDeck = { title -> homeViewModel.createDeck(title); navController.popBackStack() }
                        )
                    }

                    composable("edit_deck/{deckId}") { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId") ?: return@composable

                        EditDeckScreen(
                            deckId = deckId,
                            viewModel = homeViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable("add_card/{deckId}") { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId") ?: return@composable
                        val addCardViewModel: AddCardViewModel = viewModel(factory = viewModelFactory)
                        AddCardScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onAddCard = { f, b, c -> addCardViewModel.addCard(deckId, f, b, c) }
                        )
                    }

                    composable(
                        route = "deck_detail/{deckId}/{count}/{title}",
                        arguments = listOf(
                            navArgument("deckId") { type = NavType.StringType },
                            navArgument("count") { type = NavType.IntType },
                            navArgument("title") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId") ?: return@composable
                        val count = backStackEntry.arguments?.getInt("count") ?: 0
                        val title = backStackEntry.arguments?.getString("title") ?: "Deck"

                        val detailViewModel: DeckDetailViewModel = viewModel(factory = viewModelFactory)

                        DeckDetailScreen(
                            deckId = deckId,
                            initialCount = count,
                            initialTitle = title,
                            viewModel = detailViewModel,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToAddCard = { navController.navigate("add_card/$deckId") },
                            onNavigateToPreview = { navController.navigate("preview/$deckId") },
                            onNavigateToFlashcard = { navController.navigate("flashcard/$deckId") },
                            onNavigateToQuiz = { navController.navigate("quiz/$deckId") }
                        )
                    }

                    composable("preview/{deckId}") { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId") ?: return@composable
                        val studyViewModel: StudyViewModel = viewModel(factory = viewModelFactory)
                        PreviewScreen(deckId, studyViewModel) { navController.popBackStack() }
                    }

                    composable("flashcard/{deckId}") { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId") ?: return@composable
                        val studyViewModel: StudyViewModel = viewModel(factory = viewModelFactory)
                        FlashcardScreen(deckId, studyViewModel) { navController.popBackStack() }
                    }

                    composable("quiz/{deckId}") { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId") ?: return@composable
                        val studyViewModel: StudyViewModel = viewModel(factory = viewModelFactory)
                        QuizScreen(deckId, studyViewModel) { navController.popBackStack() }
                    }
                }
            }
        }
    }
}