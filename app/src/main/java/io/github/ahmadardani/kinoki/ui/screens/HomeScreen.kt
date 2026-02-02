package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.ahmadardani.kinoki.data.model.Deck
import io.github.ahmadardani.kinoki.ui.components.MainBottomBar
import io.github.ahmadardani.kinoki.ui.components.MainTopBar
import io.github.ahmadardani.kinoki.ui.theme.*
import io.github.ahmadardani.kinoki.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToSettings: () -> Unit,
    onAddDeckClick: () -> Unit,
    onDeckClick: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val decks by viewModel.decks.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadDecks()
    }

    Scaffold(
        topBar = { MainTopBar(title = "Home") },
        bottomBar = {
            MainBottomBar(
                currentScreen = "home",
                onNavigateToHome = {},
                onNavigateToSettings = onNavigateToSettings
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isExpanded) {
                    ExtendedFloatingActionButton(
                        text = { Text("Import Deck") },
                        icon = { Icon(Icons.Default.FileUpload, contentDescription = null) },
                        onClick = { isExpanded = false },
                        containerColor = KinokiWhite,
                        contentColor = KinokiDarkBlue
                    )

                    ExtendedFloatingActionButton(
                        text = { Text("Create A New Deck") },
                        icon = { Icon(Icons.Default.Create, contentDescription = null) },
                        onClick = {
                            isExpanded = false
                            onAddDeckClick()
                        },
                        containerColor = KinokiWhite,
                        contentColor = KinokiDarkBlue
                    )
                }

                FloatingActionButton(
                    onClick = { isExpanded = !isExpanded },
                    containerColor = KinokiDarkBlue,
                    contentColor = KinokiWhite,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = if (isExpanded) "Close Menu" else "Add Options"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(KinokiBackground)
                .padding(innerPadding)
        ) {
            if (decks.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(decks) { deck ->
                        DeckItem(
                            deck = deck,
                            onClick = { onDeckClick(deck.id) },
                            onDelete = { viewModel.deleteDeck(deck.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Layers,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Currently, there is no deck available.",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DeckItem(
    deck: Deck,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = KinokiWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(KinokiBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    tint = KinokiDarkBlue
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = deck.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = KinokiDarkBlue,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${deck.cards.size} Cards",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.5f)
                )
            }
        }
    }
}