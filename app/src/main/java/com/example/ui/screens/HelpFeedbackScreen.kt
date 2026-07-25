package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpFeedbackScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var feedbackText by remember { mutableStateOf("") }
    var feedbackSent by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HELP & FEEDBACK", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("help_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("FREQUENTLY ASKED QUESTIONS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

            FaqCard("How do I activate Pro Mode?", "Tap the PRO mode chip on the bottom mode slider above the shutter button to reveal ISO, Shutter Speed, White Balance, and EV compensation controls.")
            FaqCard("How do I lock focus and exposure?", "Long press anywhere on the camera viewfinder surface to activate AE/AF lock.")
            FaqCard("Where are saved presets stored?", "Custom presets created via Live Filters are saved locally in the Room Database and accessible in the Presets Library.")

            HorizontalDivider()

            Text("SEND FEEDBACK TO RAHUL SHAH", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

            OutlinedTextField(
                value = feedbackText,
                onValueChange = { feedbackText = it },
                label = { Text("Your comments or feature requests") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("feedback_input")
            )

            Button(
                onClick = {
                    if (feedbackText.isNotBlank()) {
                        feedbackSent = true
                        feedbackText = ""
                    }
                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("send_feedback_button")
            ) {
                Text(if (feedbackSent) "FEEDBACK SENT! THANK YOU" else "SUBMIT FEEDBACK")
            }
        }
    }
}

@Composable
private fun FaqCard(question: String, answer: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(question, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(answer, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
