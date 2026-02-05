package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.ahmadardani.kinoki.data.model.Card
import io.github.ahmadardani.kinoki.ui.components.CommonTopBar
import io.github.ahmadardani.kinoki.ui.theme.KinokiBackground
import io.github.ahmadardani.kinoki.ui.theme.KinokiDarkBlue
import io.github.ahmadardani.kinoki.ui.theme.KinokiWhite
import io.github.ahmadardani.kinoki.ui.viewmodel.HomeViewModel

@Composable
fun EditDeckScreen(
    deckId: String,
    viewModel: HomeViewModel,
    onNavigateBack: () -> Unit
) {
    val decks by viewModel.decks.collectAsStateWithLifecycle()
    val targetDeck = decks.find { it.id == deckId }

    var title by remember(targetDeck) { mutableStateOf(targetDeck?.title ?: "") }

    var showEditCardDialog by remember { mutableStateOf(false) }
    var cardToEdit by remember { mutableStateOf<Card?>(null) }

    fun openEditDialog(card: Card) {
        cardToEdit = card
        showEditCardDialog = true
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Edit Deck",
                onBackClick = onNavigateBack
            )
        },
        containerColor = KinokiBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Deck Name",
                    style = MaterialTheme.typography.bodyMedium,
                    color = KinokiDarkBlue,
                    fontWeight = FontWeight.Normal
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Deck Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = KinokiDarkBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = KinokiWhite,
                        unfocusedContainerColor = KinokiWhite,
                        cursorColor = KinokiDarkBlue,
                        focusedTextColor = KinokiDarkBlue,
                        unfocusedTextColor = KinokiDarkBlue
                    )
                )
            }

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            Text(
                text = "Cards List (${targetDeck?.cards?.size ?: 0})",
                style = MaterialTheme.typography.titleMedium,
                color = KinokiDarkBlue,
                fontWeight = FontWeight.Normal
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                val cardList = targetDeck?.cards ?: emptyList()

                if (cardList.isEmpty()) {
                    item {
                        Text(
                            text = "No cards in this deck yet.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                items(cardList) { card ->
                    EditCardItem(
                        card = card,
                        onEditClick = { openEditDialog(card) },
                        onDeleteClick = { viewModel.removeCard(deckId, card.id) }
                    )
                }
            }

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.updateDeckTitle(deckId, title)
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = KinokiDarkBlue,
                    contentColor = KinokiWhite,
                    disabledContainerColor = Color.Gray
                ),
                enabled = title.isNotBlank()
            ) {
                Text(
                    text = "Save Changes",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }

    if (showEditCardDialog && cardToEdit != null) {
        EditCardDialog(
            card = cardToEdit!!,
            onDismiss = { showEditCardDialog = false },
            onSave = { updatedFront, updatedBack, updatedCenter ->
                val newCard = cardToEdit!!.copy(
                    front = updatedFront,
                    back = updatedBack,
                    center = updatedCenter
                )
                viewModel.editCard(deckId, newCard)
                showEditCardDialog = false
            }
        )
    }
}

@Composable
fun EditCardItem(
    card: Card,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    androidx.compose.material3.Card(
        colors = CardDefaults.cardColors(containerColor = KinokiWhite),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() }
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
                    text = card.front,
                    style = MaterialTheme.typography.bodyLarge,
                    color = KinokiDarkBlue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = card.back,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Card",
                    tint = Color.Red.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun EditCardDialog(
    card: Card,
    onDismiss: () -> Unit,
    onSave: (String, String, String?) -> Unit
) {
    var front by remember { mutableStateOf(card.front) }
    var back by remember { mutableStateOf(card.back) }
    var center by remember { mutableStateOf(card.center ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = KinokiWhite,
        title = {
            Text(
                text = "Edit Card",
                color = KinokiDarkBlue,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Normal
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = front,
                    onValueChange = { front = it },
                    label = { Text("Front") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = KinokiWhite,
                        unfocusedContainerColor = KinokiWhite,
                        focusedBorderColor = KinokiDarkBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = KinokiDarkBlue,
                        unfocusedTextColor = KinokiDarkBlue,
                        cursorColor = KinokiDarkBlue
                    )
                )

                OutlinedTextField(
                    value = center,
                    onValueChange = { center = it },
                    label = { Text("Center (Optional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = KinokiWhite,
                        unfocusedContainerColor = KinokiWhite,
                        focusedBorderColor = KinokiDarkBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = KinokiDarkBlue,
                        unfocusedTextColor = KinokiDarkBlue,
                        cursorColor = KinokiDarkBlue
                    )
                )

                OutlinedTextField(
                    value = back,
                    onValueChange = { back = it },
                    label = { Text("Back") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = KinokiWhite,
                        unfocusedContainerColor = KinokiWhite,
                        focusedBorderColor = KinokiDarkBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = KinokiDarkBlue,
                        unfocusedTextColor = KinokiDarkBlue,
                        cursorColor = KinokiDarkBlue
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(front, back, if(center.isBlank()) null else center)
                },
                colors = ButtonDefaults.buttonColors(containerColor = KinokiDarkBlue)
            ) {
                Text("Save", color = KinokiWhite)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}