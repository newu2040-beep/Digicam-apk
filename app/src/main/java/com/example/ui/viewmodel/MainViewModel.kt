package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.DigiCamDatabase
import com.example.data.db.MediaItemEntity
import com.example.data.db.PresetEntity
import com.example.data.model.*
import com.example.data.preferences.UserPreferencesRepository
import com.example.data.repository.DigiCamRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = DigiCamDatabase.getDatabase(application, viewModelScope)
    val preferencesRepository = UserPreferencesRepository(application)
    val repository = DigiCamRepository(application, database.mediaDao(), database.presetDao())

    val appTheme: StateFlow<AppTheme> = preferencesRepository.appTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.MATCHA)

    val onboardingCompleted: StateFlow<Boolean> = preferencesRepository.onboardingCompleted
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val gridType: StateFlow<GridType> = preferencesRepository.gridType
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GridType.RULE_OF_THIRDS)

    val showHistogram: StateFlow<Boolean> = preferencesRepository.showHistogram
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val showLevel: StateFlow<Boolean> = preferencesRepository.showLevel
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val shutterSound: StateFlow<Boolean> = preferencesRepository.shutterSound
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val allMedia: StateFlow<List<MediaItemEntity>> = repository.allMedia
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteMedia: StateFlow<List<MediaItemEntity>> = repository.favoriteMedia
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trashMedia: StateFlow<List<MediaItemEntity>> = repository.trashMedia
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPresets: StateFlow<List<PresetEntity>> = repository.allPresets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Camera state
    private val _currentLens = MutableStateFlow(CameraLens.REAR)
    val currentLens: StateFlow<CameraLens> = _currentLens

    private val _cameraMode = MutableStateFlow(CameraMode.AUTO)
    val cameraMode: StateFlow<CameraMode> = _cameraMode

    private val _proSettings = MutableStateFlow(ProSettings())
    val proSettings: StateFlow<ProSettings> = _proSettings

    private val _filterParams = MutableStateFlow(FilterParams())
    val filterParams: StateFlow<FilterParams> = _filterParams

    private val _timerSeconds = MutableStateFlow(0) // 0, 3, 5, 10
    val timerSeconds: StateFlow<Int> = _timerSeconds

    private val _selectedPhotoForDetail = MutableStateFlow<MediaItemEntity?>(null)
    val selectedPhotoForDetail: StateFlow<MediaItemEntity?> = _selectedPhotoForDetail

    fun completeOnboarding() {
        viewModelScope.launch {
            preferencesRepository.setOnboardingCompleted(true)
        }
    }

    fun setAppTheme(theme: AppTheme) {
        viewModelScope.launch {
            preferencesRepository.setAppTheme(theme)
        }
    }

    fun setCameraMode(mode: CameraMode) {
        _cameraMode.value = mode
    }

    fun setCameraLens(lens: CameraLens) {
        _currentLens.value = lens
    }

    fun updateProSettings(settings: ProSettings) {
        _proSettings.value = settings
    }

    fun updateFilterParams(params: FilterParams) {
        _filterParams.value = params
    }

    fun cycleTimer() {
        _timerSeconds.value = when (_timerSeconds.value) {
            0 -> 3
            3 -> 5
            5 -> 10
            else -> 0
        }
    }

    fun toggleGrid() {
        viewModelScope.launch {
            val next = when (gridType.value) {
                GridType.NONE -> GridType.RULE_OF_THIRDS
                GridType.RULE_OF_THIRDS -> GridType.GOLDEN_RATIO
                GridType.GOLDEN_RATIO -> GridType.SQUARE
                GridType.SQUARE -> GridType.NONE
            }
            preferencesRepository.setGridType(next)
        }
    }

    fun toggleHistogram() {
        viewModelScope.launch {
            preferencesRepository.setShowHistogram(!showHistogram.value)
        }
    }

    fun toggleShowLevel() {
        viewModelScope.launch {
            preferencesRepository.setShowLevel(!showLevel.value)
        }
    }

    fun toggleShutterSound() {
        viewModelScope.launch {
            preferencesRepository.setShutterSound(!shutterSound.value)
        }
    }

    fun deletePreset(preset: PresetEntity) {
        viewModelScope.launch {
            repository.deletePreset(preset)
        }
    }

    fun savePhoto(bitmap: Bitmap) {
        viewModelScope.launch {
            repository.saveCapturedPhoto(
                bitmap = bitmap,
                filterParams = filterParams.value,
                iso = proSettings.value.iso.toString(),
                shutterSpeed = proSettings.value.shutterSpeed
            )
        }
    }

    fun toggleFavorite(mediaId: Long, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(mediaId, currentFav)
        }
    }

    fun moveToTrash(mediaId: Long) {
        viewModelScope.launch {
            repository.moveToTrash(mediaId)
        }
    }

    fun restoreFromTrash(mediaId: Long) {
        viewModelScope.launch {
            repository.restoreFromTrash(mediaId)
        }
    }

    fun permanentDelete(media: MediaItemEntity) {
        viewModelScope.launch {
            repository.permanentDelete(media)
        }
    }

    fun saveCustomPreset(name: String, category: String) {
        viewModelScope.launch {
            val params = filterParams.value
            val entity = PresetEntity(
                name = name,
                category = category,
                filterName = params.filterName,
                intensity = params.intensity,
                grain = params.grain,
                fade = params.fade,
                temperature = params.temperature,
                contrast = params.contrast,
                highlights = params.highlights,
                shadows = params.shadows,
                saturation = params.saturation,
                tint = params.tint,
                isCustom = true
            )
            repository.insertPreset(entity)
        }
    }

    fun applyPreset(preset: PresetEntity) {
        _filterParams.value = FilterParams(
            filterName = preset.filterName,
            intensity = preset.intensity,
            grain = preset.grain,
            fade = preset.fade,
            temperature = preset.temperature,
            contrast = preset.contrast,
            highlights = preset.highlights,
            shadows = preset.shadows,
            saturation = preset.saturation,
            tint = preset.tint
        )
    }

    fun setSelectedPhoto(media: MediaItemEntity?) {
        _selectedPhotoForDetail.value = media
    }
}
