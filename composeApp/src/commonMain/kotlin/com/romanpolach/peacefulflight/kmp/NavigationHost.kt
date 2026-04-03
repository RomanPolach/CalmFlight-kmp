package com.romanpolach.peacefulflight.kmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.romanpolach.peacefulflight.kmp.ui.navigation.BottomNavItem
import com.romanpolach.peacefulflight.kmp.ui.navigation.Screen
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.am_intro
import peacefulflight.composeapp.generated.resources.am_part_1
import peacefulflight.composeapp.generated.resources.am_part_2
import peacefulflight.composeapp.generated.resources.am_part_3
import peacefulflight.composeapp.generated.resources.am_part_4
import peacefulflight.composeapp.generated.resources.am_part_5
import peacefulflight.composeapp.generated.resources.am_part_6
import peacefulflight.composeapp.generated.resources.am_title
import peacefulflight.composeapp.generated.resources.cloud_meditation_footer
import peacefulflight.composeapp.generated.resources.cloud_meditation_part_1
import peacefulflight.composeapp.generated.resources.cloud_meditation_part_2
import peacefulflight.composeapp.generated.resources.cloud_meditation_part_3
import peacefulflight.composeapp.generated.resources.cloud_meditation_part_4
import peacefulflight.composeapp.generated.resources.cloud_meditation_part_5
import peacefulflight.composeapp.generated.resources.cloud_meditation_part_6
import peacefulflight.composeapp.generated.resources.cloud_meditation_part_7
import peacefulflight.composeapp.generated.resources.cloud_meditation_part_8
import peacefulflight.composeapp.generated.resources.cloud_meditation_part_9
import peacefulflight.composeapp.generated.resources.ct_investigation_part_1
import peacefulflight.composeapp.generated.resources.ct_investigation_part_2
import peacefulflight.composeapp.generated.resources.ct_investigation_part_3
import peacefulflight.composeapp.generated.resources.ct_investigation_part_4
import peacefulflight.composeapp.generated.resources.ct_investigation_part_5
import peacefulflight.composeapp.generated.resources.ct_investigation_part_6
import peacefulflight.composeapp.generated.resources.ct_investigation_part_7
import peacefulflight.composeapp.generated.resources.ct_title
import peacefulflight.composeapp.generated.resources.ftf_title
import peacefulflight.composeapp.generated.resources.ptw_intro
import peacefulflight.composeapp.generated.resources.ptw_step_1
import peacefulflight.composeapp.generated.resources.ptw_step_2
import peacefulflight.composeapp.generated.resources.ptw_step_3
import peacefulflight.composeapp.generated.resources.ptw_step_4
import peacefulflight.composeapp.generated.resources.ptw_step_5
import peacefulflight.composeapp.generated.resources.ptw_step_6
import peacefulflight.composeapp.generated.resources.ptw_title
import peacefulflight.composeapp.generated.resources.sca_intro
import peacefulflight.composeapp.generated.resources.sca_step_1
import peacefulflight.composeapp.generated.resources.sca_step_2
import peacefulflight.composeapp.generated.resources.sca_step_3
import peacefulflight.composeapp.generated.resources.sca_step_4
import peacefulflight.composeapp.generated.resources.sca_step_5
import peacefulflight.composeapp.generated.resources.sca_step_6
import peacefulflight.composeapp.generated.resources.sca_step_7
import peacefulflight.composeapp.generated.resources.sca_title
import peacefulflight.composeapp.generated.resources.wo2_intro
import peacefulflight.composeapp.generated.resources.wo2_step_1
import peacefulflight.composeapp.generated.resources.wo2_step_10
import peacefulflight.composeapp.generated.resources.wo2_step_2
import peacefulflight.composeapp.generated.resources.wo2_step_3
import peacefulflight.composeapp.generated.resources.wo2_step_4
import peacefulflight.composeapp.generated.resources.wo2_step_5
import peacefulflight.composeapp.generated.resources.wo2_step_6
import peacefulflight.composeapp.generated.resources.wo2_step_7
import peacefulflight.composeapp.generated.resources.wo2_step_8
import peacefulflight.composeapp.generated.resources.wo2_step_9
import peacefulflight.composeapp.generated.resources.wo2_title

/**
 * Navigation host for the app - matches original exactly
 */
