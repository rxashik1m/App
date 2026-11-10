package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
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
fun ProjectUploadScreen(
    onZipSelected: (Uri, String) -> Unit,
    onSelectSample: (String) -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedUri = uri
            val name = uri.lastPathSegment ?: "project_archive.zip"
            selectedFileName = name
            onZipSelected(uri, name)
        }
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
                text = "Universal Project Uploader",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = SlateTextPrimary
            )
            Text(
                text = "Upload any AI-generated codebase or archive for automatic scanning",
                fontSize = 12.sp,
                color = SlateTextSecondary
            )
        }

        // Dropzone / File Picker Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SlateSurfaceDark)
                .border(
                    width = 2.dp,
                    color = if (selectedFileName != null) ForgeEmerald else ForgeCyan.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {
                    filePicker.launch(arrayOf("application/zip", "application/octet-stream", "*/*"))
                }
                .padding(28.dp)
                .testTag("upload_dropzone"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (selectedFileName != null) ForgeEmerald.copy(alpha = 0.15f)
                            else ForgeCyan.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (selectedFileName != null) Icons.Default.Check else Icons.Default.FolderZip,
                        contentDescription = null,
                        tint = if (selectedFileName != null) ForgeEmerald else ForgeCyan,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedFileName != null) {
                    Text(
                        text = selectedFileName ?: "",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Archive selected! Tap below to inspect structure",
                        fontSize = 12.sp,
                        color = ForgeEmerald
                    )
                } else {
                    Text(
                        text = "Tap to Select Project ZIP Archive",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Supports ZIP files generated by Google AI Studio, Cursor, Claude, v0, Bolt, Lovable, Windsurf, or custom archives (Max 150MB)",
                        fontSize = 11.sp,
                        color = SlateTextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        filePicker.launch(arrayOf("application/zip", "application/octet-stream", "*/*"))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgeCyan,
                        contentColor = Color(0xFF021B24)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("choose_file_btn")
                ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Browse Files", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Instant Preloaded AI Samples
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
                    Icon(Icons.Default.Speed, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "OR LOAD A PRE-SCANNED AI PROJECT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForgeCyan,
                        letterSpacing = 0.8.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                val samplePicks = listOf(
                    Triple("google_ai_studio_react", "Google AI Studio: Gemini Canvas", "React + Vite (Pipeline B)"),
                    Triple("cursor_native_android", "Cursor: Compose Task Tracker", "Native Android Kotlin (Pipeline C)"),
                    Triple("claude_html5_game", "Claude: Cyber Dash 2D Game", "HTML/JS Canvas (Pipeline A)"),
                    Triple("bolt_new_fullstack", "Bolt.new: Enterprise Task API", "Fullstack Express + React (Pipeline F)")
                )

                samplePicks.forEach { (id, title, pipeline) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SlateSurfaceElevated)
                            .border(1.dp, SlateBorderDark, RoundedCornerShape(8.dp))
                            .clickable { onSelectSample(id) }
                            .padding(12.dp)
                            .testTag("upload_sample_$id")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                                Text(pipeline, fontSize = 10.sp, color = SlateTextSecondary)
                            }
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Security Checklist Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F1728))
                .border(1.dp, SlateBorderDark, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = ForgeEmerald, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Automated Archive Security Protocols",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForgeEmerald
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Path traversal (Zip Slip ../) sanitization enabled", fontSize = 11.sp, color = SlateTextSecondary)
                Text("• Safe uncompressed limit enforced (max 150MB)", fontSize = 11.sp, color = SlateTextSecondary)
                Text("• Environment secrets (.env) masked before inspection", fontSize = 11.sp, color = SlateTextSecondary)
                Text("• Automatic 24-hour sandbox purge policy", fontSize = 11.sp, color = SlateTextSecondary)
            }
        }
    }
}
