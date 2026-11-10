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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppCustomizationConfig
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
fun AppCustomizationScreen(
    currentConfig: AppCustomizationConfig,
    onConfigChange: (AppCustomizationConfig) -> Unit,
    onStartBuild: (Boolean) -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var appName by remember { mutableStateOf(currentConfig.appName) }
    var packageId by remember { mutableStateOf(currentConfig.packageId) }
    var versionName by remember { mutableStateOf(currentConfig.versionName) }
    var versionCode by remember { mutableStateOf(currentConfig.versionCode.toString()) }
    var selectedOrientation by remember { mutableStateOf(currentConfig.appOrientation) }
    var outputFormat by remember { mutableStateOf(currentConfig.outputFormat) }
    var buildType by remember { mutableStateOf(currentConfig.buildType) }
    var simulateFailure by remember { mutableStateOf(false) }
    var enableAdMob by remember { mutableStateOf(currentConfig.enableAdMob) }
    var admobAppId by remember { mutableStateOf(currentConfig.admobAppId) }
    var admobBannerId by remember { mutableStateOf(currentConfig.admobBannerId) }

    val availablePermissions = listOf(
        Pair("INTERNET", "Required for web views, API calls, and cloud databases"),
        Pair("ACCESS_NETWORK_STATE", "Check internet connectivity and offline status"),
        Pair("POST_NOTIFICATIONS", "Receive background alerts & push messages"),
        Pair("CAMERA", "Take pictures / scan barcodes with device camera")
    )

    var selectedPermissions by remember {
        mutableStateOf(currentConfig.permissions.toSet())
    }

    fun syncConfig() {
        onConfigChange(
            currentConfig.copy(
                appName = appName,
                packageId = packageId,
                versionName = versionName,
                versionCode = versionCode.toIntOrNull() ?: 1,
                appOrientation = selectedOrientation,
                outputFormat = outputFormat,
                buildType = buildType,
                permissions = selectedPermissions.toList(),
                enableAdMob = enableAdMob,
                admobAppId = admobAppId,
                admobBannerId = admobBannerId
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBgDark)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        Column {
            Text(
                text = "Android App Customizer",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = SlateTextPrimary
            )
            Text(
                text = "Configure manifest parameters, package naming, SDK limits and permissions",
                fontSize = 12.sp,
                color = SlateTextSecondary
            )
        }

        // Section 1: App Identity
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "APP IDENTITY & PACKAGE MANIFEST",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }

                // App Name
                OutlinedTextField(
                    value = appName,
                    onValueChange = {
                        appName = it
                        syncConfig()
                    },
                    label = { Text("App Launcher Name") },
                    modifier = Modifier.fillMaxWidth().testTag("custom_app_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForgeCyan,
                        unfocusedBorderColor = SlateBorderDark,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary,
                        focusedLabelColor = ForgeCyan,
                        unfocusedLabelColor = SlateTextSecondary
                    ),
                    singleLine = true
                )

                // Package ID
                OutlinedTextField(
                    value = packageId,
                    onValueChange = {
                        packageId = it
                        syncConfig()
                    },
                    label = { Text("Unique Application ID (package)") },
                    modifier = Modifier.fillMaxWidth().testTag("custom_package_id_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForgeCyan,
                        unfocusedBorderColor = SlateBorderDark,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary,
                        focusedLabelColor = ForgeCyan,
                        unfocusedLabelColor = SlateTextSecondary
                    ),
                    singleLine = true
                )

                // Version Name & Code
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = versionName,
                        onValueChange = {
                            versionName = it
                            syncConfig()
                        },
                        label = { Text("Version Name") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForgeCyan,
                            unfocusedBorderColor = SlateBorderDark,
                            focusedTextColor = SlateTextPrimary,
                            unfocusedTextColor = SlateTextPrimary
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = versionCode,
                        onValueChange = {
                            versionCode = it
                            syncConfig()
                        },
                        label = { Text("Version Code") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForgeCyan,
                            unfocusedBorderColor = SlateBorderDark,
                            focusedTextColor = SlateTextPrimary,
                            unfocusedTextColor = SlateTextPrimary
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // Section 2: Build & Output Format
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Build, contentDescription = null, tint = ForgeEmerald, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BUILD TARGET & BINARY FORMAT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }

                Text("Output Format:", fontSize = 12.sp, color = SlateTextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("APK", "AAB (Play Store)").forEach { format ->
                        val isSelected = outputFormat == if (format.startsWith("APK")) "APK" else "AAB"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ForgeCyan.copy(alpha = 0.15f) else SlateSurfaceElevated)
                                .border(1.dp, if (isSelected) ForgeCyan else SlateBorderDark, RoundedCornerShape(8.dp))
                                .clickable {
                                    outputFormat = if (format.startsWith("APK")) "APK" else "AAB"
                                    syncConfig()
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = format,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ForgeCyan else SlateTextPrimary
                            )
                        }
                    }
                }

                Text("Build Type:", fontSize = 12.sp, color = SlateTextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Release (Signed)", "Debug").forEach { type ->
                        val cleanType = if (type.startsWith("Release")) "Release" else "Debug"
                        val isSelected = buildType == cleanType
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ForgeEmerald.copy(alpha = 0.15f) else SlateSurfaceElevated)
                                .border(1.dp, if (isSelected) ForgeEmerald else SlateBorderDark, RoundedCornerShape(8.dp))
                                .clickable {
                                    buildType = cleanType
                                    syncConfig()
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ForgeEmerald else SlateTextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Android Permissions
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
                    Icon(Icons.Default.Security, contentDescription = null, tint = ForgeAmber, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ANDROID PERMISSIONS (LEAST-PRIVILEGE)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                availablePermissions.forEach { (perm, desc) ->
                    val isChecked = selectedPermissions.contains(perm)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedPermissions = if (isChecked) {
                                    selectedPermissions - perm
                                } else {
                                    selectedPermissions + perm
                                }
                                syncConfig()
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                selectedPermissions = if (it) selectedPermissions + perm else selectedPermissions - perm
                                syncConfig()
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = ForgeCyan,
                                checkmarkColor = Color(0xFF031A24),
                                uncheckedColor = SlateBorderDark
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(perm, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary, fontFamily = FontFamily.Monospace)
                            Text(desc, fontSize = 10.sp, color = SlateTextSecondary)
                        }
                    }
                }
            }
        }

        // Section: Ads System & Monetization Scaffolding
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚡", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "GOOGLE ADMOB MONETIZATION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForgeCyan,
                                letterSpacing = 0.8.sp
                            )
                        }
                        Text(
                            text = "Inject Google Mobile Ads SDK and banner ad units into generated APK",
                            fontSize = 11.sp,
                            color = SlateTextSecondary
                        )
                    }

                    Switch(
                        checked = enableAdMob,
                        onCheckedChange = {
                            enableAdMob = it
                            syncConfig()
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ForgeCyan,
                            checkedTrackColor = ForgeCyan.copy(alpha = 0.3f),
                            uncheckedThumbColor = SlateTextSecondary,
                            uncheckedTrackColor = SlateBorderDark
                        )
                    )
                }

                if (enableAdMob) {
                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = admobAppId,
                        onValueChange = {
                            admobAppId = it
                            syncConfig()
                        },
                        label = { Text("AdMob App ID") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForgeCyan,
                            unfocusedBorderColor = SlateBorderDark,
                            focusedTextColor = SlateTextPrimary,
                            unfocusedTextColor = SlateTextPrimary
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = admobBannerId,
                        onValueChange = {
                            admobBannerId = it
                            syncConfig()
                        },
                        label = { Text("Banner Ad Unit ID") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForgeCyan,
                            unfocusedBorderColor = SlateBorderDark,
                            focusedTextColor = SlateTextPrimary,
                            unfocusedTextColor = SlateTextPrimary
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // Section 4: Testing & Error Simulation Toggle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SlateSurfaceElevated)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.BugReport, contentDescription = null, tint = ForgeRose, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Simulate Build Failure", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                        Text("Test AI-assisted error diagnosis & one-click repair", fontSize = 10.sp, color = SlateTextSecondary)
                    }
                }

                Switch(
                    checked = simulateFailure,
                    onCheckedChange = { simulateFailure = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ForgeRose,
                        checkedTrackColor = ForgeRose.copy(alpha = 0.3f),
                        uncheckedThumbColor = SlateTextSecondary,
                        uncheckedTrackColor = SlateBorderDark
                    )
                )
            }
        }

        // Primary Action
        Button(
            onClick = {
                syncConfig()
                onStartBuild(simulateFailure)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("start_compilation_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = ForgeCyan, contentColor = Color(0xFF021B24)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Compile & Sign ${outputFormat}", fontWeight = FontWeight.Black, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
