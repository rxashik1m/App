package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SystemWorkerMetrics
import com.example.ui.Screen
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.ForgeCyan
import com.example.ui.theme.ForgeEmerald
import com.example.ui.theme.ForgeIndigo
import com.example.ui.theme.SlateBgDark
import com.example.ui.theme.SlateBorderDark
import com.example.ui.theme.SlateSurfaceDark
import com.example.ui.theme.SlateSurfaceElevated
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary

@Composable
fun AdminDashboardScreen(
    metrics: SystemWorkerMetrics,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBgDark)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Admin Telemetry & Infrastructure",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = SlateTextPrimary
            )
            Text(
                text = "Cluster health, Docker workers, compilation queue, and disk quotas",
                fontSize = 12.sp,
                color = SlateTextSecondary
            )
        }

        // Stats grid
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMetricCard(
                    title = "Total Builds",
                    value = "${metrics.totalCompletedBuilds + metrics.totalFailedBuilds}",
                    subtitle = "${metrics.totalCompletedBuilds} successful",
                    icon = Icons.Default.Speed,
                    accentColor = ForgeCyan,
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    title = "Active Queue",
                    value = "${metrics.queueLength} Jobs",
                    subtitle = "0 pending delay",
                    icon = Icons.Default.Dns,
                    accentColor = ForgeIndigo,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMetricCard(
                    title = "Worker Pool",
                    value = "4 / 8",
                    subtitle = "Autoscaling enabled",
                    icon = Icons.Default.Memory,
                    accentColor = ForgeEmerald,
                    modifier = Modifier.weight(1f)
                )
                StatMetricCard(
                    title = "Active Users",
                    value = "142",
                    subtitle = "SaaS developers",
                    icon = Icons.Default.People,
                    accentColor = ForgeCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Worker Nodes Health Table
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("DOCKER BUILD WORKER CLUSTER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateTextSecondary)
                Spacer(modifier = Modifier.height(12.dp))

                val workers = listOf(
                    Triple("worker-node-01", "Node v20 + Android SDK 36 (Vite/Capacitor)", "ONLINE"),
                    Triple("worker-node-02", "Gradle 8.11 + AGP 9.1.1 (Native Android)", "ONLINE"),
                    Triple("worker-node-03", "Flutter SDK 3.5.0 + Dart (Cross-platform)", "ONLINE"),
                    Triple("worker-node-04", "Metro Bundler + Hermes (React Native)", "BUSY")
                )

                workers.forEach { (name, env, status) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                            Text(env, fontSize = 10.sp, color = SlateTextSecondary)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (status == "ONLINE") ForgeEmerald.copy(alpha = 0.15f)
                                    else ForgeCyan.copy(alpha = 0.15f)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = status,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (status == "ONLINE") ForgeEmerald else ForgeCyan
                            )
                        }
                    }
                }
            }
        }

        // Storage Quota Meter
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceElevated)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Temporary Storage Quota", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                    Text("${metrics.storageUsedMb.toInt()} MB / ${metrics.storageLimitMb.toInt()} MB", fontSize = 12.sp, color = ForgeCyan)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (metrics.storageUsedMb / metrics.storageLimitMb).toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = ForgeCyan,
                    trackColor = SlateBorderDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Enforced 24h expiration prevents disk exhaustion.", fontSize = 11.sp, color = SlateTextSecondary)
            }
        }
    }
}
