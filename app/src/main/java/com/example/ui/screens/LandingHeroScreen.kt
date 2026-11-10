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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
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
fun LandingHeroScreen(
    onNavigate: (Screen) -> Unit,
    onQuickStartSample: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBgDark)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Innovation Banner
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(ForgeCyan.copy(alpha = 0.12f))
                .border(1.dp, ForgeCyan.copy(alpha = 0.3f), RoundedCornerShape(30.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = ForgeCyan,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "NEXT-GEN ANDROID CONVERSION ENGINE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForgeCyan,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Title
        Text(
            text = "Convert Any AI Project\nInto Production APK & AAB",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = SlateTextPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Upload projects from Google AI Studio, Claude, Cursor, v0, Bolt.new or ChatGPT. Automatic framework detection, Android wrapper injection, Gradle compilation, and zero-retention security.",
            fontSize = 13.sp,
            color = SlateTextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { onNavigate(Screen.UPLOAD) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("landing_upload_cta"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForgeCyan,
                    contentColor = Color(0xFF021B24)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Upload Project", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            OutlinedButton(
                onClick = { onNavigate(Screen.DASHBOARD) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("landing_dashboard_cta"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ForgeCyan),
                border = androidx.compose.foundation.BorderStroke(1.dp, ForgeCyan.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Open Dashboard", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Supported AI Tools Badges Grid
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "SUPPORTED AI CODING SOURCES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextSecondary,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                val aiTools = listOf(
                    "Google AI Studio", "Cursor Composer", "Claude Sonnet",
                    "Bolt.new", "v0.dev", "Lovable.dev", "Windsurf Cascade", "ChatGPT Canvas"
                )

                // Grid chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    aiTools.take(4).forEach { tool ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(SlateSurfaceElevated)
                                .border(1.dp, SlateBorderDark, RoundedCornerShape(6.dp))
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tool, fontSize = 10.sp, color = SlateTextPrimary, maxLines = 1)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    aiTools.drop(4).forEach { tool ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(SlateSurfaceElevated)
                                .border(1.dp, SlateBorderDark, RoundedCornerShape(6.dp))
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tool, fontSize = 10.sp, color = SlateTextPrimary, maxLines = 1)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Instant Try Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(SlateSurfaceDark, Color(0xFF0F1829))
                    )
                )
                .border(1.dp, ForgeCyan.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = ForgeEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TRY PRELOADED AI SAMPLES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForgeEmerald
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Test the scanner, customization studio, and APK compiler instantly:",
                    fontSize = 12.sp,
                    color = SlateTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Samples list
                val samples = listOf(
                    Triple("google_ai_studio_react", "Google AI Studio: Gemini Canvas", "React + Vite + Gemini API"),
                    Triple("cursor_native_android", "Cursor: Compose Task Tracker", "Kotlin + Jetpack Compose Native"),
                    Triple("claude_html5_game", "Claude: Cyber Dash 2D Game", "HTML5 + Canvas + Howler.js"),
                    Triple("bolt_new_fullstack", "Bolt.new: Enterprise Task API", "Full-Stack Express + React")
                )

                samples.forEach { (id, title, desc) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SlateSurfaceElevated)
                            .border(1.dp, SlateBorderDark, RoundedCornerShape(8.dp))
                            .clickable { onQuickStartSample(id) }
                            .padding(10.dp)
                            .testTag("sample_item_$id")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                                Text(desc, fontSize = 10.sp, color = SlateTextSecondary)
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = ForgeCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Security & Privacy Guarantee
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF0F1E19))
                .border(1.dp, ForgeEmerald.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = ForgeEmerald,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Zero-Retention Privacy Architecture",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForgeEmerald
                )
                Text(
                    text = "Temporary source code and build containers are isolated in sandboxes and permanently deleted after download or 24 hours.",
                    fontSize = 10.sp,
                    color = Color(0xFFA7F3D0)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
