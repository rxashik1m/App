package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.ForgeCyan
import com.example.ui.theme.SlateBorderDark
import com.example.ui.theme.SlateSurfaceDark
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary

@Composable
fun AppForgeBottomNav(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .border(width = 1.dp, color = SlateBorderDark)
            .testTag("appforge_bottom_nav"),
        containerColor = SlateSurfaceDark,
        contentColor = SlateTextPrimary,
        tonalElevation = 8.dp
    ) {
        val navItems = listOf(
            Triple(Screen.DASHBOARD, "Dashboard", Icons.Default.Dashboard),
            Triple(Screen.UPLOAD, "Upload", Icons.Default.CloudUpload),
            Triple(Screen.SCANNER, "Scanner", Icons.Default.DocumentScanner),
            Triple(Screen.BUILD_PROGRESS, "Builder", Icons.Default.Build),
            Triple(Screen.DOWNLOAD_CENTER, "Downloads", Icons.Default.CloudDownload)
        )

        navItems.forEach { (screen, label, icon) ->
            val isSelected = currentScreen == screen ||
                    (screen == Screen.SCANNER && currentScreen == Screen.CUSTOMIZATION)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ForgeCyan,
                    selectedTextColor = ForgeCyan,
                    indicatorColor = ForgeCyan.copy(alpha = 0.15f),
                    unselectedIconColor = SlateTextSecondary,
                    unselectedTextColor = SlateTextSecondary
                ),
                modifier = Modifier.testTag("nav_item_${label.lowercase()}")
            )
        }
    }
}
