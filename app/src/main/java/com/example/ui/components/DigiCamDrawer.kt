package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class DrawerDestination(val route: String, val title: String, val icon: ImageVector) {
    object Camera : DrawerDestination("camera", "Camera", Icons.Default.PhotoCamera)
    object Gallery : DrawerDestination("gallery", "Gallery", Icons.Default.PhotoLibrary)
    object Presets : DrawerDestination("presets", "Presets", Icons.Default.Tune)
    object Filters : DrawerDestination("filters", "Live Filters", Icons.Default.AutoFixHigh)
    object Editor : DrawerDestination("editor", "Photo Editor", Icons.Default.Edit)
    object Favorites : DrawerDestination("favorites", "Favorites", Icons.Default.Favorite)
    object Settings : DrawerDestination("settings", "Settings", Icons.Default.Settings)
    object Help : DrawerDestination("help", "Help & Feedback", Icons.Default.HelpOutline)
    object About : DrawerDestination("about", "About DIGICAM", Icons.Default.Info)
}

@Composable
fun DigiCamDrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    val bentoGreen = Color(0xFFB4C79F)

    ModalDrawerSheet(
        drawerContainerColor = Color(0xFF141414),
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp, top = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(bentoGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color(0xFF0F0F0F)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "DIGICAM",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Bento Grid Edition • v1.0",
                            fontSize = 11.sp,
                            color = bentoGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp), color = Color.White.copy(alpha = 0.12f))

            val items = listOf(
                DrawerDestination.Camera,
                DrawerDestination.Gallery,
                DrawerDestination.Presets,
                DrawerDestination.Filters,
                DrawerDestination.Editor,
                DrawerDestination.Favorites,
                DrawerDestination.Settings,
                DrawerDestination.Help,
                DrawerDestination.About
            )

            items.forEach { dest ->
                val isSelected = currentRoute == dest.route
                NavigationDrawerItem(
                    label = { Text(dest.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                    icon = { Icon(dest.icon, contentDescription = dest.title) },
                    selected = isSelected,
                    onClick = {
                        onCloseDrawer()
                        onNavigate(dest.route)
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = bentoGreen,
                        selectedIconColor = Color(0xFF0F0F0F),
                        selectedTextColor = Color(0xFF0F0F0F),
                        unselectedContainerColor = Color.Transparent,
                        unselectedIconColor = Color.White.copy(alpha = 0.7f),
                        unselectedTextColor = Color.White.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .testTag("drawer_item_${dest.route}")
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF222222))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(bentoGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Made with ❤️ by Rahul Shah",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
