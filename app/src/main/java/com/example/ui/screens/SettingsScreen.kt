package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.ForgeCyan
import com.example.ui.theme.ForgeEmerald
import com.example.ui.theme.SlateBgDark
import com.example.ui.theme.SlateBorderDark
import com.example.ui.theme.SlateSurfaceDark
import com.example.ui.theme.SlateSurfaceElevated
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary

@Composable
fun SettingsScreen(
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var keyAlias by remember { mutableStateOf("upload") }
    var keystorePath by remember { mutableStateOf("my-upload-key.jks") }
    var cloudWorkerEndpoint by remember { mutableStateOf("https://worker.appforge.ai/api/v1") }

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
                text = "Settings & Signing Credentials",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = SlateTextPrimary
            )
            Text(
                text = "Manage release signing keys and cloud compilation endpoints",
                fontSize = 12.sp,
                color = SlateTextSecondary
            )
        }

        // Account Profile Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ForgeCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Developer Account", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                    Text("rxashik100k@gmail.com", fontSize = 12.sp, color = ForgeCyan)
                    Text("Tier: Universal Studio Pro (Unlimited Builds)", fontSize = 10.sp, color = SlateTextSecondary)
                }
            }
        }

        // Keystore Signing Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Key, contentDescription = null, tint = ForgeEmerald, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RELEASE KEYSTORE CREDENTIALS (V2/V3 SIGNING)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }

                OutlinedTextField(
                    value = keystorePath,
                    onValueChange = { keystorePath = it },
                    label = { Text("Keystore File Path") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForgeCyan,
                        unfocusedBorderColor = SlateBorderDark,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    )
                )

                OutlinedTextField(
                    value = keyAlias,
                    onValueChange = { keyAlias = it },
                    label = { Text("Key Alias") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForgeCyan,
                        unfocusedBorderColor = SlateBorderDark,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    )
                )
            }
        }

        // Cloud Worker Endpoint
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CLOUD COMPILATION WORKER ORCHESTRATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }

                OutlinedTextField(
                    value = cloudWorkerEndpoint,
                    onValueChange = { cloudWorkerEndpoint = it },
                    label = { Text("Worker Dispatch Gateway URL") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForgeCyan,
                        unfocusedBorderColor = SlateBorderDark,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    )
                )
            }
        }

        Button(
            onClick = {
                Toast.makeText(context, "Preferences successfully saved.", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = ForgeCyan, contentColor = Color(0xFF021B24)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().testTag("save_settings_btn")
        ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Save Preferences", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}
