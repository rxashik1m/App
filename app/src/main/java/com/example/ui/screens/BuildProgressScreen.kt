package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.model.BuildJob
import com.example.data.model.BuildStatus
import com.example.ui.Screen
import com.example.ui.components.TerminalLogViewer
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

@Composable
fun BuildProgressScreen(
    buildJob: BuildJob?,
    onApplyAiRepair: () -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    if (buildJob == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(SlateBgDark)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.HourglassTop, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("No Active Build Process", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Start a build from the Customization panel or Upload screen.", fontSize = 12.sp, color = SlateTextSecondary)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onNavigate(Screen.UPLOAD) },
                    colors = ButtonDefaults.buttonColors(containerColor = ForgeCyan, contentColor = Color(0xFF021B24))
                ) {
                    Text("Go to Upload", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    val isCompleted = buildJob.status == BuildStatus.COMPLETED
    val isFailed = buildJob.status == BuildStatus.FAILED

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBgDark)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Pipeline Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Live Build Pipeline",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = SlateTextPrimary
                )
                Text(
                    text = "${buildJob.config.appName} (${buildJob.pipelineType.displayName})",
                    fontSize = 12.sp,
                    color = ForgeCyan,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when {
                            isCompleted -> ForgeEmerald.copy(alpha = 0.15f)
                            isFailed -> ForgeRose.copy(alpha = 0.15f)
                            else -> ForgeCyan.copy(alpha = 0.15f)
                        }
                    )
                    .border(
                        1.dp,
                        when {
                            isCompleted -> ForgeEmerald
                            isFailed -> ForgeRose
                            else -> ForgeCyan
                        },
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = buildJob.status.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = when {
                        isCompleted -> ForgeEmerald
                        isFailed -> ForgeRose
                        else -> ForgeCyan
                    }
                )
            }
        }

        // Live Progress Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
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
                                .background(
                                    when {
                                        isCompleted -> ForgeEmerald
                                        isFailed -> ForgeRose
                                        else -> ForgeCyan
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = buildJob.currentStageName.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                    }
                    Text(
                        text = "${buildJob.progressPercent}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = ForgeCyan
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { buildJob.progressPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = when {
                        isCompleted -> ForgeEmerald
                        isFailed -> ForgeRose
                        else -> ForgeCyan
                    },
                    trackColor = SlateSurfaceElevated
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Build Metadata line
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Target: ${buildJob.config.outputFormat} (${buildJob.config.buildType})", fontSize = 11.sp, color = SlateTextSecondary)
                    Text("Worker: docker-vm-az1", fontSize = 11.sp, color = SlateTextSecondary)
                }
            }
        }

        // Live Cyber Terminal Log Viewer
        TerminalLogViewer(logs = buildJob.logs)

        // Error Analysis & One-Click AI Repair Banner
        AnimatedVisibility(visible = isFailed) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ForgeRose.copy(alpha = 0.1f))
                    .border(1.dp, ForgeRose.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = ForgeRose, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI BUILD ERROR DIAGNOSTICS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForgeRose
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = buildJob.errorMessage ?: "Build process halted due to runtime mismatch.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Suggested Solution: ${buildJob.suggestedFix}",
                        fontSize = 11.sp,
                        color = Color(0xFFFECDD3)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onApplyAiRepair,
                        colors = ButtonDefaults.buttonColors(containerColor = ForgeEmerald, contentColor = Color(0xFF031A24)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("ai_repair_button")
                    ) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Apply AI Repair & Recompile", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Success Card Banner
        AnimatedVisibility(visible = isCompleted) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ForgeEmerald.copy(alpha = 0.1f))
                    .border(1.dp, ForgeEmerald.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ForgeEmerald, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Compilation & Signing Successful!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForgeEmerald)
                            Text("Binary ready: ${buildJob.apkSizeMb} MB APK / ${buildJob.aabSizeMb} MB AAB", fontSize = 11.sp, color = SlateTextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onNavigate(Screen.DOWNLOAD_CENTER) },
                        colors = ButtonDefaults.buttonColors(containerColor = ForgeCyan, contentColor = Color(0xFF021B24)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("proceed_to_download_btn")
                    ) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Proceed to Download Center", fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
