package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_items")
data class MediaItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val filePath: String,
    val title: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isTrash: Boolean = false,
    val width: Int = 1920,
    val height: Int = 1080,
    val fileSize: Long = 0,
    val iso: String = "100",
    val shutterSpeed: String = "1/250",
    val aperture: String = "f/1.8",
    val filterName: String = "Classic Film",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val mimeType: String = "image/jpeg"
)

@Entity(tableName = "presets")
data class PresetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // "Film", "Retro", "Mood", "Portrait", "B&W", "Custom"
    val filterName: String,
    val intensity: Float = 0.8f,
    val grain: Float = 0.2f,
    val fade: Float = 0.1f,
    val temperature: Float = 0.0f,
    val contrast: Float = 0.1f,
    val highlights: Float = 0.0f,
    val shadows: Float = 0.0f,
    val saturation: Float = 0.0f,
    val tint: Float = 0.0f,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = true
)
