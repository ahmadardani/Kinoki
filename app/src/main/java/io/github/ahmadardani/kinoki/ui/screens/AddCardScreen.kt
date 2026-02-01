package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.ahmadardani.kinoki.ui.components.CommonTopBar
import io.github.ahmadardani.kinoki.ui.theme.*

data class TempCard(
    val front: String,
    val back: String,
    val center: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    onNavigateBack: () -> Unit,
    onAddCard: (String, String, String?) -> Unit
) {
    var frontText by remember { mutableStateOf("") }
    var centerText by remember { mutableStateOf("") }
    var backText by remember { mutableStateOf("") }
    var isCenterEnabled by remember { mutableStateOf(false) }

    val addedCards = remember { mutableStateListOf<TempCard>() }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CommonTopBar(
                title = if (addedCards.isEmpty()) "Add Card" else "Add Card (${addedCards.size})",
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "View List",
                            tint = KinokiWhite
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(KinokiBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            InputLabel(text = "Front")
            KinokiTextField(value = frontText, onValueChange = { frontText = it }, placeholder = "Front side content")

            Spacer(modifier = Modifier.height(16.dp))

            if (isCenterEnabled) {
                InputLabel(text = "Center")
                KinokiTextField(value = centerText, onValueChange = { centerText = it }, placeholder = "Center content")
                Spacer(modifier = Modifier.height(16.dp))
            }

            InputLabel(text = "Back")
            KinokiTextField(value = backText, onValueChange = { backText = it }, placeholder = "Back side content")

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Add Center", style = MaterialTheme.typography.titleMedium, color = KinokiDarkBlue, fontWeight = FontWeight.SemiBold)
                Switch(
                    checked = isCenterEnabled,
                    onCheckedChange = { isCenterEnabled = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = KinokiWhite, checkedTrackColor = KinokiDarkBlue, uncheckedThumbColor = KinokiInactiveIcon, uncheckedTrackColor = KinokiWhite)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(50),
                    border = null,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = KinokiDarkBlue, containerColor = Color.Transparent),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) { Text("Finish", fontWeight = FontWeight.Bold) }

                Button(
                    onClick = {
                        val centerContent = if (isCenterEnabled) centerText else null
                        onAddCard(frontText, backText, centerContent)

                        addedCards.add(0, TempCard(frontText, backText, centerContent))

                        frontText = ""
                        centerText = ""
                        backText = ""
                    },
                    enabled = frontText.isNotBlank() && backText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = KinokiDarkBlue, contentColor = KinokiWhite, disabledContainerColor = KinokiInactiveIcon),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) { Text("Add Card", fontWeight = FontWeight.Bold) }
            }

        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = KinokiBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "Cards in this Session (${addedCards.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = KinokiDarkBlue,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn {
                        itemsIndexed(addedCards) { index, card ->
                            AddedCardItem(
                                card = card,
                                number = addedCards.size - index,
                                onDelete = {
                                    addedCards.removeAt(index)
                                }
                            )
                        }
                    }

                    if (addedCards.isEmpty()) {
                        Text(
                            text = "No cards added yet.",
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}


@Composable
fun AddedCardItem(
    card: TempCard,
    number: Int,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(24.dp).background(color = KinokiDarkBlue, shape = CircleShape)) {
                Text(text = number.toString(), color = KinokiWhite, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = card.front,
                style = MaterialTheme.typography.bodyLarge,
                color = KinokiDarkBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Card",
                    tint = Color.Red.copy(alpha = 0.6f)
                )
            }
        }

        val subText = buildString {
            if (!card.center.isNullOrBlank()) append("${card.center} • ")
            append(card.back)
        }

        Text(text = subText, style = MaterialTheme.typography.bodyMedium, color = Color.Gray, modifier = Modifier.padding(start = 36.dp, top = 0.dp, bottom = 8.dp))

        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f), modifier = Modifier.padding(start = 36.dp))
    }
}

@Composable
fun InputLabel(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium, color = KinokiDarkBlue, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
fun KinokiTextField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange, placeholder = { Text(placeholder, color = Color.Gray) },
        singleLine = false, maxLines = 3, shape = RoundedCornerShape(50),
        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = KinokiDarkBlue, unfocusedTextColor = KinokiDarkBlue, focusedContainerColor = KinokiWhite, unfocusedContainerColor = KinokiWhite, focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent, cursorColor = KinokiDarkBlue),
        modifier = Modifier.fillMaxWidth()
    )
}