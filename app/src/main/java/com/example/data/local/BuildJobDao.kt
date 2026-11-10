package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildJobDao {

    @Query("SELECT * FROM build_jobs ORDER BY createdAtMs DESC")
    fun getAllJobs(): Flow<List<BuildJobEntity>>

    @Query("SELECT * FROM build_jobs WHERE buildId = :buildId")
    suspend fun getJobById(buildId: String): BuildJobEntity?

    @Query("SELECT * FROM build_jobs WHERE isPurged = 0 ORDER BY createdAtMs DESC")
    fun getActiveUnpurgedJobs(): Flow<List<BuildJobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: BuildJobEntity)

    @Update
    suspend fun updateJob(job: BuildJobEntity)

    @Query("UPDATE build_jobs SET isDownloaded = 1 WHERE buildId = :buildId")
    suspend fun markAsDownloaded(buildId: String)

    @Query("UPDATE build_jobs SET isPurged = 1 WHERE buildId = :buildId")
    suspend fun markAsPurged(buildId: String)

    @Query("DELETE FROM build_jobs WHERE buildId = :buildId")
    suspend fun deleteJobById(buildId: String)

    @Query("DELETE FROM build_jobs WHERE expiresAtMs < :nowMs")
    suspend fun deleteExpiredJobs(nowMs: Long)
}
