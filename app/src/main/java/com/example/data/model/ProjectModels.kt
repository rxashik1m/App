package com.example.data.model

enum class ProjectType(val label: String, val badgeColorHex: Long) {
    WEB_HTML("HTML / CSS / JS", 0xFFE44D26),
    REACT_VITE("React + Vite", 0xFF61DAFB),
    VUE_JS("Vue.js", 0xFF42B883),
    ANGULAR("Angular", 0xFFDD0031),
    SVELTE("Svelte", 0xFFFF3E00),
    NEXT_JS("Next.js", 0xFF000000),
    NATIVE_ANDROID_COMPOSE("Android (Jetpack Compose)", 0xFF3DDC84),
    NATIVE_ANDROID_XML("Android (Java / XML)", 0xFF0288D1),
    FLUTTER("Flutter / Dart", 0xFF02569B),
    REACT_NATIVE("React Native", 0xFF00D8FF),
    FULLSTACK_NODE("Full-Stack (Client + API)", 0xFF68A063),
    UNKNOWN("Unsupported / Incomplete", 0xFF9E9E9E)
}

enum class AiSourceTool(val displayName: String, val description: String) {
    GOOGLE_AI_STUDIO("Google AI Studio", "Generated via Gemini API / Google AI Studio build system"),
    CLAUDE("Claude Artifacts / Anthropic", "Generated via Anthropic Claude Sonnet code artifacts"),
    CURSOR("Cursor IDE", "Generated via Cursor Composer / Agent mode"),
    BOLT_NEW("Bolt.new / StackBlitz", "Full-stack web container prompt project"),
    LOVABLE("Lovable.dev", "AI web-app rapid prototyping project"),
    V0_DEV("v0.dev / Vercel", "Tailwind & React component-driven project"),
    WINDSURF("Windsurf / Codeium", "Cascade AI agent multi-file workspace"),
    CHATGPT("ChatGPT / OpenAI", "OpenAI Canvas & Code Interpreter output"),
    GITHUB_COPILOT("GitHub Copilot", "Copilot Workspace / Chat scaffolding"),
    MANUAL_CUSTOM("Custom AI / Raw Code", "Custom or unrecognized AI coding assistant")
}

enum class PipelineType(val displayName: String, val description: String) {
    PIPELINE_A("Pipeline A: HTML/CSS/JS", "Web packaging via Capacitor Android bridge"),
    PIPELINE_B("Pipeline B: Modern Web (React/Vite/Vue)", "Node dependency resolution, Vite build & Capacitor Android"),
    PIPELINE_C("Pipeline C: Native Android", "Native Gradle compilation, manifest preservation & AAPT2 packaging"),
    PIPELINE_D("Pipeline D: Flutter", "Flutter pub get & Flutter Android Gradle compilation"),
    PIPELINE_E("Pipeline E: React Native", "Metro bundler & Android Native Gradle wrapper"),
    PIPELINE_F("Pipeline F: Full-stack", "Frontend isolation with external API endpoint configuration")
}

enum class BuildStatus {
    IDLE,
    UPLOADING,
    SCANNING,
    PREPARING,
    INSTALLING_DEPENDENCIES,
    BUILDING,
    SIGNING,
    COMPLETED,
    FAILED,
    EXPIRED
}

data class DependencyInfo(
    val name: String,
    val version: String,
    val isInstalled: Boolean,
    val isCritical: Boolean = true
)

data class EnvVarInfo(
    val key: String,
    val maskedValue: String,
    val isSecret: Boolean = true
)

data class ProjectScanResult(
    val scanId: String,
    val projectName: String,
    val projectType: ProjectType,
    val detectedAiTool: AiSourceTool,
    val entryPoint: String,
    val hasPackageJson: Boolean,
    val hasGradleConfig: Boolean,
    val hasAndroidManifest: Boolean,
    val dependencies: List<DependencyInfo>,
    val missingDependencies: List<String>,
    val detectedEnvVars: List<EnvVarInfo>,
    val detectedPermissions: List<String>,
    val recommendedPipeline: PipelineType,
    val healthScore: Int, // 0 - 100
    val totalFilesCount: Int,
    val totalSizeBytes: Long,
    val warnings: List<String>,
    val isBuildable: Boolean
)

data class AppCustomizationConfig(
    val appName: String,
    val packageId: String,
    val versionName: String = "1.0.0",
    val versionCode: Int = 1,
    val minSdk: Int = 24,
    val targetSdk: Int = 36,
    val appOrientation: String = "Portrait", // Portrait, Landscape, Auto-Sensor
    val themeMode: String = "System", // System, Dark, Light
    val statusBarColorHex: String = "#0A0F1D",
    val navBarColorHex: String = "#0A0F1D",
    val enableInternet: Boolean = true,
    val permissions: List<String> = listOf("INTERNET", "ACCESS_NETWORK_STATE"),
    val buildType: String = "Release", // Debug, Release
    val outputFormat: String = "APK", // APK, AAB
    val enableAdMob: Boolean = false,
    val admobAppId: String = "ca-app-pub-3940256099942544~3347511713",
    val admobBannerId: String = "ca-app-pub-3940256099942544/6300978111"
)

data class BuildLogEntry(
    val timestampMs: Long = System.currentTimeMillis(),
    val stage: String,
    val message: String,
    val isError: Boolean = false,
    val isSuccess: Boolean = false
)

data class BuildJob(
    val buildId: String,
    val userId: String = "usr_dev_4829",
    val projectName: String,
    val projectType: ProjectType,
    val pipelineType: PipelineType,
    val config: AppCustomizationConfig,
    val status: BuildStatus,
    val progressPercent: Int,
    val currentStageName: String,
    val logs: List<BuildLogEntry>,
    val apkSizeMb: Double? = null,
    val aabSizeMb: Double? = null,
    val sha256Checksum: String? = null,
    val downloadUrl: String? = null,
    val createdAtMs: Long = System.currentTimeMillis(),
    val expiresAtMs: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000), // 24 hours
    val isDownloaded: Boolean = false,
    val isPurged: Boolean = false,
    val errorMessage: String? = null,
    val suggestedFix: String? = null
)

data class SystemWorkerMetrics(
    val activeWorkersCount: Int = 4,
    val maxWorkersCount: Int = 8,
    val queueLength: Int = 1,
    val averageBuildDurationSec: Int = 42,
    val storageUsedMb: Double = 312.4,
    val storageLimitMb: Double = 2048.0,
    val totalCompletedBuilds: Int = 184,
    val totalFailedBuilds: Int = 6,
    val successRatePercent: Double = 96.8
)
