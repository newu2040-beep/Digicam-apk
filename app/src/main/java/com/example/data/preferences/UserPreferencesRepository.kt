package com.example.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.AppTheme
import com.example.data.model.GridType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "digicam_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val APP_THEME = stringPreferencesKey("app_theme")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val DEFAULT_FILTER = stringPreferencesKey("default_filter")
        val GRID_TYPE = stringPreferencesKey("grid_type")
        val SHOW_HISTOGRAM = booleanPreferencesKey("show_histogram")
        val SHOW_LEVEL = booleanPreferencesKey("show_level")
        val SHUTTER_SOUND = booleanPreferencesKey("shutter_sound")
        val GEOTAG_LOCATION = booleanPreferencesKey("geotag_location")
        val SAVE_RAW = booleanPreferencesKey("save_raw")
        val EXPORT_QUALITY = intPreferencesKey("export_quality")
        val EXP_RETRO_VIEWFINDER = booleanPreferencesKey("exp_retro_viewfinder")
        val EXP_ADVANCED_PROCESSING = booleanPreferencesKey("exp_advanced_processing")
    }

    val appTheme: Flow<AppTheme> = context.dataStore.data.map { prefs ->
        val name = prefs[Keys.APP_THEME] ?: AppTheme.MATCHA.name
        try { AppTheme.valueOf(name) } catch (e: Exception) { AppTheme.MATCHA }
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETED] ?: false
    }

    val defaultFilter: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.DEFAULT_FILTER] ?: "Classic Film"
    }

    val gridType: Flow<GridType> = context.dataStore.data.map { prefs ->
        val name = prefs[Keys.GRID_TYPE] ?: GridType.RULE_OF_THIRDS.name
        try { GridType.valueOf(name) } catch (e: Exception) { GridType.RULE_OF_THIRDS }
    }

    val showHistogram: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.SHOW_HISTOGRAM] ?: true
    }

    val showLevel: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.SHOW_LEVEL] ?: true
    }

    val shutterSound: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.SHUTTER_SOUND] ?: true
    }

    val geotagLocation: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.GEOTAG_LOCATION] ?: false
    }

    val saveRaw: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.SAVE_RAW] ?: false
    }

    val exportQuality: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.EXPORT_QUALITY] ?: 95
    }

    val expRetroViewfinder: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.EXP_RETRO_VIEWFINDER] ?: false
    }

    val expAdvancedProcessing: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.EXP_ADVANCED_PROCESSING] ?: true
    }

    suspend fun setAppTheme(theme: AppTheme) {
        context.dataStore.edit { prefs -> prefs[Keys.APP_THEME] = theme.name }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.ONBOARDING_COMPLETED] = completed }
    }

    suspend fun setDefaultFilter(filterName: String) {
        context.dataStore.edit { prefs -> prefs[Keys.DEFAULT_FILTER] = filterName }
    }

    suspend fun setGridType(gridType: GridType) {
        context.dataStore.edit { prefs -> prefs[Keys.GRID_TYPE] = gridType.name }
    }

    suspend fun setShowHistogram(show: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.SHOW_HISTOGRAM] = show }
    }

    suspend fun setShowLevel(show: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.SHOW_LEVEL] = show }
    }

    suspend fun setShutterSound(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.SHUTTER_SOUND] = enabled }
    }

    suspend fun setGeotagLocation(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.GEOTAG_LOCATION] = enabled }
    }

    suspend fun setSaveRaw(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.SAVE_RAW] = enabled }
    }

    suspend fun setExportQuality(quality: Int) {
        context.dataStore.edit { prefs -> prefs[Keys.EXPORT_QUALITY] = quality }
    }

    suspend fun setExpRetroViewfinder(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.EXP_RETRO_VIEWFINDER] = enabled }
    }

    suspend fun setExpAdvancedProcessing(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.EXP_ADVANCED_PROCESSING] = enabled }
    }
}
