package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
fun PreviewScreen(
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

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Preview (${index + 1}/${cards.size})",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (cards.isNotEmpty()) {
            val card = cards[index]

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KinokiBackground)
                    .padding(innerPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.prevCard() },
                    enabled = index > 0,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous",
                        tint = if (index > 0) KinokiDarkBlue else Color.LightGray.copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = KinokiWhite),
                    shape = RectangleShape,
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(0.7f)
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

                        if (face != CardFace.FRONT && !card.center.isNullOrBlank()) {
                            Spacer(Modifier.height(24.dp))
                            Text(
                                text = card.center,
                                style = MaterialTheme.typography.titleLarge,
                                color = KinokiDarkBlue.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                        }

                        if (face == CardFace.BACK) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 24.dp),
                                color = KinokiInactiveIcon.copy(alpha = 0.5f)
                            )
                            Text(
                                text = card.back,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = KinokiDarkBlue,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { viewModel.nextCard() },
                    enabled = index < cards.size - 1,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = if (index < cards.size - 1) KinokiDarkBlue else Color.LightGray.copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}