package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.model.AdRewardDocument
import com.example.data.model.BuildJob
import com.example.data.model.UserBuildDocument
import com.example.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreRepository(private val context: Context) {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(
        context.getString(R.string.firestore_database_id)
    )

    private val auth = Firebase.auth

    fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google to access cloud data.")
    }

    suspend fun syncUserProfile() {
        val user = auth.currentUser ?: return
        val uid = user.uid
        val docRef = db.collection("users").document(uid)

        try {
            val snapshot = docRef.get().await()
            if (!snapshot.exists()) {
                val newProfile = mapOf(
                    "userId" to uid,
                    "email" to (user.email ?: "developer@appforge.ai"),
                    "displayName" to (user.displayName ?: "AI App Developer"),
                    "photoUrl" to (user.photoUrl?.toString() ?: ""),
                    "tier" to "Free Developer",
                    "buildCredits" to 10L,
                    "turboCredits" to 2L,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(newProfile).await()
            } else {
                docRef.update(
                    mapOf(
                        "email" to (user.email ?: "developer@appforge.ai"),
                        "displayName" to (user.displayName ?: "AI App Developer"),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e("FirestoreRepo", "Error syncing user profile: ${e.message}", e)
        }
    }

    fun observeUserProfile(): Flow<UserProfile?> = flow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            emit(null)
            return@flow
        }
        val docRef = db.collection("users").document(uid)
        docRef.snapshots()
            .map { it.toObject(UserProfile::class.java) }
            .catch { err ->
                Log.w("FirestoreRepo", "Error observing user profile: ${err.message}")
                emit(null)
            }
            .collect { emit(it) }
    }

    fun observeUserBuilds(): Flow<List<UserBuildDocument>> = flow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            emit(emptyList())
            return@flow
        }
        val collection = db.collection("users").document(uid).collection("builds")
        collection.snapshots()
            .map { it.toObjects<UserBuildDocument>() }
            .catch { err ->
                Log.w("FirestoreRepo", "Error observing builds: ${err.message}")
                emit(emptyList())
            }
            .collect { emit(it) }
    }

    suspend fun saveBuildJob(job: BuildJob) {
        val uid = auth.currentUser?.uid ?: return
        val docRef = db.collection("users").document(uid).collection("builds").document(job.buildId)
        val payload = mapOf(
            "buildId" to job.buildId,
            "userId" to uid,
            "appName" to job.config.appName,
            "packageId" to job.config.packageId,
            "projectType" to job.projectType.name,
            "pipelineType" to job.pipelineType.name,
            "status" to job.status.name,
            "apkSizeMb" to (job.apkSizeMb ?: 0.0),
            "sha256Checksum" to (job.sha256Checksum ?: ""),
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload, SetOptions.merge()).await()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e("FirestoreRepo", "Error saving build to cloud: ${e.message}", e)
        }
    }

    suspend fun claimAdReward(rewardType: String, adTitle: String) {
        val uid = requireUserId()
        val rewardId = "rwd_" + java.util.UUID.randomUUID().toString().take(8)
        val docRef = db.collection("users").document(uid).collection("ad_rewards").document(rewardId)
        val userDoc = db.collection("users").document(uid)

        val payload = mapOf(
            "rewardId" to rewardId,
            "userId" to uid,
            "rewardType" to rewardType,
            "adTitle" to adTitle,
            "claimedAt" to FieldValue.serverTimestamp()
        )

        try {
            docRef.set(payload).await()
            // Increment turbo credits
            userDoc.update(
                mapOf(
                    "turboCredits" to FieldValue.increment(1L),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e("FirestoreRepo", "Error claiming ad reward: ${e.message}", e)
        }
    }
}
