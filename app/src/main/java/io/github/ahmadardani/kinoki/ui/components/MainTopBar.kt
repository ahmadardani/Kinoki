package io.github.ahmadardani.kinoki.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import io.github.ahmadardani.kinoki.ui.theme.KinokiDarkBlue
import io.github.ahmadardani.kinoki.ui.theme.KinokiWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = KinokiWhite,
                fontWeight = FontWeight.SemiBold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = KinokiDarkBlue
        )
    )
}