package com.example.data.repository

import android.content.Context
import android.net.Uri
import com.example.data.local.AppForgeDatabase
import com.example.data.local.BuildJobDao
import com.example.data.local.BuildJobEntity
import com.example.data.model.AppCustomizationConfig
import com.example.data.model.BuildJob
import com.example.data.model.BuildStatus
import com.example.data.model.PipelineType
import com.example.data.model.ProjectScanResult
import com.example.data.model.ProjectType
import com.example.data.model.SystemWorkerMetrics
import com.example.data.model.UserProfile
import com.example.engine.ConversionPipelineEngine
import com.example.engine.ProjectScannerEngine
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppForgeRepository(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private val db = AppForgeDatabase.getDatabase(context)
    private val dao: BuildJobDao = db.buildJobDao()
    private val scannerEngine = ProjectScannerEngine(context)
    private val pipelineEngine = ConversionPipelineEngine()
    private val firestoreRepository = FirestoreRepository(context)

    private val _currentScanResult = MutableStateFlow<ProjectScanResult?>(null)
    val currentScanResult: StateFlow<ProjectScanResult?> = _currentScanResult.asStateFlow()

    private val _activeBuildJob = MutableStateFlow<BuildJob?>(null)
    val activeBuildJob: StateFlow<BuildJob?> = _activeBuildJob.asStateFlow()

    private val _systemMetrics = MutableStateFlow(SystemWorkerMetrics())
    val systemMetrics: StateFlow<SystemWorkerMetrics> = _systemMetrics.asStateFlow()

    val userProfile: Flow<UserProfile?> = firestoreRepository.observeUserProfile()

    init {
        scope.launch(Dispatchers.IO) {
            checkAndSeedSampleBuilds()
            purgeExpiredJobs()
            if (Firebase.auth.currentUser != null) {
                firestoreRepository.syncUserProfile()
            }
        }
    }

    suspend fun onUserSignedIn() {
        withContext(Dispatchers.IO) {
            firestoreRepository.syncUserProfile()
        }
    }

    suspend fun claimAdReward(rewardType: String, adTitle: String) {
        withContext(Dispatchers.IO) {
            if (Firebase.auth.currentUser != null) {
                firestoreRepository.claimAdReward(rewardType, adTitle)
            }
        }
    }

    fun scanUploadedZip(uri: Uri, fileName: String) {
        scope.launch(Dispatchers.IO) {
            val result = scannerEngine.scanZipArchive(uri, fileName)
            _currentScanResult.value = result
        }
    }

    fun scanSampleProject(sampleId: String) {
        scope.launch(Dispatchers.IO) {
            val result = scannerEngine.scanSampleProject(sampleId)
            _currentScanResult.value = result
        }
    }

    fun clearCurrentScan() {
        _currentScanResult.value = null
    }

    fun startBuild(
        scanResult: ProjectScanResult,
        config: AppCustomizationConfig,
        simulateFailure: Boolean = false
    ) {
        scope.launch(Dispatchers.IO) {
            pipelineEngine.executePipeline(scanResult, config, simulateFailure).collect { job ->
                _activeBuildJob.value = job
                if (job.status == BuildStatus.COMPLETED || job.status == BuildStatus.FAILED) {
                    // Persist to Room local database
                    val entity = BuildJobEntity(
                        buildId = job.buildId,
                        userId = Firebase.auth.currentUser?.uid ?: job.userId,
                        projectName = job.projectName,
                        projectType = job.projectType.name,
                        pipelineType = job.pipelineType.name,
                        appName = job.config.appName,
                        packageId = job.config.packageId,
                        versionName = job.config.versionName,
                        versionCode = job.config.versionCode,
                        status = job.status.name,
                        progressPercent = job.progressPercent,
                        currentStageName = job.currentStageName,
                        apkSizeMb = job.apkSizeMb,
                        aabSizeMb = job.aabSizeMb,
                        sha256Checksum = job.sha256Checksum,
                        downloadUrl = job.downloadUrl,
                        createdAtMs = job.createdAtMs,
                        expiresAtMs = job.expiresAtMs,
                        isDownloaded = job.isDownloaded,
                        isPurged = job.isPurged,
                        errorMessage = job.errorMessage,
                        suggestedFix = job.suggestedFix
                    )
                    dao.insertJob(entity)

                    // Sync to Firebase Firestore if signed in
                    if (Firebase.auth.currentUser != null) {
                        firestoreRepository.saveBuildJob(job)
                    }

                    // Update metrics
                    _systemMetrics.value = _systemMetrics.value.copy(
                        totalCompletedBuilds = if (job.status == BuildStatus.COMPLETED) _systemMetrics.value.totalCompletedBuilds + 1 else _systemMetrics.value.totalCompletedBuilds,
                        totalFailedBuilds = if (job.status == BuildStatus.FAILED) _systemMetrics.value.totalFailedBuilds + 1 else _systemMetrics.value.totalFailedBuilds
                    )
                }
            }
        }
    }

    suspend fun markAsDownloaded(buildId: String) = withContext(Dispatchers.IO) {
        dao.markAsDownloaded(buildId)
        val current = _activeBuildJob.value
        if (current?.buildId == buildId) {
            _activeBuildJob.value = current.copy(isDownloaded = true)
        }
    }

    suspend fun purgeSourceAndArtifacts(buildId: String) = withContext(Dispatchers.IO) {
        dao.markAsPurged(buildId)
        val current = _activeBuildJob.value
        if (current?.buildId == buildId) {
            _activeBuildJob.value = current.copy(isPurged = true)
        }
    }

    suspend fun purgeExpiredJobs() = withContext(Dispatchers.IO) {
        dao.deleteExpiredJobs(System.currentTimeMillis())
    }

    fun getAllBuildJobs(): Flow<List<BuildJobEntity>> {
        return dao.getAllJobs()
    }

    private suspend fun checkAndSeedSampleBuilds() {
        val now = System.currentTimeMillis()
        val dummy = BuildJobEntity(
            buildId = "bld_seed_101",
            userId = "usr_dev_4829",
            projectName = "gemini-smart-canvas",
            projectType = ProjectType.REACT_VITE.name,
            pipelineType = PipelineType.PIPELINE_B.name,
            appName = "Gemini Canvas",
            packageId = "com.aistudio.geminicanvas",
            versionName = "1.0.0",
            versionCode = 1,
            status = BuildStatus.COMPLETED.name,
            progressPercent = 100,
            currentStageName = "Compilation Complete",
            apkSizeMb = 12.8,
            aabSizeMb = 8.3,
            sha256Checksum = "9b71d224bd62f3785d96d46ad3ea3d73319bfbc2890caadae2dff72519673ca7",
            downloadUrl = "https://appforge.internal/downloads/bld_seed_101/com.aistudio.geminicanvas.apk",
            createdAtMs = now - (3 * 3600 * 1000),
            expiresAtMs = now + (21 * 3600 * 1000),
            isDownloaded = true,
            isPurged = false,
            errorMessage = null,
            suggestedFix = null
        )
        dao.insertJob(dummy)
    }
}
