package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.ahmadardani.kinoki.ui.components.CommonTopBar
import io.github.ahmadardani.kinoki.ui.theme.*
import io.github.ahmadardani.kinoki.ui.viewmodel.DeckDetailViewModel

@Composable
fun DeckDetailScreen(
    deckId: String,
    initialCount: Int,
    initialTitle: String,
    viewModel: DeckDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddCard: () -> Unit,
    onNavigateToPreview: () -> Unit,
    onNavigateToFlashcard: () -> Unit,
    onNavigateToQuiz: () -> Unit
) {

    LaunchedEffect(deckId) {
        viewModel.loadDeck(deckId)
    }

    val deck by viewModel.deck.collectAsStateWithLifecycle()

    val displayCount = deck?.cards?.size ?: initialCount
    val displayTitle = deck?.title ?: initialTitle

    Scaffold(
        topBar = {
            CommonTopBar(
                title = displayTitle,
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToAddCard,
                containerColor = KinokiDarkBlue,
                contentColor = KinokiWhite,
                icon = { Icon(Icons.Default.Add, "Add Cards") },
                text = { Text("Add Cards") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(KinokiBackground)
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = KinokiWhite),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$displayCount",
                        style = MaterialTheme.typography.displayMedium,
                        color = KinokiDarkBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total Cards",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = "Study Mode",
                style = MaterialTheme.typography.titleMedium,
                color = KinokiDarkBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textAlign = TextAlign.Start
            )

            val hasCards = displayCount > 0

            MenuButton(
                title = "Preview",
                subtitle = "Read cards sequentially",
                icon = Icons.Default.Visibility,
                enabled = hasCards,
                onClick = onNavigateToPreview
            )

            Spacer(modifier = Modifier.height(12.dp))

            MenuButton(
                title = "Flashcard",
                subtitle = "Classic flip card study",
                icon = Icons.Default.Style,
                enabled = hasCards,
                onClick = onNavigateToFlashcard
            )

            Spacer(modifier = Modifier.height(12.dp))

            MenuButton(
                title = "Quiz",
                subtitle = "Multiple choice test",
                icon = Icons.Default.School,
                enabled = displayCount >= 4,
                onClick = onNavigateToQuiz
            )

            if (displayCount in 1..3) {
                Text(
                    text = "Quiz requires at least 4 cards.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun MenuButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = KinokiWhite,
            contentColor = KinokiDarkBlue,
            disabledContainerColor = Color.White.copy(alpha = 0.5f),
            disabledContentColor = Color.Gray.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 0.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxWidth().height(80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if(enabled) KinokiDarkBlue else Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = if(enabled) Color.Gray else Color.LightGray)
            }
            if (enabled) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = KinokiInactiveIcon)
            }
        }
    }
}