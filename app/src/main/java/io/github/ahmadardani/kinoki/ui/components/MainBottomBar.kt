package io.github.ahmadardani.kinoki.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import io.github.ahmadardani.kinoki.ui.theme.KinokiBottomBar
import io.github.ahmadardani.kinoki.ui.theme.KinokiDarkBlue
import io.github.ahmadardani.kinoki.ui.theme.KinokiInactiveIcon

@Composable
fun MainBottomBar(
    currentScreen: String,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    NavigationBar(
        containerColor = KinokiBottomBar
    ) {
        NavigationBarItem(
            selected = currentScreen == "home",
            onClick = onNavigateToHome,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (currentScreen == "home") KinokiDarkBlue else KinokiInactiveIcon
                )
            },
            label = {
                Text(
                    text = "Home",
                    color = if (currentScreen == "home") KinokiDarkBlue else KinokiInactiveIcon,
                    fontWeight = if (currentScreen == "home") FontWeight.SemiBold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = KinokiBottomBar
            )
        )

        NavigationBarItem(
            selected = currentScreen == "settings",
            onClick = onNavigateToSettings,
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = if (currentScreen == "settings") KinokiDarkBlue else KinokiInactiveIcon
                )
            },
            label = {
                Text(
                    text = "Settings",
                    color = if (currentScreen == "settings") KinokiDarkBlue else KinokiInactiveIcon,
                    fontWeight = if (currentScreen == "settings") FontWeight.SemiBold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = KinokiBottomBar
            )
        )
    }
}