package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Tune
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
import com.example.data.model.FilterParams
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveFiltersScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filterParams by viewModel.filterParams.collectAsState()
    var showSaveDialog by remember { mutableStateOf(false) }
    var newPresetName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Custom") }

    val filterList = remember {
        listOf(
            "Classic Film", "Vintage Film", "Retro Warm", "Retro Cool", "Dreamy", "Soft Glow",
            "Golden Hour", "Moody", "Cinematic", "Coffee", "Matte", "Minimal", "Natural",
            "Portrait", "Street", "Travel", "Black & White", "Monochrome", "Sepia", "Pastel",
            "Disposable Camera", "Instant Film", "Film Grain", "Dust", "Bloom", "Soft Contrast", "HDR Style"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LIVE FILTERS & COLOR GRAD", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("filter_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showSaveDialog = true },
                        modifier = Modifier.testTag("save_preset_button")
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Save Preset", tint = MaterialTheme.colorScheme.primary)
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Filter Base Picker
            Text("SELECT FILTER STYLE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filterList) { name ->
                    val isSelected = filterParams.filterName == name
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { viewModel.updateFilterParams(filterParams.copy(filterName = name)) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("filter_$name")
                    ) {
                        Text(
                            text = name,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            HorizontalDivider()

            Text("FINE TUNE PARAMETERS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

            // Parameter Sliders
            FilterSliderItem("Intensity", filterParams.intensity, 0f..1f) {
                viewModel.updateFilterParams(filterParams.copy(intensity = it))
            }

            FilterSliderItem("Film Grain", filterParams.grain, 0f..1f) {
                viewModel.updateFilterParams(filterParams.copy(grain = it))
            }

            FilterSliderItem("Fade / Matte", filterParams.fade, 0f..0.5f) {
                viewModel.updateFilterParams(filterParams.copy(fade = it))
            }

            FilterSliderItem("Temperature", filterParams.temperature, -0.5f..0.5f) {
                viewModel.updateFilterParams(filterParams.copy(temperature = it))
            }

            FilterSliderItem("Contrast", filterParams.contrast, -0.5f..0.5f) {
                viewModel.updateFilterParams(filterParams.copy(contrast = it))
            }

            FilterSliderItem("Saturation", filterParams.saturation, -0.5f..0.5f) {
                viewModel.updateFilterParams(filterParams.copy(saturation = it))
            }

            FilterSliderItem("Tint (Green / Magenta)", filterParams.tint, -0.5f..0.5f) {
                viewModel.updateFilterParams(filterParams.copy(tint = it))
            }

            FilterSliderItem("Shadows Lift", filterParams.shadows, 0f..0.4f) {
                viewModel.updateFilterParams(filterParams.copy(shadows = it))
            }

            Button(
                onClick = { showSaveDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp))
            ) {
                Icon(Icons.Default.Bookmark, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("SAVE AS CUSTOM PRESET")
            }
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Custom Preset") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newPresetName,
                        onValueChange = { newPresetName = it },
                        label = { Text("Preset Name") },
                        modifier = Modifier.fillMaxWidth().testTag("preset_name_input")
                    )
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = { selectedCategory = it },
                        label = { Text("Category (Film, Retro, Mood, Custom)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newPresetName.isNotBlank()) {
                            viewModel.saveCustomPreset(newPresetName, selectedCategory)
                            showSaveDialog = false
                            newPresetName = ""
                        }
                    },
                    modifier = Modifier.testTag("confirm_save_preset_button")
                ) {
                    Text("SAVE")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("CANCEL")
                }
            }
        )
    }
}

@Composable
private fun FilterSliderItem(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(String.format("%.2f", value), fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange
        )
    }
}
