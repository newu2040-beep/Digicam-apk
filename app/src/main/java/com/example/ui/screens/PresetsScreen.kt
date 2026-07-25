package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.data.db.PresetEntity
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val presets by viewModel.allPresets.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Film", "Retro", "Mood", "Portrait", "B&W", "Custom")

    val filteredPresets = if (selectedCategory == "All") {
        presets
    } else {
        presets.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PRESET LIBRARY", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("presets_back_button")) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Category Filter Chips
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
                edgePadding = 0.dp
            ) {
                categories.forEach { cat ->
                    Tab(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        text = { Text(cat, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredPresets) { preset ->
                    PresetItemCard(
                        preset = preset,
                        onApply = {
                            viewModel.applyPreset(preset)
                            onNavigateBack()
                        },
                        onDelete = {
                            viewModel.deletePreset(preset)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PresetItemCard(
    preset: PresetEntity,
    onApply: () -> Unit,
    onDelete: () -> Unit
) {
    val bentoGreen = Color(0xFFB4C79F)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF181818)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(preset.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = bentoGreen
                    ) {
                        Text(
                            text = preset.category.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F0F0F),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Text(
                    text = "Base: ${preset.filterName} • Grain: ${String.format("%.1f", preset.grain)}",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onApply,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = bentoGreen,
                        contentColor = Color(0xFF0F0F0F)
                    ),
                    modifier = Modifier.testTag("apply_preset_${preset.id}")
                ) {
                    Text("APPLY", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                if (preset.isCustom) {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Preset", tint = Color(0xFFFF6B6B))
                    }
                }
            }
        }
    }
}
