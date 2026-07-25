package com.example.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM media_items WHERE isTrash = 0 ORDER BY timestamp DESC")
    fun getAllMedia(): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE isFavorite = 1 AND isTrash = 0 ORDER BY timestamp DESC")
    fun getFavoriteMedia(): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE isTrash = 1 ORDER BY timestamp DESC")
    fun getTrashMedia(): Flow<List<MediaItemEntity>>

    @Query("SELECT * FROM media_items WHERE id = :id")
    suspend fun getMediaById(id: Long): MediaItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: MediaItemEntity): Long

    @Update
    suspend fun updateMedia(media: MediaItemEntity)

    @Query("UPDATE media_items SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE media_items SET isTrash = 1 WHERE id = :id")
    suspend fun moveToTrash(id: Long)

    @Query("UPDATE media_items SET isTrash = 0 WHERE id = :id")
    suspend fun restoreFromTrash(id: Long)

    @Query("DELETE FROM media_items WHERE id = :id")
    suspend fun permanentDelete(id: Long)

    @Query("DELETE FROM media_items WHERE isTrash = 1")
    suspend fun emptyTrash()
}

@Dao
interface PresetDao {
    @Query("SELECT * FROM presets ORDER BY isFavorite DESC, name ASC")
    fun getAllPresets(): Flow<List<PresetEntity>>

    @Query("SELECT * FROM presets WHERE category = :category ORDER BY name ASC")
    fun getPresetsByCategory(category: String): Flow<List<PresetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreset(preset: PresetEntity): Long

    @Update
    suspend fun updatePreset(preset: PresetEntity)

    @Delete
    suspend fun deletePreset(preset: PresetEntity)

    @Query("DELETE FROM presets WHERE id = :id")
    suspend fun deletePresetById(id: Long)
}
