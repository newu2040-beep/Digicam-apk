package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GridType

@Composable
fun GridOverlay(
    gridType: GridType,
    modifier: Modifier = Modifier
) {
    if (gridType == GridType.NONE) return

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val strokeWidth = 1.dp.toPx()
        val gridColor = Color.White.copy(alpha = 0.4f)

        when (gridType) {
            GridType.RULE_OF_THIRDS -> {
                // Vertical lines
                drawLine(gridColor, Offset(width / 3f, 0f), Offset(width / 3f, height), strokeWidth)
                drawLine(gridColor, Offset(2 * width / 3f, 0f), Offset(2 * width / 3f, height), strokeWidth)
                // Horizontal lines
                drawLine(gridColor, Offset(0f, height / 3f), Offset(width, height / 3f), strokeWidth)
                drawLine(gridColor, Offset(0f, 2 * height / 3f), Offset(width, 2 * height / 3f), strokeWidth)
            }
            GridType.GOLDEN_RATIO -> {
                val phi = 0.382f
                drawLine(gridColor, Offset(width * phi, 0f), Offset(width * phi, height), strokeWidth)
                drawLine(gridColor, Offset(width * (1 - phi), 0f), Offset(width * (1 - phi), height), strokeWidth)
                drawLine(gridColor, Offset(0f, height * phi), Offset(width, height * phi), strokeWidth)
                drawLine(gridColor, Offset(0f, height * (1 - phi)), Offset(width, height * (1 - phi)), strokeWidth)
            }
            GridType.SQUARE -> {
                val squareSize = minOf(width, height)
                val left = (width - squareSize) / 2f
                val top = (height - squareSize) / 2f
                drawRect(
                    color = Color.Black.copy(alpha = 0.5f),
                    topLeft = Offset(0f, 0f),
                    size = Size(width, top)
                )
                drawRect(
                    color = Color.Black.copy(alpha = 0.5f),
                    topLeft = Offset(0f, top + squareSize),
                    size = Size(width, height - (top + squareSize))
                )
                drawRect(
                    color = gridColor,
                    topLeft = Offset(left, top),
                    size = Size(squareSize, squareSize),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                )
            }
            GridType.NONE -> {}
        }
    }
}

@Composable
fun LevelIndicatorOverlay(
    roll: Float,
    modifier: Modifier = Modifier
) {
    val isLevel = kotlin.math.abs(roll) < 1.5f
    val lineColor = if (isLevel) Color(0xFF4CAF50) else Color.White.copy(alpha = 0.8f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.rotate(-roll)
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(2.dp)
                    .background(lineColor)
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(lineColor)
            )
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(2.dp)
                    .background(lineColor)
            )
        }
    }
}

@Composable
fun HistogramView(
    modifier: Modifier = Modifier
) {
    // Generates a smooth simulated live RGB histogram
    val bins = remember { List(32) { (10..100).random().toFloat() } }

    Box(
        modifier = modifier
            .width(120.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(4.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barWidth = size.width / bins.size
            val maxVal = bins.maxOrNull() ?: 100f

            bins.forEachIndexed { index, valBin ->
                val barHeight = (valBin / maxVal) * size.height
                drawRect(
                    color = Color.White.copy(alpha = 0.7f),
                    topLeft = Offset(index * barWidth, size.height - barHeight),
                    size = Size(barWidth - 1f, barHeight)
                )
            }
        }
    }
}

@Composable
fun FocusRingOverlay(
    focusPoint: Offset?,
    isLocked: Boolean,
    modifier: Modifier = Modifier
) {
    if (focusPoint == null) return

    val transition = rememberInfiniteTransition(label = "focus_pulse")
    val scale by transition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val ringColor = if (isLocked) Color(0xFFFFC107) else Color(0xFFA8E6CF)

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .offset(
                    x = (focusPoint.x - 36).dp,
                    y = (focusPoint.y - 36).dp
                )
                .size((72 * if (isLocked) 1.0f else scale).dp)
                .border(2.dp, ringColor, CircleShape)
                .padding(4.dp)
        ) {
            if (isLocked) {
                Text(
                    text = "AE/AF LOCK",
                    color = ringColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }
    }
}

@Composable
fun BentoHudOverlay(
    activeFilterName: String,
    iso: Int,
    ev: Float,
    wb: String,
    modifier: Modifier = Modifier
) {
    val bentoGreen = Color(0xFFB4C79F)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Active Filter Tag Pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(bentoGreen)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = activeFilterName.uppercase(),
                color = Color(0xFF0F0F0F),
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.8.sp
            )
        }

        // Bento Info Badge Card
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            // EV
            Text(
                text = "EV ${if (ev >= 0) "+${ev}" else "$ev"}",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Text(text = "•", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)

            // ISO
            Text(
                text = "ISO $iso",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Text(text = "•", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)

            // WB
            Text(
                text = wb,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            // Ready Status Dot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(bentoGreen.copy(alpha = 0.2f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(bentoGreen)
                )
                Text(
                    text = "READY",
                    color = bentoGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

