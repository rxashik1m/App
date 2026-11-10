package com.example

import com.example.data.model.AppCustomizationConfig
import com.example.data.model.BuildStatus
import com.example.data.model.PipelineType
import com.example.data.model.ProjectScanResult
import com.example.data.model.ProjectType
import com.example.data.model.AiSourceTool
import com.example.engine.ConversionPipelineEngine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `test pipeline execution and sha256 checksum generation`() = runBlocking {
        val engine = ConversionPipelineEngine()
        val scanResult = ProjectScanResult(
            scanId = "scan_test_1",
            projectName = "test-canvas-app",
            projectType = ProjectType.REACT_VITE,
            detectedAiTool = AiSourceTool.GOOGLE_AI_STUDIO,
            entryPoint = "src/main.tsx",
            hasPackageJson = true,
            hasGradleConfig = false,
            hasAndroidManifest = false,
            dependencies = emptyList(),
            missingDependencies = emptyList(),
            detectedEnvVars = emptyList(),
            detectedPermissions = listOf("INTERNET"),
            recommendedPipeline = PipelineType.PIPELINE_B,
            healthScore = 95,
            totalFilesCount = 20,
            totalSizeBytes = 500000L,
            warnings = emptyList(),
            isBuildable = true
        )

        val config = AppCustomizationConfig(
            appName = "Test Canvas",
            packageId = "com.aistudio.testcanvas",
            versionName = "1.0.0",
            versionCode = 1
        )

        val completedJob = engine.executePipeline(scanResult, config).last()
        assertEquals(BuildStatus.COMPLETED, completedJob.status)
        assertEquals(100, completedJob.progressPercent)
        assertNotNull(completedJob.sha256Checksum)
        assertEquals(64, completedJob.sha256Checksum?.length)
        assertTrue(completedJob.logs.isNotEmpty())
    }

    @Test
    fun `test pipeline error simulation and diagnostics`() = runBlocking {
        val engine = ConversionPipelineEngine()
        val scanResult = ProjectScanResult(
            scanId = "scan_test_err",
            projectName = "failing-app",
            projectType = ProjectType.REACT_VITE,
            detectedAiTool = AiSourceTool.GOOGLE_AI_STUDIO,
            entryPoint = "src/main.tsx",
            hasPackageJson = true,
            hasGradleConfig = false,
            hasAndroidManifest = false,
            dependencies = emptyList(),
            missingDependencies = emptyList(),
            detectedEnvVars = emptyList(),
            detectedPermissions = listOf("INTERNET"),
            recommendedPipeline = PipelineType.PIPELINE_B,
            healthScore = 80,
            totalFilesCount = 10,
            totalSizeBytes = 200000L,
            warnings = emptyList(),
            isBuildable = true
        )

        val config = AppCustomizationConfig(
            appName = "Failing App",
            packageId = "com.aistudio.failapp"
        )

        val failedJob = engine.executePipeline(scanResult, config, forceFailScenario = true).last()
        assertEquals(BuildStatus.FAILED, failedJob.status)
        assertNotNull(failedJob.errorMessage)
        assertNotNull(failedJob.suggestedFix)
    }
}
