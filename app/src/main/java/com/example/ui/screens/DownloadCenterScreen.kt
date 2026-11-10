package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BuildJob
import com.example.ui.Screen
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
fun DownloadCenterScreen(
    buildJob: BuildJob?,
    onPurgeNow: (String) -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var isDownloadedState by remember { mutableStateOf(false) }

    if (buildJob == null || buildJob.sha256Checksum == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(SlateBgDark)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CloudDownload, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("No Compiled Artifacts Available", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Run a conversion pipeline to generate downloadable APK and AAB binaries.", fontSize = 12.sp, color = SlateTextSecondary)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onNavigate(Screen.UPLOAD) },
                    colors = ButtonDefaults.buttonColors(containerColor = ForgeCyan, contentColor = Color(0xFF021B24))
                ) {
                    Text("Start New Conversion", fontWeight = FontWeight.Bold)
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
        // Header
        Column {
            Text(
                text = "APK & AAB Download Center",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = SlateTextPrimary
            )
            Text(
                text = "Production binaries signed and verified by AppForge AI",
                fontSize = 12.sp,
                color = SlateTextSecondary
            )
        }

        // Primary APK Download Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, ForgeCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .padding(16.dp)
                .testTag("apk_download_card")
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
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ForgeCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Android, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "${buildJob.config.appName}.apk",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )
                            Text(
                                text = "Universal Android Package (Debug / Sideload)",
                                fontSize = 11.sp,
                                color = SlateTextSecondary
                            )
                        }
                    }

                    Text(
                        text = "${buildJob.apkSizeMb ?: 12.4} MB",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = ForgeCyan
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        isDownloadedState = true
                        Toast.makeText(context, "APK Download Initiated: ${buildJob.config.appName}.apk", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForgeCyan, contentColor = Color(0xFF021B24)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("download_apk_button")
                ) {
                    Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Download APK Binary", fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }
        }

        // AAB (Google Play Bundle) Card
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
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ForgeEmerald.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ForgeEmerald, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "${buildJob.config.appName}.aab",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )
                            Text(
                                text = "Android App Bundle (Google Play Console)",
                                fontSize = 11.sp,
                                color = SlateTextSecondary
                            )
                        }
                    }

                    Text(
                        text = "${buildJob.aabSizeMb ?: 8.1} MB",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = ForgeEmerald
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "AAB Bundle Download Initiated: ${buildJob.config.appName}.aab", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Download AAB Bundle", fontSize = 13.sp)
                }
            }
        }

        // Cryptographic Verification Details
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PACKAGE INTEGRITY & SIGNING FINGERPRINT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Package ID:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text(buildJob.config.packageId, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary, fontFamily = FontFamily.Monospace)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Version:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text("${buildJob.config.versionName} (Code: ${buildJob.config.versionCode})", fontSize = 12.sp, color = SlateTextPrimary)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Signing Scheme:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text("APK v2 + v3 Scheme (apksigner)", fontSize = 12.sp, color = ForgeEmerald)
                }

                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text("SHA-256 Checksum:", fontSize = 11.sp, color = SlateTextSecondary)
                    Text(
                        text = buildJob.sha256Checksum ?: "",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = ForgeCyan,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                clipboardManager.setText(AnnotatedString(buildJob.sha256Checksum ?: ""))
                                Toast.makeText(context, "SHA-256 copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(vertical = 2.dp)
                    )
                }
            }
        }

        // Automatic File Deletion Warning & Instant Purge Action
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF1E1528))
                .border(1.dp, ForgeRose.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = ForgeRose, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Zero-Retention File Deletion Policy",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForgeRose
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "AppForge AI is a temporary conversion pipeline. Source code and generated artifacts are automatically purged from isolated storage within 24 hours.",
                    fontSize = 11.sp,
                    color = Color(0xFFFBCFE8),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        onPurgeNow(buildJob.buildId)
                        Toast.makeText(context, "Temporary files and build artifacts purged immediately.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth().testTag("instant_purge_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ForgeRose),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ForgeRose.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !buildJob.isPurged
                ) {
                    Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (buildJob.isPurged) "Source Code Already Purged" else "Purge Temporary Source Code Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
