package com.romanpolach.peacefulflight.kmp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Bottom navigation items - matches original exactly
 */
sealed class BottomNavItem(
    val route: String,
    val labelKey: String,
    val icon: ImageVector
) {
    data object Cockpit : BottomNavItem("cockpit", "nav_cockpit", Icons.Default.Home)
    data object Learn : BottomNavItem("learn", "nav_learn", Icons.Default.School)
    data object Sos : BottomNavItem("sos", "nav_sos", Icons.Default.MedicalServices)
    data object Tools : BottomNavItem("tools", "nav_tools", Icons.Default.Build)
}

/**
 * All app screens (routes) - matches original exactly
 */
sealed class Screen(val route: String) {
    data object RidingTheWave : Screen("riding_the_wave")
    data object GForceMonitorStandalone : Screen("g_force")
    data object PostponeTheWorry : Screen("postpone_the_worry")
    data object WorryOlympics : Screen("worry_olympics")
    data object FacingTheFear : Screen("facing_the_fear")
    data object RealityCheck : Screen("reality_check")
    data object SafetyFacts : Screen("safety_facts")
    data object AcceptanceMeditation : Screen("acceptance_meditation")
    data object CatastrophicThinking : Screen("catastrophic_thinking")
    data object LearnDetail : Screen("learn_detail/{itemId}") {
        fun createRoute(itemId: String) = "learn_detail/$itemId"
        val navArguments = listOf(
            navArgument("itemId") { type = NavType.StringType }
        )
    }

    data object HelpOptions : Screen("help_options")
    data object SelfCompassion : Screen("self_compassion")
    data object VoiceSettings : Screen("voice_settings")
}
