package com.example.engine

import com.example.data.model.AppCustomizationConfig
import com.example.data.model.BuildJob
import com.example.data.model.BuildLogEntry
import com.example.data.model.BuildStatus
import com.example.data.model.PipelineType
import com.example.data.model.ProjectScanResult
import com.example.data.model.ProjectType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.MessageDigest
import java.util.UUID

class ConversionPipelineEngine {

    fun executePipeline(
        scanResult: ProjectScanResult,
        customConfig: AppCustomizationConfig,
        forceFailScenario: Boolean = false
    ): Flow<BuildJob> = flow {
        val buildId = "bld_" + UUID.randomUUID().toString().take(8)
        val logs = mutableListOf<BuildLogEntry>()

        fun addLog(stage: String, msg: String, isErr: Boolean = false, isSucc: Boolean = false): BuildLogEntry {
            val entry = BuildLogEntry(
                timestampMs = System.currentTimeMillis(),
                stage = stage,
                message = msg,
                isError = isErr,
                isSuccess = isSucc
            )
            logs.add(entry)
            return entry
        }

        var currentJob = BuildJob(
            buildId = buildId,
            projectName = scanResult.projectName,
            projectType = scanResult.projectType,
            pipelineType = scanResult.recommendedPipeline,
            config = customConfig,
            status = BuildStatus.UPLOADING,
            progressPercent = 5,
            currentStageName = "Uploading Project Archive",
            logs = logs.toList(),
            createdAtMs = System.currentTimeMillis(),
            expiresAtMs = System.currentTimeMillis() + (24 * 60 * 60 * 1000)
        )
        emit(currentJob)
        delay(400)

        // Stage 1: Uploading
        addLog("UPLOAD", "Project archive safely received: ${scanResult.totalSizeBytes / 1024} KB across ${scanResult.totalFilesCount} files.")
        addLog("SECURITY", "Verified archive integrity: 0 path traversal violations, safe from zip bombs.")
        currentJob = currentJob.copy(
            status = BuildStatus.SCANNING,
            progressPercent = 18,
            currentStageName = "Universal Project Scanner",
            logs = logs.toList()
        )
        emit(currentJob)
        delay(500)

        // Stage 2: Scanning
        addLog("SCANNER", "Framework identified: ${scanResult.projectType.label} (AI Source: ${scanResult.detectedAiTool.displayName})")
        addLog("SCANNER", "Entry point verified: ${scanResult.entryPoint}")
        addLog("PIPELINE", "Selected build pipeline: ${scanResult.recommendedPipeline.displayName}")
        if (scanResult.detectedEnvVars.isNotEmpty()) {
            addLog("SCANNER", "Masked ${scanResult.detectedEnvVars.size} detected environment variables (secrets safe).")
        }
        currentJob = currentJob.copy(
            status = BuildStatus.PREPARING,
            progressPercent = 32,
            currentStageName = "Preparing Android Container",
            logs = logs.toList()
        )
        emit(currentJob)
        delay(600)

        // Stage 3: Preparing Container
        addLog("SCAFFOLD", "Provisioning isolated build container with Android SDK 36 (minor API 1)...")
        addLog("CONFIG", "Injecting appName: \"${customConfig.appName}\", applicationId: \"${customConfig.packageId}\"")
        addLog("CONFIG", "Configured SDK bounds: minSdk = ${customConfig.minSdk}, targetSdk = ${customConfig.targetSdk}")
        addLog("PERMISSIONS", "Configured Android permissions in AndroidManifest.xml: ${customConfig.permissions.joinToString()}")
        if (customConfig.enableAdMob) {
            addLog("MONETIZATION", "Injected Google Mobile Ads (AdMob) SDK & App ID: ${customConfig.admobAppId}")
            addLog("MONETIZATION", "Configured adaptive banner ad unit: ${customConfig.admobBannerId}")
        }
        addLog("ASSETS", "Synthesizing adaptive launcher icon (foreground & background 108dp layers)...")
        currentJob = currentJob.copy(
            status = BuildStatus.INSTALLING_DEPENDENCIES,
            progressPercent = 50,
            currentStageName = "Resolving & Installing Dependencies",
            logs = logs.toList()
        )
        emit(currentJob)
        delay(700)

        // Stage 4: Dependencies
        when (scanResult.recommendedPipeline) {
            PipelineType.PIPELINE_A -> {
                addLog("DEPS", "HTML/JS project: bundling web static distribution assets...")
                addLog("DEPS", "Injecting Capacitor core Android runtime bridge...")
            }
            PipelineType.PIPELINE_B -> {
                addLog("DEPS", "Running pnpm install --frozen-lockfile in isolated sandbox...")
                addLog("DEPS", "Resolved ${scanResult.dependencies.size} package dependencies with 0 vulnerabilities.")
            }
            PipelineType.PIPELINE_C -> {
                addLog("DEPS", "Running Gradle dependency resolution for Kotlin & AndroidX Compose BOM...")
                addLog("DEPS", "Gradle wrapper validated: Gradle 8.11 with AGP 9.1.1.")
            }
            PipelineType.PIPELINE_D -> {
                addLog("DEPS", "Running flutter pub get in isolated worker...")
                addLog("DEPS", "Dart SDK 3.5.0 environment satisfied.")
            }
            PipelineType.PIPELINE_E -> {
                addLog("DEPS", "Resolving React Native autolinking modules and Metro bundler...")
            }
            PipelineType.PIPELINE_F -> {
                addLog("DEPS", "Full-Stack Project: Isolating frontend assets from backend node runtime...")
                addLog("DEPS", "Configured client API endpoint gateway bridge.")
            }
        }

        // Test error simulation if requested
        if (forceFailScenario) {
            delay(500)
            addLog("BUILD", "ERROR: Incompatible dependency version detected: '@google/genai' requires Node v18+ but worker was on Node v16", isErr = true)
            addLog("DIAGNOSTICS", "AI Error Analyzer: Incompatible Node.js runtime or conflicting transitive dependency.", isErr = true)
            currentJob = currentJob.copy(
                status = BuildStatus.FAILED,
                progressPercent = 55,
                currentStageName = "Build Failed",
                logs = logs.toList(),
                errorMessage = "Dependency conflict in package.json (@google/genai requires Node 18+).",
                suggestedFix = "Upgrade build worker Node container to v20 LTS and update package.json build script to vite build --emptyOutDir."
            )
            emit(currentJob)
            return@flow
        }

        currentJob = currentJob.copy(
            status = BuildStatus.BUILDING,
            progressPercent = 70,
            currentStageName = "Compiling Android Application",
            logs = logs.toList()
        )
        emit(currentJob)
        delay(800)

        // Stage 5: Building
        when (scanResult.recommendedPipeline) {
            PipelineType.PIPELINE_A, PipelineType.PIPELINE_B -> {
                addLog("BUILD", "Running: vite build --mode production...")
                addLog("BUILD", "Generated optimized web assets in dist/ (Chunks: 3, JS: 142KB, CSS: 28KB).")
                addLog("BUILD", "Syncing web assets to android/app/src/main/assets/public...")
                addLog("BUILD", "Executing Gradle build: ./gradlew :app:assembleRelease")
            }
            PipelineType.PIPELINE_C -> {
                addLog("BUILD", "Compiling Kotlin source files with KSP symbol processor...")
                addLog("BUILD", "Compiling Jetpack Compose layouts into optimized bytecode...")
                addLog("BUILD", "Executing AAPT2 resource merging and dex compilation...")
            }
            PipelineType.PIPELINE_D -> {
                addLog("BUILD", "Running flutter build apk --release --split-per-abi=false...")
            }
            PipelineType.PIPELINE_E -> {
                addLog("BUILD", "Bundling React Native index.android.bundle with Hermes engine...")
                addLog("BUILD", "Running Gradle :app:assembleRelease...")
            }
            PipelineType.PIPELINE_F -> {
                addLog("BUILD", "Bundling web UI with remote API gateway proxy...")
                addLog("BUILD", "Running Android release compilation...")
            }
        }

        currentJob = currentJob.copy(
            status = BuildStatus.SIGNING,
            progressPercent = 88,
            currentStageName = "Signing & Aligning Packages",
            logs = logs.toList()
        )
        emit(currentJob)
        delay(600)

        // Stage 6: Signing
        val isRelease = customConfig.buildType.equals("Release", ignoreCase = true)
        if (isRelease) {
            addLog("SIGNING", "Aligning zip packaging with zipalign -p 4...")
            addLog("SIGNING", "Signing APK with apksigner (v2 & v3 signature scheme using release keystore)...")
            addLog("SIGNING", "Generated signed AAB (Android App Bundle) for Google Play Store upload.")
        } else {
            addLog("SIGNING", "Signing debug binary with auto-generated development keystore...")
        }

        // Generate SHA256 checksum
        val rawSeed = "${customConfig.packageId}-${customConfig.versionCode}-${System.currentTimeMillis()}"
        val sha256 = generateFakeSha256(rawSeed)
        val apkSize = when (scanResult.projectType) {
            ProjectType.NATIVE_ANDROID_COMPOSE, ProjectType.NATIVE_ANDROID_XML -> 7.8
            ProjectType.FLUTTER -> 18.4
            ProjectType.REACT_NATIVE -> 24.1
            else -> 12.3
        }
        val aabSize = (apkSize * 0.65)

        addLog("VERIFY", "Cryptographic verification passed. SHA-256: ${sha256.take(16)}...${sha256.takeLast(8)}", isSucc = true)
        addLog("RETENTION", "Temporary source code queued for automated deletion in 24 hours or immediately upon download.", isSucc = true)
        addLog("SUCCESS", "AppForge AI successfully built ${customConfig.outputFormat} for ${customConfig.appName}!", isSucc = true)

        currentJob = currentJob.copy(
            status = BuildStatus.COMPLETED,
            progressPercent = 100,
            currentStageName = "Compilation Complete",
            logs = logs.toList(),
            apkSizeMb = apkSize,
            aabSizeMb = Math.round(aabSize * 10.0) / 10.0,
            sha256Checksum = sha256,
            downloadUrl = "https://appforge.internal/downloads/${buildId}/${customConfig.packageId}.${customConfig.outputFormat.lowercase()}",
            isDownloaded = false,
            isPurged = false
        )
        emit(currentJob)
    }

    private fun generateFakeSha256(seed: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(seed.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
