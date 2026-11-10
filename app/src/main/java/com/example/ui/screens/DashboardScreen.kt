package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ads.DeveloperBannerAd
import com.example.data.local.BuildJobEntity
import com.example.data.model.BuildJob
import com.example.data.model.BuildStatus
import com.example.data.model.ProjectType
import com.example.data.model.SystemWorkerMetrics
import com.example.ui.Screen
import com.example.ui.components.FrameworkBadge
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.ForgeAmber
import com.example.ui.theme.ForgeCyan
import com.example.ui.theme.ForgeEmerald
import com.example.ui.theme.ForgeRose
import com.example.ui.theme.SlateBgDark
import com.example.ui.theme.SlateBorderDark
import com.example.ui.theme.SlateSurfaceDark
import com.example.ui.theme.SlateSurfaceElevated
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    metrics: SystemWorkerMetrics,
    history: List<BuildJobEntity>,
    activeJob: BuildJob?,
    onNavigate: (Screen) -> Unit,
    onDownloadBuild: (String) -> Unit,
    onPurgeBuild: (String) -> Unit,
    onOpenRewardedAd: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBgDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome and Quick Action
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Build Dashboard",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = SlateTextPrimary
                    )
                    Text(
                        text = "Real-time AI to Android conversion pipelines",
                        fontSize = 12.sp,
                        color = SlateTextSecondary
                    )
                }

                Button(
                    onClick = { onNavigate(Screen.UPLOAD) },
                    colors = ButtonDefaults.buttonColors(containerColor = ForgeCyan, contentColor = Color(0xFF031A24)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("dashboard_new_build_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Build", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Ads System: Developer Banner Ad
        item {
            DeveloperBannerAd(onWatchRewarded = onOpenRewardedAd)
        }

        // Active build card (if any running or recently generated)
        if (activeJob != null && activeJob.status != BuildStatus.IDLE) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SlateSurfaceDark)
                        .border(1.dp, ForgeCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .clickable { onNavigate(Screen.BUILD_PROGRESS) }
                        .padding(14.dp)
                        .testTag("active_build_banner")
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (activeJob.status == BuildStatus.COMPLETED) ForgeEmerald else ForgeCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ACTIVE COMPILER JOB: ${activeJob.currentStageName.uppercase()}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeJob.status == BuildStatus.COMPLETED) ForgeEmerald else ForgeCyan
                                )
                            }
                            Text(
                                text = "${activeJob.progressPercent}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = ForgeCyan
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${activeJob.config.appName} (${activeJob.config.packageId})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Pipeline: ${activeJob.pipelineType.displayName}",
                            fontSize = 11.sp,
                            color = SlateTextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        // Mini progress bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(SlateSurfaceElevated)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(activeJob.progressPercent / 100f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(ForgeCyan)
                            )
                        }
                    }
                }
            }
        }

        // Metrics Grid (2x2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatMetricCard(
                        title = "Completed",
                        value = "${metrics.totalCompletedBuilds}",
                        subtitle = "${metrics.successRatePercent}% pass rate",
                        icon = Icons.Default.CheckCircle,
                        accentColor = ForgeEmerald,
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        title = "Workers",
                        value = "${metrics.activeWorkersCount} / ${metrics.maxWorkersCount}",
                        subtitle = "Docker isolated sandboxes",
                        icon = Icons.Default.Memory,
                        accentColor = ForgeCyan,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatMetricCard(
                        title = "Storage",
                        value = "${metrics.storageUsedMb.toInt()} MB",
                        subtitle = "24h auto-delete active",
                        icon = Icons.Default.Storage,
                        accentColor = ForgeAmber,
                        modifier = Modifier.weight(1f)
                    )
                    StatMetricCard(
                        title = "Avg Duration",
                        value = "${metrics.averageBuildDurationSec}s",
                        subtitle = "Fast Gradle & Vite cache",
                        icon = Icons.Default.Android,
                        accentColor = Color(0xFFA78BFA),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Recent Build History
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT CONVERSIONS & BUILDS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextSecondary,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "${history.size} builds recorded",
                    fontSize = 11.sp,
                    color = SlateTextSecondary
                )
            }
        }

        if (history.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SlateSurfaceDark)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Android,
                            contentDescription = null,
                            tint = SlateTextSecondary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No projects converted yet",
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Upload a ZIP archive or try a preloaded AI sample to generate your first APK.",
                            fontSize = 11.sp,
                            color = SlateTextSecondary
                        )
                    }
                }
            }
        } else {
            items(history) { job ->
                val projectType = runCatching { ProjectType.valueOf(job.projectType) }.getOrDefault(ProjectType.REACT_VITE)
                val isSuccess = job.status == BuildStatus.COMPLETED.name
                val isFailed = job.status == BuildStatus.FAILED.name
                val formattedDate = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(job.createdAtMs))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SlateSurfaceDark)
                        .border(1.dp, SlateBorderDark, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                        .testTag("history_item_${job.buildId}")
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = job.appName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateTextPrimary
                                )
                                Text(
                                    text = job.packageId,
                                    fontSize = 11.sp,
                                    color = SlateTextSecondary
                                )
                            }

                            // Status badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        when {
                                            isSuccess -> ForgeEmerald.copy(alpha = 0.15f)
                                            isFailed -> ForgeRose.copy(alpha = 0.15f)
                                            else -> ForgeCyan.copy(alpha = 0.15f)
                                        }
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = job.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        isSuccess -> ForgeEmerald
                                        isFailed -> ForgeRose
                                        else -> ForgeCyan
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FrameworkBadge(projectType = projectType)

                            Text(
                                text = "Build: $formattedDate",
                                fontSize = 10.sp,
                                color = SlateTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Footer actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSuccess && job.apkSizeMb != null) {
                                Text(
                                    text = "APK: ${job.apkSizeMb} MB | AAB: ${job.aabSizeMb} MB",
                                    fontSize = 11.sp,
                                    color = SlateTextSecondary
                                )

                                Row {
                                    OutlinedButton(
                                        onClick = { onDownloadBuild(job.buildId) },
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Download", fontSize = 11.sp)
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    OutlinedButton(
                                        onClick = { onPurgeBuild(job.buildId) },
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(34.dp),
                                        enabled = !job.isPurged
                                    ) {
                                        Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (job.isPurged) "Purged" else "Purge", fontSize = 11.sp)
                                    }
                                }
                            } else if (isFailed) {
                                Text(
                                    text = job.errorMessage ?: "Build error occurred",
                                    fontSize = 11.sp,
                                    color = ForgeRose,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
