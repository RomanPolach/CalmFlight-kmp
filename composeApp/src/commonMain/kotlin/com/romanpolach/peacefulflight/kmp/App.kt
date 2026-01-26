package com.romanpolach.peacefulflight.kmp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.romanpolach.peacefulflight.kmp.model.ThemeMode
import com.romanpolach.peacefulflight.kmp.ui.navigation.BottomNavItem
import com.romanpolach.peacefulflight.kmp.ui.theme.PeacefulFlightTheme
import com.romanpolach.peacefulflight.kmp.viewmodel.MainViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.end_flight_btn
import peacefulflight.composeapp.generated.resources.nav_cockpit
import peacefulflight.composeapp.generated.resources.nav_learn
import peacefulflight.composeapp.generated.resources.nav_sos
import peacefulflight.composeapp.generated.resources.nav_tools
import peacefulflight.composeapp.generated.resources.start_flight_btn

/**
 * Main App composable - entry point for Compose Multiplatform
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
fun App(
    viewModel: MainViewModel = koinViewModel()
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val isSystemDark = isSystemInDarkTheme()

    val useDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemDark
    }

    PeacefulFlightTheme(darkTheme = useDarkTheme) {
        MainScreen(viewModel)
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isFlightActive by viewModel.isFlightActive.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val bottomNavItems = listOf(
        BottomNavItem.Cockpit,
        BottomNavItem.Learn,
        BottomNavItem.Sos,
        BottomNavItem.Tools
    )

    // Check if current route is a root tab screen
    val isRootTabScreen = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isRootTabScreen) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    val items = listOf(
                        BottomNavItem.Cockpit,
                        BottomNavItem.Learn,
                        null, // Placeholder for FAB
                        BottomNavItem.Sos,
                        BottomNavItem.Tools
                    )

                    items.forEach { item ->
                        if (item == null) {
                            // Placeholder for the FAB in the middle
                            NavigationBarItem(
                                selected = false,
                                onClick = { },
                                icon = { },
                                enabled = false
                            )
                        } else {
                            val labelRes = when (item.labelKey) {
                                "nav_cockpit" -> Res.string.nav_cockpit
                                "nav_learn" -> Res.string.nav_learn
                                "nav_sos" -> Res.string.nav_sos
                                "nav_tools" -> Res.string.nav_tools
                                else -> Res.string.nav_cockpit
                            }

                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        item.icon,
                                        contentDescription = stringResource(labelRes)
                                    )
                                },
                                label = { Text(stringResource(labelRes)) },
                                selected = currentRoute == item.route,
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.6f
                                    ),
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.6f
                                    )
                                ),
                                onClick = {
                                    navController.navigate(item.route) {
                                        navController.graph.startDestinationRoute?.let { route ->
                                            popUpTo(route) {
                                                saveState = true
                                            }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            if (isRootTabScreen) {
                FloatingActionButton(
                    onClick = {
                        if (isFlightActive) {
                            viewModel.endFlight(5) // Default middle value for now
                        } else {
                            viewModel.startFlight(5)
                        }
                    },
                    containerColor = if (isFlightActive)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = if (isFlightActive)
                        MaterialTheme.colorScheme.onError
                    else
                        MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(72.dp)
                        .offset(y = 48.dp)
                ) {
                    Icon(
                        imageVector = if (isFlightActive)
                            Icons.Default.FlightLand
                        else
                            Icons.Default.FlightTakeoff,
                        contentDescription = stringResource(if (isFlightActive) Res.string.end_flight_btn else Res.string.start_flight_btn),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            startDestination = BottomNavItem.Cockpit.route
        )
    }
}