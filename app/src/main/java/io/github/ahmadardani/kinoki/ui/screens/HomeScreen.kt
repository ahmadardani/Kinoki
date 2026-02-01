package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.ahmadardani.kinoki.ui.components.MainBottomBar
import io.github.ahmadardani.kinoki.ui.components.MainTopBar
import io.github.ahmadardani.kinoki.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onAddDeckClick: () -> Unit
) {
    Scaffold(
        topBar = {
            MainTopBar(title = "Home")
        },
        bottomBar = {
            MainBottomBar(
                currentScreen = "home",
                onNavigateToHome = {},
                onNavigateToSettings = onNavigateToSettings
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDeckClick,
                containerColor = KinokiDarkBlue,
                contentColor = KinokiWhite,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add New Deck"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(KinokiBackground)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
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
                    text = "Currently There is no deck available",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}