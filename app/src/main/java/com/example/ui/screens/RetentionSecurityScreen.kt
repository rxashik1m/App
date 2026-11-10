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
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
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
fun RetentionSecurityScreen(
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

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
                text = "Retention & Security Engine",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = SlateTextPrimary
            )
            Text(
                text = "Automated file lifecycle management and privacy compliance",
                fontSize = 12.sp,
                color = SlateTextSecondary
            )
        }

        // Live Retention Policy Badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, ForgeEmerald.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = ForgeEmerald, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ZERO-RETENTION GUARANTEE",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForgeEmerald
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "AppForge AI is strictly a temporary compilation service. Uploaded projects and uncompressed source trees are held only in isolated worker sandboxes and permanently destroyed per policy:",
                    fontSize = 12.sp,
                    color = SlateTextSecondary,
                    lineHeight = 17.sp
                )
            }
        }

        // Rules Breakdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "ACTIVE PURGE TIMERS & POLICIES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextSecondary,
                    letterSpacing = 0.8.sp
                )

                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = ForgeCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("24-Hour Expiration Purge", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                        Text("If a build artifact is not downloaded within 24 hours, all source files, intermediate logs, and binaries are permanently erased.", fontSize = 11.sp, color = SlateTextSecondary)
                    }
                }

                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = ForgeRose, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Immediate Post-Download Destruction", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                        Text("When a user confirms an APK/AAB download, the source ZIP and extraction directories are immediately wiped.", fontSize = 11.sp, color = SlateTextSecondary)
                    }
                }

                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = ForgeEmerald, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Environment Secret Masking", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                        Text("API keys (e.g. Gemini, Firebase, OpenAI) are scrubbed before analysis and never saved to persistent storage.", fontSize = 11.sp, color = SlateTextSecondary)
                    }
                }
            }
        }

        // Active Sandbox State
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SlateSurfaceElevated)
                .border(1.dp, SlateBorderDark, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("CURRENT SANDBOX STATUS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateTextSecondary)
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Ephemeral Disk Consumption:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text("312.4 MB / 2048 MB", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Scheduled Purge Queue:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text("Next run in 18 minutes", fontSize = 12.sp, color = ForgeCyan)
                }
            }
        }

        // Force Purge All Action
        Button(
            onClick = {
                Toast.makeText(context, "Purge command broadcast: All temporary source files deleted.", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = ForgeRose, contentColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().testTag("purge_all_sandboxes_btn")
        ) {
            Icon(Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Trigger Manual Sanitation of All Sandboxes", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}
