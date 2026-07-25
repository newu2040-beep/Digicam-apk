package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppTheme
import com.example.data.model.GridType
import com.example.ui.theme.getThemePalette
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTheme by viewModel.appTheme.collectAsState()
    val gridType by viewModel.gridType.collectAsState()
    val showHistogram by viewModel.showHistogram.collectAsState()
    val showLevel by viewModel.showLevel.collectAsState()
    val shutterSound by viewModel.shutterSound.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SETTINGS & PREFERENCES", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("settings_back_button")) {
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
            // Section 1: Themes
            Text("PASTEL THEMES (14 OPTIONS)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppTheme.values().forEach { theme ->
                    val isSelected = theme == currentTheme
                    val palette = getThemePalette(theme, false)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { viewModel.setAppTheme(theme) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(palette.primary)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(theme.displayName, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
                        }

                        if (isSelected) {
                            Text("ACTIVE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            HorizontalDivider()

            // Section 2: Camera Overlays & Hardware
            Text("CAMERA VIEWPORT & HARDWARE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

            SettingsToggleRow("Live Digital Level Gauge", showLevel) { viewModel.toggleShowLevel() }
            SettingsToggleRow("Real-time Histogram Chart", showHistogram) { viewModel.toggleHistogram() }
            SettingsToggleRow("Shutter Sound Feedback", shutterSound) { viewModel.toggleShutterSound() }

            HorizontalDivider()

            // Section 3: Experimental Flags
            Text("EXPERIMENTAL FEATURE FLAGS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

            SettingsToggleRow("Retro Compact Viewfinder UI", false) {}
            SettingsToggleRow("AI Auto Color Processing", true) {}
            SettingsToggleRow("Camera Sensor Diagnostics", false) {}
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
