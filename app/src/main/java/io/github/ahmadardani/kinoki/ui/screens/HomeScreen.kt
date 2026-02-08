package io.github.ahmadardani.kinoki.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToSettings: () -> Unit,
    onAddDeckClick: () -> Unit,
    onDeckClick: (String, Int, String) -> Unit,
    onEditDeck: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val decks by viewModel.decks.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var deckToExport by remember { mutableStateOf<Deck?>(null) }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val content = context.contentResolver.openInputStream(it)?.bufferedReader().use { reader ->
                    reader?.readText()
                }
                content?.let { json -> viewModel.importDeck(json) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { saveUri ->
            deckToExport?.let { deck ->
                scope.launch(Dispatchers.IO) {
                    try {
                        val jsonString = Json { prettyPrint = true }.encodeToString(deck)

                        context.contentResolver.openOutputStream(saveUri)?.use { outputStream ->
                            outputStream.write(jsonString.toByteArray())
                        }

                        snackbarHostState.showSnackbar("Deck exported successfully.")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        snackbarHostState.showSnackbar("Failed to export deck.")
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadDecks()
    }

    Scaffold(
        topBar = { MainTopBar(title = "Home") },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                        onClick = {
                            isExpanded = false
                            importLauncher.launch("application/json")
                        },
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
                            onClick = { onDeckClick(deck.id, deck.cards.size, deck.title) },
                            onEdit = { onEditDeck(deck.id) },
                            onDelete = { viewModel.deleteDeck(deck.id) },
                            onExport = {
                                deckToExport = deck
                                exportLauncher.launch("${deck.title}.json")
                            }
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
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onExport: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = KinokiWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = deck.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = KinokiDarkBlue,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${deck.cards.size} Cards",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = Color.Gray
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    containerColor = KinokiWhite,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 4.dp
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Deck", color = KinokiDarkBlue) },
                        onClick = {
                            showMenu = false
                            onEdit()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = null,
                                tint = KinokiDarkBlue
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Export Deck", color = KinokiDarkBlue) },
                        onClick = {
                            showMenu = false
                            onExport()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = KinokiDarkBlue
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Delete Deck", color = Color.Red) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    )
                }
            }
        }
    }
}