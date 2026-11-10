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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProjectScanResult
import com.example.ui.Screen
import com.example.ui.components.AiToolBadge
import com.example.ui.components.FrameworkBadge
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
fun ProjectScannerScreen(
    scanResult: ProjectScanResult?,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    if (scanResult == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(SlateBgDark)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = ForgeCyan,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No Project Scanned Yet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Please upload an archive or pick an AI sample to run the universal scanner.",
                    fontSize = 12.sp,
                    color = SlateTextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onNavigate(Screen.UPLOAD) },
                    colors = ButtonDefaults.buttonColors(containerColor = ForgeCyan, contentColor = Color(0xFF031A24)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Go to Upload", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBgDark)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Scanner Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Project Audit Report",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = SlateTextPrimary
                )
                Text(
                    text = scanResult.projectName,
                    fontSize = 13.sp,
                    color = ForgeCyan,
                    fontWeight = FontWeight.Bold
                )
            }

            // Health Score Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (scanResult.healthScore >= 90) ForgeEmerald.copy(alpha = 0.15f)
                        else ForgeAmber.copy(alpha = 0.15f)
                    )
                    .border(
                        1.dp,
                        if (scanResult.healthScore >= 90) ForgeEmerald else ForgeAmber,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${scanResult.healthScore}%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = if (scanResult.healthScore >= 90) ForgeEmerald else ForgeAmber
                    )
                    Text(
                        text = "HEALTH",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextSecondary
                    )
                }
            }
        }

        // Framework & AI Source Detection Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "FRAMEWORK & ARCHITECTURE DETECTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextSecondary,
                    letterSpacing = 0.8.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FrameworkBadge(projectType = scanResult.projectType)
                    AiToolBadge(tool = scanResult.detectedAiTool)
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Entry Point:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text(scanResult.entryPoint, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary, fontFamily = FontFamily.Monospace)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Archive Contents:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text("${scanResult.totalFilesCount} files (${scanResult.totalSizeBytes / 1024} KB)", fontSize = 12.sp, color = SlateTextPrimary)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Recommended Pipeline:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text(scanResult.recommendedPipeline.displayName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForgeCyan)
                }
            }
        }

        // Warnings / Architecture Advisory (if any)
        if (scanResult.warnings.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ForgeAmber.copy(alpha = 0.1f))
                    .border(1.dp, ForgeAmber.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.ReportProblem,
                        contentDescription = null,
                        tint = ForgeAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Conversion Architecture Notice",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForgeAmber
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        scanResult.warnings.forEach { warning ->
                            Text(
                                text = "• $warning",
                                fontSize = 11.sp,
                                color = SlateTextPrimary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Environment Variables (Masked)
        if (scanResult.detectedEnvVars.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SlateSurfaceDark)
                    .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Key, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "DETECTED ENVIRONMENT CONFIGURATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextSecondary,
                            letterSpacing = 0.8.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    scanResult.detectedEnvVars.forEach { env ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(env.key, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = SlateTextPrimary)
                            Text(env.maskedValue, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = SlateTextSecondary)
                        }
                    }
                }
            }
        }

        // Dependencies Overview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Code, contentDescription = null, tint = ForgeEmerald, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DEPENDENCIES & MANIFEST AUDIT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                scanResult.dependencies.forEach { dep ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(dep.name, fontSize = 12.sp, color = SlateTextPrimary)
                        Text(dep.version, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = ForgeCyan)
                    }
                }

                if (scanResult.missingDependencies.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    scanResult.missingDependencies.forEach { missing ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = ForgeAmber, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(missing, fontSize = 11.sp, color = ForgeAmber)
                        }
                    }
                }
            }
        }

        // Bottom CTA Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { onNavigate(Screen.UPLOAD) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Re-scan", fontSize = 13.sp)
            }

            Button(
                onClick = { onNavigate(Screen.CUSTOMIZATION) },
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
                    .testTag("scanner_proceed_customization_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = ForgeCyan, contentColor = Color(0xFF031A24)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Customize & Build APK", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