@Composable
fun NavigationHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Main tabs
        composable(BottomNavItem.Cockpit.route) {
            com.romanpolach.peacefulflight.kmp.ui.screens.CockpitScreen(
                onNavigateToSos = {
                    navController.navigate(BottomNavItem.Sos.route)
                },
                onNavigateToTool = { toolId ->
                    when (toolId) {
                        "3" -> navController.navigate(Screen.GForceMonitorStandalone.route)
                        "5" -> navController.navigate(Screen.RidingTheWave.route)
                        "6" -> navController.navigate(Screen.PostponeTheWorry.route)
                        "7" -> navController.navigate(Screen.WorryOlympics.route)
                        "8" -> navController.navigate(Screen.FacingTheFear.route)
                        "13" -> navController.navigate(Screen.CatastrophicThinking.route)
                    }
                },
                onNavigateToLearn = { itemId ->
                    navController.navigate(Screen.LearnDetail.createRoute(itemId))
                }
            )
        }

        composable(BottomNavItem.Learn.route) {
            com.romanpolach.peacefulflight.kmp.ui.screens.LearnScreen(
                onNavigateToDetail = { itemId ->
                    navController.navigate(Screen.LearnDetail.createRoute(itemId))
                }
            )
        }

        composable(BottomNavItem.Sos.route) {
            com.romanpolach.peacefulflight.kmp.ui.screens.SosScreen(
                onNavigateToPanic = {
                    navController.navigate(Screen.FacingTheFear.route)
                },
                onNavigateToHelpOptions = {
                    navController.navigate(Screen.HelpOptions.route)
                },
                onExitSos = {
                    navController.navigate(BottomNavItem.Cockpit.route) {
                        popUpTo(BottomNavItem.Cockpit.route) { inclusive = true }
                    }
                }
            )
        }

        composable(BottomNavItem.Tools.route) {
            com.romanpolach.peacefulflight.kmp.ui.screens.ToolsScreen(
                onNavigateToTool = { toolId ->
                    when (toolId) {
                        "3" -> navController.navigate(Screen.GForceMonitorStandalone.route)
                        "5" -> navController.navigate(Screen.RidingTheWave.route)
                        "6" -> navController.navigate(Screen.PostponeTheWorry.route)
                        "7" -> navController.navigate(Screen.WorryOlympics.route)
                        "8" -> navController.navigate(Screen.FacingTheFear.route)
                        "9" -> navController.navigate(Screen.RealityCheck.route)
                        "10" -> navController.navigate(Screen.SafetyFacts.route)
                        "11" -> navController.navigate(Screen.AcceptanceMeditation.route)
                        "12" -> navController.navigate(Screen.SelfCompassion.route)
                        "13" -> navController.navigate(Screen.CatastrophicThinking.route)
                        "14" -> navController.navigate(Screen.VoiceSettings.route)
                    }
                }
            )
        }

        // Tool screens
        composable(route = Screen.RidingTheWave.route) {
            com.romanpolach.peacefulflight.kmp.ui.screens.RidingTheWaveScreen(
                onFinish = { navController.popBackStack() }
            )
        }

        composable(Screen.GForceMonitorStandalone.route) {
            com.romanpolach.peacefulflight.kmp.ui.screens.GForceScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.PostponeTheWorry.route) {
            val steps = listOf(
                Res.string.ptw_intro,
                Res.string.ptw_step_1,
                Res.string.ptw_step_2,
                Res.string.ptw_step_3,
                Res.string.ptw_step_4,
                Res.string.ptw_step_5,
                Res.string.ptw_step_6
            )
            com.romanpolach.peacefulflight.kmp.ui.screens.GuidedInterventionScreen(
                titleRes = Res.string.ptw_title,
                steps = steps,
                onFinish = { navController.popBackStack() }
            )
        }

        composable(Screen.WorryOlympics.route) {
            val steps = listOf(
                Res.string.wo2_intro,
                Res.string.wo2_step_1,
                Res.string.wo2_step_2,
                Res.string.wo2_step_3,
                Res.string.wo2_step_4,
                Res.string.wo2_step_5,
                Res.string.wo2_step_6,
                Res.string.wo2_step_7,
                Res.string.wo2_step_8,
                Res.string.wo2_step_9,
                Res.string.wo2_step_10
            )
            com.romanpolach.peacefulflight.kmp.ui.screens.GuidedInterventionScreen(
                titleRes = Res.string.wo2_title,
                steps = steps,
                onFinish = { navController.popBackStack() }
            )
        }

        composable(Screen.FacingTheFear.route) {
            val steps = listOf(
                Res.string.cloud_meditation_part_1,
                Res.string.cloud_meditation_part_2,
                Res.string.cloud_meditation_part_3,
                Res.string.cloud_meditation_part_4,
                Res.string.cloud_meditation_part_5,
                Res.string.cloud_meditation_part_6,
                Res.string.cloud_meditation_part_7,
                Res.string.cloud_meditation_part_8,
                Res.string.cloud_meditation_part_9,
                Res.string.cloud_meditation_footer
            )
            com.romanpolach.peacefulflight.kmp.ui.screens.GuidedInterventionScreen(
                titleRes = Res.string.ftf_title,
                steps = steps,
                onFinish = { navController.popBackStack() }
            )
        }

        composable(Screen.RealityCheck.route) {
            com.romanpolach.peacefulflight.kmp.ui.screens.RealityCheckScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.SafetyFacts.route) {
            com.romanpolach.peacefulflight.kmp.ui.screens.SafetyFactsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AcceptanceMeditation.route) {
            val steps = listOf(
                Res.string.am_intro,
                Res.string.am_part_1,
                Res.string.am_part_2,
                Res.string.am_part_3,
                Res.string.am_part_4,
                Res.string.am_part_5,
                Res.string.am_part_6
            )
            com.romanpolach.peacefulflight.kmp.ui.screens.GuidedInterventionScreen(
                titleRes = Res.string.am_title,
                steps = steps,
                onFinish = { navController.popBackStack() }
            )
        }

        composable(Screen.SelfCompassion.route) {
            val steps = listOf(
                Res.string.sca_intro,
                Res.string.sca_step_1,
                Res.string.sca_step_2,
                Res.string.sca_step_3,
                Res.string.sca_step_4,
                Res.string.sca_step_5,
                Res.string.sca_step_6,
                Res.string.sca_step_7
            )
            com.romanpolach.peacefulflight.kmp.ui.screens.GuidedInterventionScreen(
                titleRes = Res.string.sca_title,
                steps = steps,
                onFinish = { navController.popBackStack() }
            )
        }

        composable(Screen.CatastrophicThinking.route) {
            val steps = listOf(
                Res.string.ct_investigation_part_1,
                Res.string.ct_investigation_part_2,
                Res.string.ct_investigation_part_3,
                Res.string.ct_investigation_part_4,
                Res.string.ct_investigation_part_5,
                Res.string.ct_investigation_part_6,
                Res.string.ct_investigation_part_7
            )
            com.romanpolach.peacefulflight.kmp.ui.screens.GuidedInterventionScreen(
                titleRes = Res.string.ct_title,
                steps = steps,
                onFinish = { navController.popBackStack() }
            )
        }

        composable(Screen.VoiceSettings.route) {
            com.romanpolach.peacefulflight.kmp.ui.screens.VoiceSettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.HelpOptions.route) {
            com.romanpolach.peacefulflight.kmp.ui.screens.HelpOptionsScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToGForce = { navController.navigate(Screen.GForceMonitorStandalone.route) },
                onNavigateToRidingTheWave = { navController.navigate(Screen.RidingTheWave.route) },
                onNavigateToPostponeTheWorry = { navController.navigate(Screen.PostponeTheWorry.route) },
                onNavigateToWorryOlympics = { navController.navigate(Screen.WorryOlympics.route) },
                onNavigateToFacingTheFear = { navController.navigate(Screen.FacingTheFear.route) },
                onNavigateToRealityCheck = { navController.navigate(Screen.RealityCheck.route) },
                onNavigateToSafetyFacts = { navController.navigate(Screen.SafetyFacts.route) },
                onNavigateToAcceptanceMeditation = { navController.navigate(Screen.AcceptanceMeditation.route) },
                onNavigateToSelfCompassion = { navController.navigate(Screen.SelfCompassion.route) }
            )
        }

        composable(
            route = Screen.LearnDetail.route,
            arguments = Screen.LearnDetail.navArguments
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            com.romanpolach.peacefulflight.kmp.ui.screens.LearnDetailScreen(
                itemId = itemId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

