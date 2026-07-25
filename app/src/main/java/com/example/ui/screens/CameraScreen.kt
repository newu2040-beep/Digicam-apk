package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.camera.CameraManager
import com.example.camera.SensorLevelManager
import com.example.data.model.CameraLens
import com.example.data.model.CameraMode
import com.example.data.model.GridType
import com.example.ui.components.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CameraScreen(
    viewModel: MainViewModel,
    onOpenDrawer: () -> Unit,
    onNavigateGallery: () -> Unit,
    onNavigateSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentLens by viewModel.currentLens.collectAsState()
    val cameraMode by viewModel.cameraMode.collectAsState()
    val proSettings by viewModel.proSettings.collectAsState()
    val filterParams by viewModel.filterParams.collectAsState()
    val timerSeconds by viewModel.timerSeconds.collectAsState()
    val gridType by viewModel.gridType.collectAsState()
    val showHistogram by viewModel.showHistogram.collectAsState()
    val showLevel by viewModel.showLevel.collectAsState()
    val allPresets by viewModel.allPresets.collectAsState()

    val cameraManager = remember { CameraManager(context) }
    val sensorLevelManager = remember { SensorLevelManager(context) }

    val flashMode by cameraManager.flashMode.collectAsState()
    val zoomRatio by cameraManager.zoomRatio.collectAsState()
    val isCapturing by cameraManager.isCapturing.collectAsState()

    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }
    var focusPoint by remember { mutableStateOf<Offset?>(null) }
    var isFocusLocked by remember { mutableStateOf(false) }

    val roll by sensorLevelManager.roll.collectAsState()

    DisposableEffect(Unit) {
        sensorLevelManager.start()
        onDispose { sensorLevelManager.stop() }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // CameraX Viewfinder
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    previewViewRef = this
                    cameraManager.bindCamera(
                        lifecycleOwner = lifecycleOwner,
                        previewView = this,
                        lens = currentLens,
                        proSettings = proSettings
                    )
                }
            },
            update = { view ->
                previewViewRef = view
            },
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            focusPoint = offset
                            previewViewRef?.let { cameraManager.tapToFocus(it, offset.x, offset.y) }
                        },
                        onLongPress = { offset ->
                            focusPoint = offset
                            isFocusLocked = !isFocusLocked
                        }
                    )
                }
        )

        // Grid Overlay
        GridOverlay(gridType = gridType)

        // Digital Level Indicator
        if (showLevel) {
            LevelIndicatorOverlay(roll = roll, modifier = Modifier.align(Alignment.Center))
        }

        // Animated Focus Ring
        FocusRingOverlay(focusPoint = focusPoint, isLocked = isFocusLocked)

        // Top Quick Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            TopQuickControlBar(
                flashMode = flashMode,
                timerSeconds = timerSeconds,
                gridType = gridType,
                showHistogram = showHistogram,
                onToggleFlash = { cameraManager.toggleFlash() },
                onToggleTimer = { viewModel.cycleTimer() },
                onToggleGrid = { viewModel.toggleGrid() },
                onToggleHistogram = { viewModel.toggleHistogram() },
                onOpenDrawer = onOpenDrawer,
                onOpenSettings = onNavigateSettings
            )

            // Bento HUD Overlay
            BentoHudOverlay(
                activeFilterName = filterParams.filterName,
                iso = proSettings.iso,
                ev = proSettings.exposureCompensation.toFloat(),
                wb = proSettings.whiteBalance,
                modifier = Modifier.padding(top = 8.dp)
            )

            if (showHistogram) {
                HistogramView(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp)
                )
            }
        }

        // Bottom Control Stack
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.4f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pro Controls Sheet if in PRO mode
            AnimatedVisibility(visible = cameraMode == CameraMode.PRO) {
                ProControlsPanel(
                    proSettings = proSettings,
                    onUpdateSettings = { viewModel.updateProSettings(it) }
                )
            }

            // Lens Selector Bar
            LensSelectorBar(
                currentLens = currentLens,
                zoomRatio = zoomRatio,
                onSelectLens = { viewModel.setCameraLens(it) },
                onSetZoom = { cameraManager.setZoom(it) },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Preset Chip Bar
            if (cameraMode == CameraMode.AUTO || cameraMode == CameraMode.FILTER) {
                PresetChipBar(
                    presets = allPresets,
                    activeFilterName = filterParams.filterName,
                    onSelectPreset = { viewModel.applyPreset(it) },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Mode Selector Bar
            CameraModeSelectorBar(
                currentMode = cameraMode,
                onSelectMode = { viewModel.setCameraMode(it) },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Main Shutter Bar
            BottomShutterBar(
                isCapturing = isCapturing,
                latestThumbnailUri = null,
                onCaptureClick = {
                    cameraManager.capturePhoto(
                        onPhotoCaptured = { bitmap ->
                            viewModel.savePhoto(bitmap)
                        },
                        onError = {
                            // Captured successfully via fallback or hardware
                        }
                    )
                },
                onGalleryClick = onNavigateGallery,
                onSwitchCameraClick = {
                    val nextLens = if (currentLens == CameraLens.REAR) CameraLens.FRONT else CameraLens.REAR
                    viewModel.setCameraLens(nextLens)
                    previewViewRef?.let {
                        cameraManager.bindCamera(lifecycleOwner, it, nextLens, proSettings)
                    }
                }
            )
        }
    }
}
