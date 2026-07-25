package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.DigiCamDrawerContent
import com.example.ui.screens.*
import com.example.ui.theme.DigiCamTheme
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val appTheme by viewModel.appTheme.collectAsState()
            val onboardingCompleted by viewModel.onboardingCompleted.collectAsState()
            val selectedPhoto by viewModel.selectedPhotoForDetail.collectAsState()

            DigiCamTheme(appTheme = appTheme) {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "camera"

                val startDestination = if (onboardingCompleted) "camera" else "onboarding"

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = currentRoute != "onboarding",
                    drawerContent = {
                        DigiCamDrawerContent(
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo("camera") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onCloseDrawer = {
                                coroutineScope.launch { drawerState.close() }
                            }
                        )
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("onboarding") {
                            OnboardingScreen(
                                currentTheme = appTheme,
                                onSelectTheme = { viewModel.setAppTheme(it) },
                                onFinishOnboarding = {
                                    viewModel.completeOnboarding()
                                    navController.navigate("camera") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("camera") {
                            CameraScreen(
                                viewModel = viewModel,
                                onOpenDrawer = { coroutineScope.launch { drawerState.open() } },
                                onNavigateGallery = { navController.navigate("gallery") },
                                onNavigateSettings = { navController.navigate("settings") }
                            )
                        }

                        composable("gallery") {
                            GalleryScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onSelectPhoto = { photo ->
                                    viewModel.setSelectedPhoto(photo)
                                    navController.navigate("photo_detail")
                                }
                            )
                        }

                        composable("photo_detail") {
                            selectedPhoto?.let { photo ->
                                PhotoDetailScreen(
                                    media = photo,
                                    viewModel = viewModel,
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateEditor = { navController.navigate("editor") }
                                )
                            }
                        }

                        composable("presets") {
                            PresetsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("filters") {
                            LiveFiltersScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("editor") {
                            PhotoEditorScreen(
                                media = selectedPhoto,
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("favorites") {
                            GalleryScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onSelectPhoto = { photo ->
                                    viewModel.setSelectedPhoto(photo)
                                    navController.navigate("photo_detail")
                                }
                            )
                        }

                        composable("settings") {
                            SettingsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("help") {
                            HelpFeedbackScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("about") {
                            AboutScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
