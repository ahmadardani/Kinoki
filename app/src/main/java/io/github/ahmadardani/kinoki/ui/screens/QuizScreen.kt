package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.ahmadardani.kinoki.ui.components.CommonTopBar
import io.github.ahmadardani.kinoki.ui.theme.*
import io.github.ahmadardani.kinoki.ui.viewmodel.StudyViewModel

@Composable
fun QuizScreen(
    deckId: String,
    viewModel: StudyViewModel,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(deckId) {
        viewModel.loadSession(deckId, isShuffle = true)
    }

    val cards by viewModel.cards.collectAsStateWithLifecycle()
    val index by viewModel.currentIndex.collectAsStateWithLifecycle()
    val options by viewModel.quizOptions.collectAsStateWithLifecycle()
    val isFinished by viewModel.isFinished.collectAsStateWithLifecycle()
    val score by viewModel.quizScore.collectAsStateWithLifecycle()

    var showHint by remember { mutableStateOf(false) }

    LaunchedEffect(index) { showHint = false }

    Scaffold(
        topBar = {
            CommonTopBar(title = "Quiz", onBackClick = onNavigateBack)
        }
    ) { innerPadding ->
        if (isFinished) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).background(KinokiBackground),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Quiz Finished!", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = KinokiDarkBlue)
                Spacer(Modifier.height(16.dp))
                Text("Score: $score / ${cards.size}", style = MaterialTheme.typography.headlineMedium, color = KinokiDarkBlue)
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = KinokiDarkBlue)
                ) {
                    Text("Back to Deck")
                }
            }
        } else if (cards.isNotEmpty()) {
            val card = cards[index]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KinokiBackground)
                    .padding(innerPadding)
                    .padding(24.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = KinokiWhite),
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = card.front, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = KinokiDarkBlue)

                            if (showHint && !card.center.isNullOrBlank()) {
                                Spacer(Modifier.height(8.dp))
                                Text(text = "Hint: ${card.center}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            }
                        }

                        if (!card.center.isNullOrBlank()) {
                            IconButton(
                                onClick = { showHint = true },
                                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                            ) {
                                Icon(Icons.Default.Lightbulb, null, tint = if(showHint) Color.Yellow else Color.Gray)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
                Text("Select the correct answer:", style = MaterialTheme.typography.titleMedium, color = KinokiDarkBlue)
                Spacer(Modifier.height(16.dp))

                options.forEach { option ->
                    Button(
                        onClick = { viewModel.submitAnswer(option) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KinokiWhite, contentColor = KinokiDarkBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(option, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                    }
                }
            }
        }
    }
}