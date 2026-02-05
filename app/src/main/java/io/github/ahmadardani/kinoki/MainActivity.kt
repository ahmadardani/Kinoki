package io.github.ahmadardani.kinoki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

                NavHost(navController = navController, startDestination = "home") {

                    composable("home") {
                        HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToSettings = { navController.navigate("settings") },
                            onAddDeckClick = { navController.navigate("create_deck") },
                            onDeckClick = { deckId -> navController.navigate("deck_detail/$deckId") },
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

                    composable("deck_detail/{deckId}") { backStackEntry ->
                        val deckId = backStackEntry.arguments?.getString("deckId") ?: return@composable
                        val detailViewModel: DeckDetailViewModel = viewModel(factory = viewModelFactory)

                        DeckDetailScreen(
                            deckId = deckId,
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