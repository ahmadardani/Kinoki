package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.ahmadardani.kinoki.ui.components.MainBottomBar
import io.github.ahmadardani.kinoki.ui.components.MainTopBar
import io.github.ahmadardani.kinoki.ui.theme.*

@Composable
fun SettingsScreen(
    onNavigateToHome: () -> Unit
) {
    Scaffold(
        topBar = {
            MainTopBar(title = "Settings")
        },
        bottomBar = {
            MainBottomBar(
                currentScreen = "settings",
                onNavigateToHome = onNavigateToHome,
                onNavigateToSettings = { }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(KinokiBackground)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SettingsItem(
                icon = Icons.Default.Palette,
                title = "Appearance",
                onClick = {}
            )

            SettingsItem(
                icon = Icons.Default.Info,
                title = "About",
                onClick = {}
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = KinokiWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),

        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = KinokiDarkBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = KinokiDarkBlue,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}