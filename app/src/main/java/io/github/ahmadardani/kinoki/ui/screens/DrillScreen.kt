package io.github.ahmadardani.kinoki.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.ahmadardani.kinoki.ui.components.CommonTopBar
import io.github.ahmadardani.kinoki.ui.theme.*
import io.github.ahmadardani.kinoki.ui.viewmodel.DrillStatus
import io.github.ahmadardani.kinoki.ui.viewmodel.StudyViewModel

@Composable
fun DrillScreen(
    deckId: String,
    viewModel: StudyViewModel,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(deckId) {
        viewModel.loadSession(deckId, isShuffle = true)
    }

    val cards by viewModel.cards.collectAsStateWithLifecycle()
    val index by viewModel.currentIndex.collectAsStateWithLifecycle()
    val isFinished by viewModel.isFinished.collectAsStateWithLifecycle()

    val userAnswer by viewModel.userDrillAnswer.collectAsStateWithLifecycle()
    val drillStatus by viewModel.drillStatus.collectAsStateWithLifecycle()

    val wrongCount by viewModel.wrongCardCount.collectAsStateWithLifecycle()

    var showHint by remember { mutableStateOf(false) }

    LaunchedEffect(index) { showHint = false }

    Scaffold(
        topBar = {
            CommonTopBar(title = "Drill Practice", onBackClick = onNavigateBack)
        }
    ) { innerPadding ->
        if (isFinished) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(KinokiBackground)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Drill Complete", style = MaterialTheme.typography.headlineSmall, color = KinokiDarkBlue)
                Spacer(Modifier.height(24.dp))

                val finalScore = cards.size - wrongCount

                Card(
                    colors = CardDefaults.cardColors(containerColor = KinokiWhite),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("$finalScore / ${cards.size}", style = MaterialTheme.typography.displayMedium, color = KinokiDarkBlue, fontWeight = FontWeight.Normal)
                        Text("Accuracy Score", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                        Spacer(modifier = Modifier.height(32.dp))
                        HorizontalDivider(color = KinokiInactiveIcon.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ScoreItem(label = "Correct", count = finalScore, color = Color(0xFF4CAF50))
                            ScoreItem(label = "Mistakes", count = wrongCount, color = Color(0xFFE57373))
                        }
                    }
                }

                Spacer(Modifier.height(48.dp))

                if (wrongCount > 0) {
                    Button(
                        onClick = { viewModel.retryWrongCards() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373), contentColor = KinokiWhite),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Review $wrongCount Mistakes")
                    }
                    Spacer(Modifier.height(16.dp))
                }

                Button(
                    onClick = { viewModel.loadSession(deckId, isShuffle = true) },
                    colors = ButtonDefaults.buttonColors(containerColor = KinokiWhite, contentColor = KinokiDarkBlue),
                    border = BorderStroke(1.dp, KinokiDarkBlue),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) { Text("Restart All") }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = KinokiDarkBlue, contentColor = KinokiWhite),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) { Text("Back to Home") }
            }
        } else if (cards.isNotEmpty()) {
            val card = cards[index]
            val targetAnswer = if (!card.center.isNullOrBlank()) card.center else card.back

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KinokiBackground)
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = KinokiWhite),
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = card.front,
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = KinokiDarkBlue
                            )

                            if (showHint) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Meaning: ${card.back}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                        }

                        IconButton(
                            onClick = { showHint = !showHint },
                            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Hint",
                                tint = if(showHint) KinokiDarkBlue else KinokiInactiveIcon
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                OutlinedTextField(
                    value = userAnswer,
                    onValueChange = { viewModel.updateDrillInput(it) },
                    label = { Text(if (!card.center.isNullOrBlank()) "Type the reading" else "Type the meaning") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = KinokiWhite,
                        unfocusedContainerColor = KinokiWhite,
                        focusedBorderColor = KinokiDarkBlue,
                        cursorColor = KinokiDarkBlue,
                        focusedTextColor = KinokiDarkBlue,
                        unfocusedTextColor = KinokiDarkBlue
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (userAnswer.isNotBlank() && drillStatus == DrillStatus.IDLE) {
                            viewModel.checkDrillAnswer()
                        }
                    }),
                    enabled = drillStatus != DrillStatus.CORRECT
                )

                Spacer(Modifier.height(16.dp))

                if (drillStatus == DrillStatus.WRONG) {
                    Text(
                        text = "Incorrect. Answer: $targetAnswer",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                } else if (drillStatus == DrillStatus.CORRECT) {
                    Text(
                        text = "Correct! Great job.",
                        color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = {
                        if (drillStatus == DrillStatus.IDLE) {
                            viewModel.checkDrillAnswer()
                        } else {
                            viewModel.nextCard()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (drillStatus == DrillStatus.CORRECT) Color(0xFF4CAF50) else KinokiDarkBlue,
                        contentColor = KinokiWhite
                    )
                ) {
                    if (drillStatus == DrillStatus.IDLE) {
                        Text("Check Answer")
                    } else if (drillStatus == DrillStatus.CORRECT) {
                        Icon(Icons.Default.Check, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Next Card")
                    } else {
                        Text("Continue")
                    }
                }
            }
        }
    }
}