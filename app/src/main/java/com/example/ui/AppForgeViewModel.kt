package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BuildJobEntity
import com.example.data.model.AppCustomizationConfig
import com.example.data.model.BuildJob
import com.example.data.model.ProjectScanResult
import com.example.data.model.SystemWorkerMetrics
import com.example.data.model.UserProfile
import com.example.data.repository.AppForgeRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    LANDING,
    DASHBOARD,
    UPLOAD,
    SCANNER,
    CUSTOMIZATION,
    BUILD_PROGRESS,
    DOWNLOAD_CENTER,
    RETENTION_SECURITY,
    ADMIN_DASHBOARD,
    SETTINGS,
    AUTH
}

class AppForgeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppForgeRepository(application, viewModelScope)

    private val _currentScreen = MutableStateFlow(Screen.LANDING)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(Firebase.auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _isRewardedAdOpen = MutableStateFlow(false)
    val isRewardedAdOpen: StateFlow<Boolean> = _isRewardedAdOpen.asStateFlow()

    val currentScanResult: StateFlow<ProjectScanResult?> = repository.currentScanResult
    val activeBuildJob: StateFlow<BuildJob?> = repository.activeBuildJob
    val systemMetrics: StateFlow<SystemWorkerMetrics> = repository.systemMetrics

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val buildHistory: StateFlow<List<BuildJobEntity>> = repository.getAllBuildJobs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _customConfig = MutableStateFlow(
        AppCustomizationConfig(
            appName = "My App",
            packageId = "com.aistudio.myapp.c7x9k",
            versionName = "1.0.0",
            versionCode = 1
        )
    )
    val customConfig: StateFlow<AppCustomizationConfig> = _customConfig.asStateFlow()

    init {
        Firebase.auth.addAuthStateListener { auth ->
            _currentUser.value = auth.currentUser
            if (auth.currentUser != null) {
                viewModelScope.launch {
                    repository.onUserSignedIn()
                }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun openRewardedAd() {
        _isRewardedAdOpen.value = true
    }

    fun closeRewardedAd() {
        _isRewardedAdOpen.value = false
    }

    fun onRewardEarned(adTitle: String) {
        _isRewardedAdOpen.value = false
        viewModelScope.launch {
            repository.claimAdReward("TURBO_BUILD_CREDIT", adTitle)
        }
    }

    fun scanZipFile(uri: Uri, fileName: String) {
        repository.scanUploadedZip(uri, fileName)
        viewModelScope.launch {
            repository.currentScanResult.collect { result ->
                if (result != null) {
                    val cleanName = result.projectName.replace("-", " ")
                        .split(" ")
                        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                    val cleanPkg = "com.aistudio." + result.projectName.lowercase().replace("[^a-z0-9]".toRegex(), "")
                    _customConfig.value = _customConfig.value.copy(
                        appName = cleanName,
                        packageId = cleanPkg
                    )
                    _currentScreen.value = Screen.SCANNER
                }
            }
        }
    }

    fun selectSampleProject(sampleId: String) {
        repository.scanSampleProject(sampleId)
        viewModelScope.launch {
            repository.currentScanResult.collect { result ->
                if (result != null) {
                    val cleanName = result.projectName.replace("-", " ")
                        .split(" ")
                        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                    val cleanPkg = "com.aistudio." + result.projectName.lowercase().replace("[^a-z0-9]".toRegex(), "")
                    _customConfig.value = _customConfig.value.copy(
                        appName = cleanName,
                        packageId = cleanPkg
                    )
                    _currentScreen.value = Screen.SCANNER
                }
            }
        }
    }

    fun updateConfig(config: AppCustomizationConfig) {
        _customConfig.value = config
    }

    fun triggerBuild(simulateFailure: Boolean = false) {
        val scan = currentScanResult.value ?: return
        repository.startBuild(scan, customConfig.value, simulateFailure)
        _currentScreen.value = Screen.BUILD_PROGRESS
    }

    fun applyAiRepair() {
        val scan = currentScanResult.value ?: return
        repository.startBuild(scan, customConfig.value, simulateFailure = false)
    }

    fun downloadArtifact(buildId: String) {
        viewModelScope.launch {
            repository.markAsDownloaded(buildId)
            _currentScreen.value = Screen.DOWNLOAD_CENTER
        }
    }

    fun purgeSourceAndArtifacts(buildId: String) {
        viewModelScope.launch {
            repository.purgeSourceAndArtifacts(buildId)
        }
    }

    fun backToDashboard() {
        _currentScreen.value = Screen.DASHBOARD
    }
}
