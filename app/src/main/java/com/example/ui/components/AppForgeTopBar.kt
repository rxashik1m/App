package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.Screen
import com.example.ui.theme.ForgeAmber
import com.example.ui.theme.ForgeCyan
import com.example.ui.theme.ForgeEmerald
import com.example.ui.theme.SlateBorderDark
import com.example.ui.theme.SlateSurfaceDark
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.google.firebase.auth.FirebaseUser

@Composable
fun AppForgeTopBar(
    currentScreen: Screen,
    currentUser: FirebaseUser?,
    turboCredits: Long = 2L,
    onOpenRewardedAd: () -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SlateSurfaceDark)
            .windowInsetsPadding(WindowInsets.statusBars)
            .border(width = 1.dp, color = SlateBorderDark)
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("appforge_top_bar")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo & Brand
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onNavigate(Screen.LANDING) }
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ForgeCyan.copy(alpha = 0.15f))
                        .border(1.dp, ForgeCyan, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡",
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "APPFORGE",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            letterSpacing = 0.5.sp,
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "AI",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = ForgeCyan
                        )
                    }
                    Text(
                        text = "Universal Compiler",
                        fontSize = 9.sp,
                        color = SlateTextSecondary
                    )
                }
            }

            // Right action icons
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Turbo Credits / Rewarded Ad trigger
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ForgeAmber.copy(alpha = 0.15f))
                        .border(1.dp, ForgeAmber.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .clickable { onOpenRewardedAd() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = ForgeAmber,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "$turboCredits Turbo (+Ad)",
                            fontSize = 10.sp,
                            color = ForgeAmber,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Account / Auth pill
                if (currentUser != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(ForgeEmerald.copy(alpha = 0.12f))
                            .border(1.dp, ForgeEmerald.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                            .clickable { onNavigate(Screen.SETTINGS) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(ForgeEmerald)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentUser.displayName?.split(" ")?.firstOrNull() ?: "Cloud",
                                fontSize = 10.sp,
                                color = ForgeEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF1E293B))
                            .clickable { onNavigate(Screen.AUTH) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Login, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sign In", fontSize = 10.sp, color = ForgeCyan, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = { onNavigate(Screen.ADMIN_DASHBOARD) },
                    modifier = Modifier.size(34.dp).testTag("topbar_admin_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin telemetry",
                        tint = if (currentScreen == Screen.ADMIN_DASHBOARD) ForgeCyan else SlateTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { onNavigate(Screen.SETTINGS) },
                    modifier = Modifier.size(34.dp).testTag("topbar_settings_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = if (currentScreen == Screen.SETTINGS) ForgeCyan else SlateTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
