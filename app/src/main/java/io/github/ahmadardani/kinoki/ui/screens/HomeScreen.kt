package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var isExpanded by remember { mutableStateOf(false) }

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
                    text = "Currently, there is no deck available.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}