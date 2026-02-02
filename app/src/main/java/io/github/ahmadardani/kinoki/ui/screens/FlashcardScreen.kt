package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.ahmadardani.kinoki.ui.components.CommonTopBar
import io.github.ahmadardani.kinoki.ui.theme.*
import io.github.ahmadardani.kinoki.ui.viewmodel.CardFace
import io.github.ahmadardani.kinoki.ui.viewmodel.StudyViewModel

@Composable
fun FlashcardScreen(
    deckId: String,
    viewModel: StudyViewModel,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(deckId) {
        viewModel.loadSession(deckId, isShuffle = false)
    }

    val cards by viewModel.cards.collectAsStateWithLifecycle()
    val index by viewModel.currentIndex.collectAsStateWithLifecycle()
    val face by viewModel.currentFace.collectAsStateWithLifecycle()
    val isFinished by viewModel.isFinished.collectAsStateWithLifecycle()

    LaunchedEffect(isFinished) {
        if (isFinished) onNavigateBack()
    }

    Scaffold(
        topBar = {
            CommonTopBar(title = "Flashcard", onBackClick = onNavigateBack)
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(KinokiBackground)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = { viewModel.nextCard() },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFCDD2),
                        contentColor = Color(0xFFB71C1C)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                ) {
                    Text(
                        text = "Forgot",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { viewModel.nextCard() },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC8E6C9),
                        contentColor = Color(0xFF1B5E20)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                ) {
                    Text(
                        text = "Remember",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        if (cards.isNotEmpty()) {
            val card = cards[index]

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KinokiBackground)
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = KinokiWhite),
                    shape = RectangleShape,
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .clickable { viewModel.onCardTap() }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = card.front,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = KinokiDarkBlue
                        )

                        if (face != CardFace.FRONT) {
                            Spacer(Modifier.height(24.dp))
                            if (!card.center.isNullOrBlank()) {
                                Text(
                                    text = card.center,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = KinokiDarkBlue.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 32.dp),
                                color = KinokiInactiveIcon.copy(alpha = 0.5f)
                            )

                            Text(
                                text = card.back,
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = KinokiDarkBlue,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Spacer(Modifier.height(48.dp))
                            Text(
                                text = "Tap card to show answer",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
