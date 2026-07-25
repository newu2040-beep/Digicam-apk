package com.example.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.example.data.db.MediaDao
import com.example.data.db.MediaItemEntity
import com.example.data.db.PresetDao
import com.example.data.db.PresetEntity
import com.example.data.model.FilterParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DigiCamRepository(
    private val context: Context,
    private val mediaDao: MediaDao,
    private val presetDao: PresetDao
) {
    val allMedia: Flow<List<MediaItemEntity>> = mediaDao.getAllMedia()
    val favoriteMedia: Flow<List<MediaItemEntity>> = mediaDao.getFavoriteMedia()
    val trashMedia: Flow<List<MediaItemEntity>> = mediaDao.getTrashMedia()
    val allPresets: Flow<List<PresetEntity>> = presetDao.getAllPresets()

    suspend fun saveCapturedPhoto(
        bitmap: Bitmap,
        filterParams: FilterParams,
        iso: String = "100",
        shutterSpeed: String = "1/250",
        latitude: Double? = null,
        longitude: Double? = null
    ): MediaItemEntity = withContext(Dispatchers.IO) {
        val processedBitmap = applyFilterToBitmap(bitmap, filterParams)
        
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "DIGICAM_$timeStamp.jpg"
        val storageDir = File(context.filesDir, "photos").apply { if (!exists()) mkdirs() }
        val photoFile = File(storageDir, fileName)

        FileOutputStream(photoFile).use { out ->
            processedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        }

        val mediaEntity = MediaItemEntity(
            filePath = photoFile.absolutePath,
            title = fileName,
            timestamp = System.currentTimeMillis(),
            width = processedBitmap.width,
            height = processedBitmap.height,
            fileSize = photoFile.length(),
            iso = iso,
            shutterSpeed = shutterSpeed,
            filterName = filterParams.filterName,
            latitude = latitude,
            longitude = longitude
        )

        val id = mediaDao.insertMedia(mediaEntity)
        mediaEntity.copy(id = id)
    }

    suspend fun toggleFavorite(mediaId: Long, currentFav: Boolean) {
        mediaDao.setFavorite(mediaId, !currentFav)
    }

    suspend fun moveToTrash(mediaId: Long) {
        mediaDao.moveToTrash(mediaId)
    }

    suspend fun restoreFromTrash(mediaId: Long) {
        mediaDao.restoreFromTrash(mediaId)
    }

    suspend fun permanentDelete(media: MediaItemEntity) {
        withContext(Dispatchers.IO) {
            val file = File(media.filePath)
            if (file.exists()) file.delete()
            mediaDao.permanentDelete(media.id)
        }
    }

    suspend fun insertPreset(preset: PresetEntity) {
        presetDao.insertPreset(preset)
    }

    suspend fun deletePreset(preset: PresetEntity) {
        presetDao.deletePreset(preset)
    }

    fun applyFilterToBitmap(src: Bitmap, params: FilterParams): Bitmap {
        val width = src.width
        val height = src.height
        val dest = Bitmap.createBitmap(width, height, src.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(dest)

        val cm = ColorMatrix()

        // Temperature & Tint adjustment
        val temp = params.temperature
        val tint = params.tint
        val contrast = 1f + params.contrast
        val sat = 1f + params.saturation

        val rScale = 1.0f + (temp * 0.3f)
        val gScale = 1.0f + (tint * 0.2f)
        val bScale = 1.0f - (temp * 0.3f)

        // Color grading matrix
        val matrix = floatArrayOf(
            rScale * contrast, 0f, 0f, 0f, (params.highlights * 50f) + (params.fade * 30f),
            0f, gScale * contrast, 0f, 0f, (params.highlights * 50f) + (params.fade * 30f),
            0f, 0f, bScale * contrast, 0f, (params.highlights * 50f) + (params.fade * 30f),
            0f, 0f, 0f, 1f, 0f
        )
        cm.set(matrix)

        // Saturation matrix if not standard
        if (params.filterName.contains("B&W") || params.filterName.contains("Monochrome")) {
            val bwMatrix = ColorMatrix().apply { setSaturation(0f) }
            cm.postConcat(bwMatrix)
        } else if (params.filterName.contains("Sepia")) {
            val sepiaMatrix = ColorMatrix().apply {
                set(floatArrayOf(
                    0.393f, 0.769f, 0.189f, 0f, 0f,
                    0.349f, 0.686f, 0.168f, 0f, 0f,
                    0.272f, 0.534f, 0.131f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                ))
            }
            cm.postConcat(sepiaMatrix)
        } else if (sat != 1.0f) {
            val satMatrix = ColorMatrix().apply { setSaturation(sat) }
            cm.postConcat(satMatrix)
        }

        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(cm)
        }

        canvas.drawBitmap(src, 0f, 0f, paint)
        return dest
    }
}
