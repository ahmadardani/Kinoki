package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.ahmadardani.kinoki.ui.components.CommonTopBar
import io.github.ahmadardani.kinoki.ui.theme.*

@Composable
fun CreateDeckScreen(
    onNavigateBack: () -> Unit,
    onCreateDeck: (String) -> Unit
) {

    var deckName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Create a New Deck",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(KinokiBackground)
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Deck Name",
                style = MaterialTheme.typography.titleMedium,
                color = KinokiDarkBlue,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = deckName,
                onValueChange = { deckName = it },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = KinokiDarkBlue,
                    unfocusedTextColor = KinokiDarkBlue,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = KinokiWhite,
                    unfocusedContainerColor = KinokiWhite
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (deckName.isNotBlank()) {
                        onCreateDeck(deckName)
                    }
                },
                enabled = deckName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = KinokiDarkBlue,
                    contentColor = KinokiWhite,
                    disabledContainerColor = KinokiInactiveIcon
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Create Deck",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}