package com.example.ui.screens

import android.Manifest
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppTheme
import com.example.ui.theme.getThemePalette
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OnboardingScreen(
    currentTheme: AppTheme,
    onSelectTheme: (AppTheme) -> Unit,
    onFinishOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(0) }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    )

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .weight(1f)
                            .clip(CircleShape)
                            .background(
                                if (index <= step) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            )
                    )
                }
            }

            AnimatedContent(targetState = step, label = "onboarding_step") { currentStep ->
                when (currentStep) {
                    0 -> OnboardingWelcomeStep(
                        onRequestPermissions = { permissionsState.launchMultiplePermissionRequest() },
                        permissionsGranted = permissionsState.allPermissionsGranted
                    )
                    1 -> OnboardingThemeStep(
                        selectedTheme = currentTheme,
                        onSelectTheme = onSelectTheme
                    )
                    2 -> OnboardingWalkthroughStep()
                }
            }

            // Bottom Navigation Button
            Button(
                onClick = {
                    if (step < 2) {
                        step++
                    } else {
                        onFinishOnboarding()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .testTag("onboarding_next_button")
            ) {
                Text(
                    text = if (step == 2) "GET STARTED" else "CONTINUE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun OnboardingWelcomeStep(
    onRequestPermissions: () -> Unit,
    permissionsGranted: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Camera,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome to DIGICAM",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "A modern aesthetic digital camera inspired by compact digicams and film photography with live color grading.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (permissionsGranted) Icons.Default.CheckCircle else Icons.Default.Security,
                    contentDescription = null,
                    tint = if (permissionsGranted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (permissionsGranted) "Permissions Granted!" else "Camera & Storage Access Required",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "DIGICAM requires access to capture photos and save them directly to your gallery.",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (!permissionsGranted) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onRequestPermissions,
                        modifier = Modifier.testTag("grant_permissions_button")
                    ) {
                        Text("GRANT PERMISSIONS")
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingThemeStep(
    selectedTheme: AppTheme,
    onSelectTheme: (AppTheme) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
    ) {
        Text(
            text = "Choose Your Vibe",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Select a pastel aesthetic color scheme for your camera interface.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(340.dp)
        ) {
            items(AppTheme.values()) { theme ->
                val isSelected = theme == selectedTheme
                val palette = getThemePalette(theme, isDark = false)

                Box(
                    modifier = Modifier
                        .height(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(palette.surface)
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) palette.primary else palette.surfaceVariant,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onSelectTheme(theme) }
                        .padding(12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(palette.primary)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = theme.displayName,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = palette.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingWalkthroughStep() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
    ) {
        Text(
            text = "Everything You Need",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(20.dp))

        val features = listOf(
            Triple(Icons.Default.Tune, "Pro Manual Controls", "ISO, Shutter speed, White Balance, Manual focus, EV compensation, and RAW capture."),
            Triple(Icons.Default.AutoFixHigh, "28 Live Aesthetic Filters", "Real-time film grain, dust, soft glow, golden hour, and retro warm tones."),
            Triple(Icons.Default.Edit, "Non-Destructive Editor", "Tweak highlights, shadows, grain, temperature, HSL, and before/after comparison.")
        )

        features.forEach { (icon, title, desc) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
