package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.db.MediaItemEntity
import com.example.data.model.PhotoEditState
import com.example.ui.viewmodel.MainViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoEditorScreen(
    media: MediaItemEntity?,
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (media == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No photo selected for editing")
        }
        return
    }

    var editState by remember { mutableStateOf(PhotoEditState()) }
    var isComparingOriginal by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NON-DESTRUCTIVE EDITOR", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("editor_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = { onNavigateBack() },
                        modifier = Modifier.padding(end = 8.dp).testTag("save_edit_button")
                    ) {
                        Text("SAVE")
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
        ) {
            // Photo View Canvas with Hold to Compare
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isComparingOriginal = true
                                tryAwaitRelease()
                                isComparingOriginal = false
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(File(media.filePath))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Editing Canvas",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                if (isComparingOriginal) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = "ORIGINAL PHOTO (BEFORE)",
                            color = Color.Yellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Adjustment Sliders
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("TONE & COLOR ADJUSTMENTS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)

                EditorSlider("Exposure", editState.exposure, -1f..1f) { editState = editState.copy(exposure = it) }
                EditorSlider("Contrast", editState.contrast, -0.5f..0.5f) { editState = editState.copy(contrast = it) }
                EditorSlider("Brightness", editState.brightness, -0.5f..0.5f) { editState = editState.copy(brightness = it) }
                EditorSlider("Highlights", editState.highlights, -0.5f..0.5f) { editState = editState.copy(highlights = it) }
                EditorSlider("Shadows", editState.shadows, -0.5f..0.5f) { editState = editState.copy(shadows = it) }
                EditorSlider("Temperature", editState.temperature, -0.5f..0.5f) { editState = editState.copy(temperature = it) }
                EditorSlider("Tint", editState.tint, -0.5f..0.5f) { editState = editState.copy(tint = it) }
                EditorSlider("Saturation", editState.saturation, -0.5f..0.5f) { editState = editState.copy(saturation = it) }
                EditorSlider("Vignette", editState.vignette, 0f..1f) { editState = editState.copy(vignette = it) }
                EditorSlider("Film Grain", editState.grain, 0f..1f) { editState = editState.copy(grain = it) }
            }
        }
    }
}

@Composable
private fun EditorSlider(
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
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(String.format("%.2f", value), fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange
        )
    }
}
