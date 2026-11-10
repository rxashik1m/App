package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val tier: String = "Free Developer",
    val buildCredits: Long = 10L,
    val turboCredits: Long = 2L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class UserBuildDocument(
    @DocumentId val id: String = "",
    val buildId: String = "",
    val userId: String = "",
    val appName: String = "",
    val packageId: String = "",
    val projectType: String = "",
    val pipelineType: String = "",
    val status: String = "",
    val apkSizeMb: Double = 0.0,
    val sha256Checksum: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class AdRewardDocument(
    @DocumentId val id: String = "",
    val rewardId: String = "",
    val userId: String = "",
    val rewardType: String = "TURBO_BUILD_CREDIT",
    val adTitle: String = "",
    @ServerTimestamp val claimedAt: Timestamp? = null
)
