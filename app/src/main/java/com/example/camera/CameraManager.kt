package com.example.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.data.model.CameraLens
import com.example.data.model.ProSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class CameraManager(private val context: Context) {

    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var imageCapture: ImageCapture? = null
    private var preview: Preview? = null

    private val _currentLens = MutableStateFlow(CameraLens.REAR)
    val currentLens: StateFlow<CameraLens> = _currentLens

    private val _flashMode = MutableStateFlow(ImageCapture.FLASH_MODE_OFF)
    val flashMode: StateFlow<Int> = _flashMode

    private val _zoomRatio = MutableStateFlow(1.0f)
    val zoomRatio: StateFlow<Float> = _zoomRatio

    private val _isCapturing = MutableStateFlow(false)
    val isCapturing: StateFlow<Boolean> = _isCapturing

    fun bindCamera(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        lens: CameraLens = CameraLens.REAR,
        proSettings: ProSettings = ProSettings()
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val cameraSelector = when (lens) {
                CameraLens.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
                else -> CameraSelector.DEFAULT_BACK_CAMERA
            }

            preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setFlashMode(_flashMode.value)
                .build()

            try {
                cameraProvider?.unbindAll()
                camera = cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                // Apply exposure compensation from Pro settings if supported
                camera?.cameraInfo?.exposureState?.let { exposureState ->
                    if (exposureState.isExposureCompensationSupported) {
                        camera?.cameraControl?.setExposureCompensationIndex(proSettings.exposureCompensation)
                    }
                }

                _currentLens.value = lens
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun setZoom(ratio: Float) {
        _zoomRatio.value = ratio
        camera?.cameraControl?.setZoomRatio(ratio)
    }

    fun toggleFlash() {
        val nextMode = when (_flashMode.value) {
            ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_AUTO
            ImageCapture.FLASH_MODE_AUTO -> ImageCapture.FLASH_MODE_ON
            else -> ImageCapture.FLASH_MODE_OFF
        }
        _flashMode.value = nextMode
        imageCapture?.flashMode = nextMode
    }

    fun enableTorch(enabled: Boolean) {
        camera?.cameraControl?.enableTorch(enabled)
    }

    fun tapToFocus(previewView: PreviewView, x: Float, y: Float) {
        val meteringPointFactory = previewView.meteringPointFactory
        val meteringPoint = meteringPointFactory.createPoint(x, y)
        val action = FocusMeteringAction.Builder(meteringPoint, FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE)
            .setAutoCancelDuration(3, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        camera?.cameraControl?.startFocusAndMetering(action)
    }

    fun capturePhoto(onPhotoCaptured: (Bitmap) -> Unit, onError: (Exception) -> Unit) {
        val imageCapture = imageCapture ?: return onError(IllegalStateException("Camera not ready"))

        _isCapturing.value = true
        val tempFile = File.createTempFile("temp_capture_", ".jpg", context.cacheDir)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(tempFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    _isCapturing.value = false
                    try {
                        val bitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
                        val rotatedBitmap = fixOrientationIfNeeded(bitmap, tempFile.absolutePath)
                        tempFile.delete()
                        onPhotoCaptured(rotatedBitmap)
                    } catch (e: Exception) {
                        onError(e)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    _isCapturing.value = false
                    onError(exception)
                }
            }
        )
    }

    private fun fixOrientationIfNeeded(bitmap: Bitmap, path: String): Bitmap {
        return if (_currentLens.value == CameraLens.FRONT) {
            val matrix = Matrix().apply { postScale(-1f, 1f) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }
    }
}
