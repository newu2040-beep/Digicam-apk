package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.PresetEntity
import com.example.data.model.FilterParams

@Composable
fun PresetChipBar(
    presets: List<PresetEntity>,
    activeFilterName: String,
    onSelectPreset: (PresetEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(presets) { preset ->
            val isSelected = preset.name == activeFilterName || preset.filterName == activeFilterName
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.6f))
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onSelectPreset(preset) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("preset_chip_${preset.id}"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = preset.name,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.White,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(
                        text = preset.category,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else Color.Gray,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}
