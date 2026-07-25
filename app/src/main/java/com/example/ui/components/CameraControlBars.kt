package com.example.ui.components

import androidx.camera.core.ImageCapture
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.data.model.CameraLens
import com.example.data.model.CameraMode
import com.example.data.model.GridType

@Composable
fun TopQuickControlBar(
    flashMode: Int,
    timerSeconds: Int,
    gridType: GridType,
    showHistogram: Boolean,
    onToggleFlash: () -> Unit,
    onToggleTimer: () -> Unit,
    onToggleGrid: () -> Unit,
    onToggleHistogram: () -> Unit,
    onOpenDrawer: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bentoGreen = Color(0xFFB4C79F)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Drawer Icon Button
        IconButton(
            onClick = onOpenDrawer,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                .testTag("drawer_menu_button")
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Open Menu",
                tint = Color.White
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            // Flash
            IconButton(
                onClick = onToggleFlash,
                modifier = Modifier.size(36.dp).testTag("flash_toggle")
            ) {
                Icon(
                    imageVector = when (flashMode) {
                        ImageCapture.FLASH_MODE_ON -> Icons.Default.FlashOn
                        ImageCapture.FLASH_MODE_AUTO -> Icons.Default.FlashAuto
                        else -> Icons.Default.FlashOff
                    },
                    contentDescription = "Flash Mode",
                    tint = if (flashMode != ImageCapture.FLASH_MODE_OFF) Color(0xFFFFC107) else Color.White
                )
            }

            // Timer
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable { onToggleTimer() }
                    .testTag("timer_toggle"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (timerSeconds == 0) "OFF" else "${timerSeconds}s",
                    color = if (timerSeconds > 0) bentoGreen else Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Grid
            IconButton(
                onClick = onToggleGrid,
                modifier = Modifier.size(36.dp).testTag("grid_toggle")
            ) {
                Icon(
                    imageVector = Icons.Default.GridOn,
                    contentDescription = "Grid",
                    tint = if (gridType != GridType.NONE) bentoGreen else Color.White.copy(alpha = 0.6f)
                )
            }

            // Histogram toggle
            IconButton(
                onClick = onToggleHistogram,
                modifier = Modifier.size(36.dp).testTag("histogram_toggle")
            ) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Histogram",
                    tint = if (showHistogram) bentoGreen else Color.White.copy(alpha = 0.6f)
                )
            }
        }

        // Settings Button
        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                .testTag("settings_button")
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White
            )
        }
    }
}

@Composable
fun LensSelectorBar(
    currentLens: CameraLens,
    zoomRatio: Float,
    onSelectLens: (CameraLens) -> Unit,
    onSetZoom: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val bentoGreen = Color(0xFFB4C79F)

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.5f))
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        val zoomLevels = listOf(0.5f to CameraLens.ULTRA_WIDE, 1.0f to CameraLens.REAR, 2.0f to CameraLens.TELEPHOTO)
        zoomLevels.forEach { (zoom, lens) ->
            val isSelected = currentLens == lens || (currentLens == CameraLens.REAR && zoomRatio == zoom)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) bentoGreen else Color.Transparent)
                    .clickable {
                        onSelectLens(lens)
                        onSetZoom(zoom)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${zoom}x".replace(".0", ""),
                    color = if (isSelected) Color(0xFF0F0F0F) else Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CameraModeSelectorBar(
    currentMode: CameraMode,
    onSelectMode: (CameraMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val bentoGreen = Color(0xFFB4C79F)

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 24.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(CameraMode.values()) { mode ->
            val isSelected = mode == currentMode
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelectMode(mode) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("mode_${mode.name}")
            ) {
                Text(
                    text = mode.label.uppercase(),
                    color = if (isSelected) bentoGreen else Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    letterSpacing = 0.8.sp
                )
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(bentoGreen)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomShutterBar(
    isCapturing: Boolean,
    latestThumbnailUri: String?,
    onCaptureClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onSwitchCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bentoGreen = Color(0xFFB4C79F)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bento Gallery Thumbnail Button
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF1E1E1E))
                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                .clickable { onGalleryClick() }
                .testTag("gallery_thumbnail_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = "Open Gallery",
                tint = Color.White
            )
        }

        // Bento Shutter Button ring
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(4.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                .padding(4.dp)
                .clickable(enabled = !isCapturing) { onCaptureClick() }
                .testTag("shutter_button"),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(if (isCapturing) bentoGreen else Color.White)
            )
        }

        // Switch Camera Front/Rear
        IconButton(
            onClick = onSwitchCameraClick,
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E1E1E))
                .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                .testTag("switch_camera_button")
        ) {
            Icon(
                imageVector = Icons.Default.Cameraswitch,
                contentDescription = "Switch Camera",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
