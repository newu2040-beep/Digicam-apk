package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.db.MediaItemEntity
import com.example.ui.viewmodel.MainViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailScreen(
    media: MediaItemEntity,
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onNavigateEditor: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showExifSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(media.title, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(media.id, media.isFavorite) }) {
                        Icon(
                            imageVector = if (media.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (media.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { showExifSheet = true }) {
                        Icon(Icons.Default.Info, contentDescription = "EXIF Details")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateEditor, modifier = Modifier.testTag("edit_photo_button")) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    if (media.isTrash) {
                        IconButton(onClick = {
                            viewModel.restoreFromTrash(media.id)
                            onNavigateBack()
                        }) {
                            Icon(Icons.Default.Restore, contentDescription = "Restore")
                        }
                        IconButton(onClick = {
                            viewModel.permanentDelete(media)
                            onNavigateBack()
                        }) {
                            Icon(Icons.Default.DeleteForever, contentDescription = "Permanent Delete", tint = Color.Red)
                        }
                    } else {
                        IconButton(onClick = {
                            viewModel.moveToTrash(media.id)
                            onNavigateBack()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(File(media.filePath))
                    .crossfade(true)
                    .build(),
                contentDescription = media.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    if (showExifSheet) {
        ModalBottomSheet(onDismissRequest = { showExifSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("EXIF METADATA", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)

                ExifRow("Camera Model", "DIGICAM Compact")
                ExifRow("Filter Preset", media.filterName)
                ExifRow("ISO Speed", "ISO ${media.iso}")
                ExifRow("Shutter Speed", media.shutterSpeed)
                ExifRow("Aperture", media.aperture)
                ExifRow("Resolution", "${media.width} x ${media.height}")
                ExifRow("File Size", "${media.fileSize / 1024} KB")
                ExifRow("Date Taken", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date(media.timestamp)))
            }
        }
    }
}

@Composable
private fun ExifRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}
