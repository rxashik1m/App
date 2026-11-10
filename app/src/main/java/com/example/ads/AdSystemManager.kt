package com.example.ads

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ForgeAmber
import com.example.ui.theme.ForgeCyan
import com.example.ui.theme.ForgeEmerald
import com.example.ui.theme.SlateBorderDark
import com.example.ui.theme.SlateSurfaceDark
import com.example.ui.theme.SlateSurfaceElevated
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import kotlinx.coroutines.delay

data class AdCampaign(
    val title: String,
    val sponsor: String,
    val description: String,
    val callToAction: String,
    val accentColor: Color
)

object AdInventory {
    val campaigns = listOf(
        AdCampaign(
            title = "Google Cloud Run for Android Workers",
            sponsor = "Google Cloud Platform",
            description = "Run scalable containerized Gradle and Docker build workers with zero server management.",
            callToAction = "Deploy Free Sandbox",
            accentColor = Color(0xFF4285F4)
        ),
        AdCampaign(
            title = "Firebase AI Logic & App Check",
            sponsor = "Firebase",
            description = "Secure your Gemini API calls and Firestore rules with enterprise-grade cryptographic attestation.",
            callToAction = "Learn More",
            accentColor = Color(0xFFFFCA28)
        ),
        AdCampaign(
            title = "Android Studio Meerkat 2026",
            sponsor = "Android Developer Tools",
            description = "Modern Compose preview rendering and AGP 9.1 compiler optimizations out of the box.",
            callToAction = "Get Android Studio",
            accentColor = Color(0xFF3DDC84)
        )
    )
}

@Composable
fun RewardedAdDialog(
    isOpen: Boolean,
    onRewardEarned: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val campaign = remember { AdInventory.campaigns.random() }
    var countdownSec by remember { mutableIntStateOf(6) }
    var isFinished by remember { mutableStateOf(false) }

    LaunchedEffect(isOpen) {
        while (countdownSec > 0) {
            delay(1000)
            countdownSec--
        }
        isFinished = true
    }

    Dialog(onDismissRequest = {
        if (isFinished) onDismiss()
    }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SlateSurfaceDark)
                .border(1.dp, campaign.accentColor.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                .padding(20.dp)
                .testTag("rewarded_ad_dialog")
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1E293B))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "DEVELOPER SPONSOR AD",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = campaign.accentColor
                        )
                    }

                    if (isFinished) {
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close ad", tint = SlateTextSecondary)
                        }
                    } else {
                        Text(
                            text = "Reward in ${countdownSec}s",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForgeAmber
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ad Visual Media Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(campaign.accentColor.copy(alpha = 0.25f), Color(0xFF0F172A))
                            )
                        )
                        .border(1.dp, campaign.accentColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = campaign.accentColor,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = campaign.sponsor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = campaign.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = SlateTextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = campaign.description,
                    fontSize = 12.sp,
                    color = SlateTextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (isFinished) {
                    Button(
                        onClick = {
                            onRewardEarned(campaign.title)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForgeEmerald,
                            contentColor = Color(0xFF031A24)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("claim_reward_btn")
                    ) {
                        Icon(Icons.Default.Stars, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Claim +1 Turbo Build Credit", fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                } else {
                    LinearProgressIndicator(
                        progress = { (6 - countdownSec) / 6f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = campaign.accentColor,
                        trackColor = SlateBorderDark
                    )
                }
            }
        }
    }
}

@Composable
fun DeveloperBannerAd(
    modifier: Modifier = Modifier,
    onWatchRewarded: () -> Unit
) {
    var isDismissed by remember { mutableStateOf(false) }
    if (isDismissed) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SlateSurfaceElevated)
            .border(1.dp, ForgeCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag("developer_banner_ad")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ForgeCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RocketLaunch,
                        contentDescription = null,
                        tint = ForgeCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "TURBO BUILD ACCELERATOR",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForgeCyan
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(ForgeAmber.copy(alpha = 0.2f))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text("FREE AD REWARD", fontSize = 9.sp, color = ForgeAmber, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(
                        text = "Watch a 6s developer ad to unlock instant compilation queue",
                        fontSize = 10.sp,
                        color = SlateTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onWatchRewarded,
                colors = ButtonDefaults.buttonColors(containerColor = ForgeCyan, contentColor = Color(0xFF021B24)),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Watch", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
