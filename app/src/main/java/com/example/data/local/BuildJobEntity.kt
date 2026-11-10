package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "build_jobs")
data class BuildJobEntity(
    @PrimaryKey
    val buildId: String,
    val userId: String,
    val projectName: String,
    val projectType: String,
    val pipelineType: String,
    val appName: String,
    val packageId: String,
    val versionName: String,
    val versionCode: Int,
    val status: String,
    val progressPercent: Int,
    val currentStageName: String,
    val apkSizeMb: Double?,
    val aabSizeMb: Double?,
    val sha256Checksum: String?,
    val downloadUrl: String?,
    val createdAtMs: Long,
    val expiresAtMs: Long,
    val isDownloaded: Boolean,
    val isPurged: Boolean,
    val errorMessage: String?,
    val suggestedFix: String?
)
