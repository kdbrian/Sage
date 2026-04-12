package com.kdbrian.sage.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kdbrian.sage.domain.model.InAppActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface InAppActivityDao {

    @Insert
    suspend fun logActivity(inAppActivity: InAppActivity)

    @Query("SELECT * FROM inappactivity")
    fun logs(): Flow<InAppActivity>

    @Query("SELECT * FROM inappactivity WHERE id =:id LIMIT 1")
    suspend fun loadLogById(id: String): InAppActivity?

    @Query("DELETE FROM inappactivity")
    suspend fun clearLogs()

}
