package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ads.RewardedAdDialog
import com.example.ui.AppForgeViewModel
import com.example.ui.Screen
import com.example.ui.components.AppForgeBottomNav
import com.example.ui.components.AppForgeTopBar
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AppCustomizationScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BuildProgressScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DownloadCenterScreen
import com.example.ui.screens.LandingHeroScreen
import com.example.ui.screens.ProjectScannerScreen
import com.example.ui.screens.ProjectUploadScreen
import com.example.ui.screens.RetentionSecurityScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.AppForgeTheme

class MainActivity : ComponentActivity() {

    private val viewModel: AppForgeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppForgeTheme(darkTheme = true) {
                AppForgeApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AppForgeApp(viewModel: AppForgeViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val scanResult by viewModel.currentScanResult.collectAsStateWithLifecycle()
    val activeJob by viewModel.activeBuildJob.collectAsStateWithLifecycle()
    val history by viewModel.buildHistory.collectAsStateWithLifecycle()
    val metrics by viewModel.systemMetrics.collectAsStateWithLifecycle()
    val customConfig by viewModel.customConfig.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val isRewardedAdOpen by viewModel.isRewardedAdOpen.collectAsStateWithLifecycle()

    // Handle back button smoothly
    if (currentScreen != Screen.DASHBOARD && currentScreen != Screen.LANDING) {
        BackHandler {
            when (currentScreen) {
                Screen.CUSTOMIZATION -> viewModel.navigateTo(Screen.SCANNER)
                Screen.SCANNER -> viewModel.navigateTo(Screen.UPLOAD)
                Screen.BUILD_PROGRESS -> viewModel.navigateTo(Screen.DASHBOARD)
                Screen.DOWNLOAD_CENTER -> viewModel.navigateTo(Screen.DASHBOARD)
                Screen.RETENTION_SECURITY -> viewModel.navigateTo(Screen.DASHBOARD)
                Screen.ADMIN_DASHBOARD -> viewModel.navigateTo(Screen.DASHBOARD)
                Screen.SETTINGS -> viewModel.navigateTo(Screen.DASHBOARD)
                Screen.UPLOAD -> viewModel.navigateTo(Screen.DASHBOARD)
                Screen.AUTH -> viewModel.navigateTo(Screen.DASHBOARD)
                else -> viewModel.navigateTo(Screen.DASHBOARD)
            }
        }
    }

    // Rewarded Ad Dialog overlay
    RewardedAdDialog(
        isOpen = isRewardedAdOpen,
        onRewardEarned = { adTitle ->
            viewModel.onRewardEarned(adTitle)
        },
        onDismiss = {
            viewModel.closeRewardedAd()
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppForgeTopBar(
                currentScreen = currentScreen,
                currentUser = currentUser,
                turboCredits = userProfile?.turboCredits ?: 2L,
                onOpenRewardedAd = { viewModel.openRewardedAd() },
                onNavigate = { viewModel.navigateTo(it) }
            )
        },
        bottomBar = {
            if (currentScreen != Screen.LANDING && currentScreen != Screen.AUTH) {
                AppForgeBottomNav(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (currentScreen) {
            Screen.LANDING -> {
                LandingHeroScreen(
                    onNavigate = { viewModel.navigateTo(it) },
                    onQuickStartSample = { sampleId ->
                        viewModel.selectSampleProject(sampleId)
                    },
                    modifier = contentModifier
                )
            }
            Screen.DASHBOARD -> {
                DashboardScreen(
                    metrics = metrics,
                    history = history,
                    activeJob = activeJob,
                    onNavigate = { viewModel.navigateTo(it) },
                    onDownloadBuild = { buildId ->
                        viewModel.downloadArtifact(buildId)
                    },
                    onPurgeBuild = { buildId ->
                        viewModel.purgeSourceAndArtifacts(buildId)
                    },
                    onOpenRewardedAd = { viewModel.openRewardedAd() },
                    modifier = contentModifier
                )
            }
            Screen.UPLOAD -> {
                ProjectUploadScreen(
                    onZipSelected = { uri, name ->
                        viewModel.scanZipFile(uri, name)
                    },
                    onSelectSample = { sampleId ->
                        viewModel.selectSampleProject(sampleId)
                    },
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = contentModifier
                )
            }
            Screen.SCANNER -> {
                ProjectScannerScreen(
                    scanResult = scanResult,
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = contentModifier
                )
            }
            Screen.CUSTOMIZATION -> {
                AppCustomizationScreen(
                    currentConfig = customConfig,
                    onConfigChange = { viewModel.updateConfig(it) },
                    onStartBuild = { simulateFailure ->
                        viewModel.triggerBuild(simulateFailure)
                    },
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = contentModifier
                )
            }
            Screen.BUILD_PROGRESS -> {
                BuildProgressScreen(
                    buildJob = activeJob,
                    onApplyAiRepair = { viewModel.applyAiRepair() },
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = contentModifier
                )
            }
            Screen.DOWNLOAD_CENTER -> {
                DownloadCenterScreen(
                    buildJob = activeJob,
                    onPurgeNow = { buildId ->
                        viewModel.purgeSourceAndArtifacts(buildId)
                    },
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = contentModifier
                )
            }
            Screen.RETENTION_SECURITY -> {
                RetentionSecurityScreen(
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = contentModifier
                )
            }
            Screen.ADMIN_DASHBOARD -> {
                AdminDashboardScreen(
                    metrics = metrics,
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = contentModifier
                )
            }
            Screen.SETTINGS -> {
                SettingsScreen(
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = contentModifier
                )
            }
            Screen.AUTH -> {
                AuthScreen(
                    onAuthSuccess = {
                        viewModel.navigateTo(Screen.DASHBOARD)
                    },
                    onSkipOrContinueGuest = {
                        viewModel.navigateTo(Screen.DASHBOARD)
                    },
                    modifier = contentModifier
                )
            }
        }
    }
}
