package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.MeteringMode
import com.example.data.model.ProSettings

@Composable
fun ProControlsPanel(
    proSettings: ProSettings,
    onUpdateSettings: (ProSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    val bentoGreen = Color(0xFFB4C79F)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(Color(0xFF141414))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BENTO PRO CONTROLS",
                color = bentoGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "RAW: ${if (proSettings.rawEnabled) "ON" else "OFF"}",
                color = if (proSettings.rawEnabled) Color(0xFF0F0F0F) else Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (proSettings.rawEnabled) bentoGreen else Color(0xFF262626))
                    .clickable { onUpdateSettings(proSettings.copy(rawEnabled = !proSettings.rawEnabled)) }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .testTag("raw_toggle")
            )
        }

        // ISO Selection
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("ISO: ${proSettings.iso}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            val isoValues = listOf(100, 200, 400, 800, 1600, 3200)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(isoValues) { iso ->
                    val selected = proSettings.iso == iso
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) bentoGreen else Color(0xFF242424))
                            .clickable { onUpdateSettings(proSettings.copy(iso = iso)) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("iso_$iso")
                    ) {
                        Text(
                            text = "$iso",
                            color = if (selected) Color(0xFF0F0F0F) else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Shutter Speed Selection
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Shutter: ${proSettings.shutterSpeed}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            val shutterValues = listOf("Auto", "1/2000", "1/1000", "1/500", "1/250", "1/125", "1/60", "1/30", "1s")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(shutterValues) { shutter ->
                    val selected = proSettings.shutterSpeed == shutter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) bentoGreen else Color(0xFF242424))
                            .clickable { onUpdateSettings(proSettings.copy(shutterSpeed = shutter)) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("shutter_$shutter")
                    ) {
                        Text(
                            text = shutter,
                            color = if (selected) Color(0xFF0F0F0F) else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Exposure Compensation Slider (-6 to +6 EV)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("EV Compensation: ${if (proSettings.exposureCompensation > 0) "+" else ""}${proSettings.exposureCompensation} EV", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Slider(
                value = proSettings.exposureCompensation.toFloat(),
                onValueChange = { onUpdateSettings(proSettings.copy(exposureCompensation = it.toInt())) },
                valueRange = -6f..6f,
                steps = 11,
                colors = SliderDefaults.colors(
                    thumbColor = bentoGreen,
                    activeTrackColor = bentoGreen,
                    inactiveTrackColor = Color(0xFF333333)
                ),
                modifier = Modifier.testTag("ev_slider")
            )
        }

        // White Balance Presets
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("White Balance: ${proSettings.whiteBalance}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            val wbValues = listOf("Auto", "Daylight", "Cloudy", "Tungsten", "Fluorescent")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(wbValues) { wb ->
                    val selected = proSettings.whiteBalance == wb
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) bentoGreen else Color(0xFF242424))
                            .clickable { onUpdateSettings(proSettings.copy(whiteBalance = wb)) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = wb,
                            color = if (selected) Color(0xFF0F0F0F) else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Metering Modes
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Metering Mode", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MeteringMode.values().forEach { mode ->
                    val selected = proSettings.meteringMode == mode
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) bentoGreen else Color(0xFF242424))
                            .clickable { onUpdateSettings(proSettings.copy(meteringMode = mode)) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = mode.label,
                            color = if (selected) Color(0xFF0F0F0F) else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
