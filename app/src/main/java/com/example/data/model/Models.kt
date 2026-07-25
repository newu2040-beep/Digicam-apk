package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class AppTheme(val displayName: String, val primaryColor: Long) {
    MATCHA("Matcha", 0xFF6B8E23),
    MINT("Mint", 0xFF4CAF50),
    LATTE("Latte", 0xFFC8A2C8),
    ESPRESSO("Espresso", 0xFF5D4037),
    PEACH("Peach", 0xFFFF8A65),
    CORAL("Coral", 0xFFFF6F61),
    LAVENDER("Lavender", 0xFF9575CD),
    SKY_BLUE("Sky Blue", 0xFF4FC3F7),
    OCEAN("Ocean", 0xFF0288D1),
    VANILLA("Vanilla", 0xFFFFF176),
    SAKURA_PINK("Sakura Pink", 0xFFF48FB1),
    LEMON_CREAM("Lemon Cream", 0xFFFFE082),
    MIDNIGHT("Midnight", 0xFF121212),
    GRAPHITE("Graphite", 0xFF37474F)
}

enum class CameraLens(val label: String) {
    REAR("Main (1x)"),
    FRONT("Selfie"),
    ULTRA_WIDE("Ultra-Wide (0.5x)"),
    TELEPHOTO("Telephoto (2x)"),
    MACRO("Macro")
}

enum class GridType(val label: String) {
    NONE("Off"),
    RULE_OF_THIRDS("3x3 Grid"),
    GOLDEN_RATIO("Golden Ratio"),
    SQUARE("1:1 Square")
}

enum class ExportFormat(val ext: String, val mimeType: String) {
    JPG("jpg", "image/jpeg"),
    PNG("png", "image/png"),
    WEBP("webp", "image/webp"),
    HEIF("heif", "image/heif"),
    DNG("dng", "image/x-adobe-dng")
}

enum class CameraMode(val label: String) {
    AUTO("Auto"),
    PRO("Pro Mode"),
    FILTER("Live Filter"),
    PORTRAIT("Portrait"),
    BURST("Burst")
}

enum class MeteringMode(val label: String) {
    MATRIX("Matrix"),
    CENTER("Center-Weighted"),
    SPOT("Spot")
}

data class FilterParams(
    val filterName: String = "Classic Film",
    val intensity: Float = 0.8f,
    val grain: Float = 0.2f,
    val fade: Float = 0.1f,
    val temperature: Float = 0.05f,
    val contrast: Float = 0.1f,
    val highlights: Float = 0.0f,
    val shadows: Float = 0.05f,
    val saturation: Float = 0.1f,
    val tint: Float = 0.0f
)

data class ProSettings(
    val iso: Int = 100, // 100 to 3200
    val shutterSpeed: String = "Auto", // "Auto", "1/2000", "1/1000", "1/500", "1/250", "1/125", "1/60", "1/30", "1/15", "1s"
    val whiteBalance: String = "Auto", // "Auto", "Daylight", "Cloudy", "Tungsten", "Fluorescent"
    val manualFocus: Float = 0.0f, // 0.0 (Auto/Infinity) to 1.0 (Macro)
    val exposureCompensation: Int = 0, // -6 to +6
    val meteringMode: MeteringMode = MeteringMode.MATRIX,
    val rawEnabled: Boolean = false,
    val lockAE: Boolean = false,
    val lockAF: Boolean = false
)

data class PhotoEditState(
    val cropRatio: Float = 0f, // 0 = free/original, 1.0 = 1:1, 1.333f = 4:3, 1.777f = 16:9
    val rotateDegrees: Float = 0f,
    val flipHorizontally: Boolean = false,
    val brightness: Float = 0f,
    val contrast: Float = 0f,
    val exposure: Float = 0f,
    val highlights: Float = 0f,
    val shadows: Float = 0f,
    val temperature: Float = 0f,
    val tint: Float = 0f,
    val vibrance: Float = 0f,
    val saturation: Float = 0f,
    val sharpen: Float = 0f,
    val noiseReduction: Float = 0f,
    val vignette: Float = 0f,
    val grain: Float = 0f,
    val bloom: Float = 0f
)
