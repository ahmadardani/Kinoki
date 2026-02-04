package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.BorderStroke
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(KinokiBackground)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Session Complete",
                    style = MaterialTheme.typography.headlineSmall,
                    color = KinokiDarkBlue
                )

                Spacer(Modifier.height(24.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = KinokiWhite),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$score / ${cards.size}",
                            style = MaterialTheme.typography.displayMedium,
                            color = KinokiDarkBlue,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "Total Score",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                        HorizontalDivider(color = KinokiInactiveIcon.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ScoreItem(label = "Correct", count = score, color = Color(0xFF4CAF50))
                            ScoreItem(label = "Mistakes", count = cards.size - score, color = Color(0xFFE57373))
                        }
                    }
                }

                Spacer(Modifier.height(48.dp))

                Button(
                    onClick = { viewModel.loadSession(deckId, isShuffle = true) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = KinokiWhite,
                        contentColor = KinokiDarkBlue
                    ),
                    border = BorderStroke(1.dp, KinokiDarkBlue),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(
                        text = "Try Again",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = KinokiDarkBlue,
                        contentColor = KinokiWhite
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(
                        text = "Back to Home",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Normal
                    )
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
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = card.front,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = KinokiDarkBlue
                            )

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
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = "Hint",
                                    tint = if(showHint) KinokiDarkBlue else KinokiInactiveIcon
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
                Text(
                    text = "Select the correct answer:",
                    style = MaterialTheme.typography.titleMedium,
                    color = KinokiDarkBlue,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(16.dp))

                options.forEach { option ->
                    Button(
                        onClick = { viewModel.submitAnswer(option) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = KinokiWhite, contentColor = KinokiDarkBlue),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                    ) {
                        Text(option, fontSize = MaterialTheme.typography.bodyLarge.fontSize, fontWeight = FontWeight.Normal)
                    }
                }
            }
        }
    }
}
@Composable
fun ScoreItem(label: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Normal,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}