package com.romanpolach.peacefulflight.kmp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.model.CockpitUiState
import com.romanpolach.peacefulflight.kmp.model.FlightStatus
import com.romanpolach.peacefulflight.kmp.ui.components.StandardTopBar
import com.romanpolach.peacefulflight.kmp.viewmodel.CockpitViewModel
import com.romanpolach.peacefulflight.kmp.viewmodel.GForceViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.cruise_tools_title
import peacefulflight.composeapp.generated.resources.flight_q1
import peacefulflight.composeapp.generated.resources.flight_q2
import peacefulflight.composeapp.generated.resources.flight_q3
import peacefulflight.composeapp.generated.resources.ftf_title
import peacefulflight.composeapp.generated.resources.landing_q1
import peacefulflight.composeapp.generated.resources.landing_q2
import peacefulflight.composeapp.generated.resources.landing_q3
import peacefulflight.composeapp.generated.resources.landing_tools_title
import peacefulflight.composeapp.generated.resources.learn_section_flight
import peacefulflight.composeapp.generated.resources.learn_section_landing
import peacefulflight.composeapp.generated.resources.learn_section_takeoff
import peacefulflight.composeapp.generated.resources.nav_cockpit
import peacefulflight.composeapp.generated.resources.nav_sos
import peacefulflight.composeapp.generated.resources.on_land_card_desc
import peacefulflight.composeapp.generated.resources.on_land_card_title
import peacefulflight.composeapp.generated.resources.rtw2_title
import peacefulflight.composeapp.generated.resources.settings
import peacefulflight.composeapp.generated.resources.status_boarding
import peacefulflight.composeapp.generated.resources.status_cruise
import peacefulflight.composeapp.generated.resources.status_landing
import peacefulflight.composeapp.generated.resources.status_takeoff
import peacefulflight.composeapp.generated.resources.takeoff_q1
import peacefulflight.composeapp.generated.resources.takeoff_q3
import peacefulflight.composeapp.generated.resources.takeoff_q5
import peacefulflight.composeapp.generated.resources.takeoff_tools_title
import peacefulflight.composeapp.generated.resources.tool_shortcut_desc_ftf
import peacefulflight.composeapp.generated.resources.tool_shortcut_desc_rtw

/**
 * Cockpit screen - main dashboard with flight phase tabs
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
fun CockpitScreen(
    onNavigateToSos: () -> Unit,
    onNavigateToTool: (String) -> Unit,
    onNavigateToLearn: (String) -> Unit = {},
    viewModel: CockpitViewModel = koinViewModel(),
    gForceViewModel: GForceViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val gForceUiState by gForceViewModel.uiState.collectAsState()

    // Start/Stop sensor tracking based on phase
    LaunchedEffect(uiState.status) {
        if (uiState.status == FlightStatus.TAKEOFF || uiState.status == FlightStatus.CRUISE) {
            gForceViewModel.startTracking()
        } else {
            gForceViewModel.stopTracking()
        }
    }

    val cockpitTitle = stringResource(Res.string.nav_cockpit)
    val sosNav = stringResource(Res.string.nav_sos)
    val settingsStr = stringResource(Res.string.settings)

    val phases = remember {
        listOf(
            FlightStatus.BOARDING,
            FlightStatus.TAKEOFF,
            FlightStatus.CRUISE,
            FlightStatus.LANDING
        )
    }

    // Get labels for each phase
    val phaseLabels = mapOf(
        FlightStatus.BOARDING to stringResource(Res.string.status_boarding),
        FlightStatus.TAKEOFF to stringResource(Res.string.status_takeoff),
        FlightStatus.CRUISE to stringResource(Res.string.status_cruise),
        FlightStatus.LANDING to stringResource(Res.string.status_landing)
    )

    val pagerState = rememberPagerState(pageCount = { phases.size })
    val scope = rememberCoroutineScope()

    // Sync pager with status from ViewModel
    LaunchedEffect(uiState.status) {
        val targetPage = phases.indexOf(uiState.status)
        if (targetPage != -1 && pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    // Sync ViewModel status with pager
    LaunchedEffect(pagerState.currentPage) {
        viewModel.setStatus(phases[pagerState.currentPage])
    }

    // Show settings dialog
    if (uiState.showSettingsDialog) {
        com.romanpolach.peacefulflight.kmp.ui.components.SettingsDialog(
            onDismiss = { viewModel.toggleSettingsDialog(false) }
        )
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = cockpitTitle,
                onBackClick = null,
                actions = {
                    IconButton(onClick = onNavigateToSos) {
                        Icon(
                            Icons.Default.HealthAndSafety,
                            contentDescription = sosNav,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    IconButton(onClick = { viewModel.toggleSettingsDialog(true) }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = settingsStr,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Phase Tabs
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                phases.forEachIndexed { index, phase ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = {
                            Text(
                                text = (phaseLabels[phase] ?: "").uppercase(),
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }

            // Main Content Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                val currentPhase = phases[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    when (currentPhase) {
                        FlightStatus.BOARDING -> BoardingContent(
                            uiState = uiState,
                            onFetchWithLocation = { viewModel.fetchWeatherWithLocation() },
                            onRetry = { viewModel.refreshWeather() }
                        )

                        FlightStatus.TAKEOFF -> TakeoffContent(
                            gForceUiState = gForceUiState,
                            onNavigateToTool = onNavigateToTool,
                            onNavigateToLearn = onNavigateToLearn
                        )

                        FlightStatus.CRUISE -> CruiseContent(
                            gForceUiState = gForceUiState,
                            onNavigateToTool = onNavigateToTool,
                            onNavigateToLearn = onNavigateToLearn
                        )

                        FlightStatus.LANDING -> LandingContent(
                            onNavigateToTool = onNavigateToTool,
                            onNavigateToLearn = onNavigateToLearn
                        )

                        else -> {}
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun BoardingContent(
    uiState: CockpitUiState,
    onFetchWithLocation: () -> Unit,
    onRetry: () -> Unit
) {
    com.romanpolach.peacefulflight.kmp.ui.components.WeatherWidget(
        weatherState = uiState.weather,
        onFetchClick = onFetchWithLocation,
        onRetry = onRetry,
        isMetric = uiState.isMetric
    )

    Spacer(modifier = Modifier.height(16.dp))

    val title = stringResource(Res.string.on_land_card_title)
    val desc = stringResource(Res.string.on_land_card_desc)

    OnLandCard(
        title = title,
        description = desc
    )
}


@Composable
fun TakeoffContent(
    gForceUiState: com.romanpolach.peacefulflight.kmp.viewmodel.GForceUiState,
    onNavigateToTool: (String) -> Unit,
    onNavigateToLearn: (String) -> Unit
) {
    com.romanpolach.peacefulflight.kmp.ui.components.GForceMonitorCard(
        uiState = gForceUiState,
        isCompact = true,
        onClick = { onNavigateToTool("3") }
    )

    val toolsTitle = stringResource(Res.string.takeoff_tools_title)
    val rtwTitle = stringResource(Res.string.rtw2_title)
    val rtwDesc = stringResource(Res.string.tool_shortcut_desc_rtw)
    val sectionTitle = stringResource(Res.string.learn_section_takeoff)

    // Tool shortcuts section
    Text(
        text = toolsTitle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 8.dp)
    )

    // Tool: Riding the Wave
    ToolShortcutCard(
        title = rtwTitle,
        description = rtwDesc,
        icon = Icons.Default.GraphicEq,
        onClick = { onNavigateToTool("5") }
    )

    // Learn Questions
    Text(
        text = sectionTitle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp)
    )

    LearnQuestionsCard(
        sectionId = "takeoff",
        onNavigateToLearn = onNavigateToLearn
    )
}

@Composable
fun CruiseContent(
    gForceUiState: com.romanpolach.peacefulflight.kmp.viewmodel.GForceUiState,
    onNavigateToTool: (String) -> Unit,
    onNavigateToLearn: (String) -> Unit
) {
    com.romanpolach.peacefulflight.kmp.ui.components.GForceMonitorCard(
        uiState = gForceUiState,
        isCompact = true,
        onClick = { onNavigateToTool("3") }
    )

    val toolsTitle = stringResource(Res.string.cruise_tools_title)
    val ftfTitle = stringResource(Res.string.ftf_title)
    val ftfDesc = stringResource(Res.string.tool_shortcut_desc_ftf)
    val sectionTitle = stringResource(Res.string.learn_section_flight)

    Text(
        text = toolsTitle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 8.dp)
    )

    // Tool: Facing the Fear
    ToolShortcutCard(
        title = ftfTitle,
        description = ftfDesc,
        icon = Icons.AutoMirrored.Filled.ArrowForward,
        onClick = { onNavigateToTool("8") }
    )

    // Learn Questions
    Text(
        text = sectionTitle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp)
    )

    LearnQuestionsCard(
        sectionId = "flight",
        onNavigateToLearn = onNavigateToLearn
    )
}

@Composable
fun LandingContent(
    onNavigateToTool: (String) -> Unit,
    onNavigateToLearn: (String) -> Unit
) {
    val toolsTitle = stringResource(Res.string.landing_tools_title)
    val rtwTitle = stringResource(Res.string.rtw2_title)
    val rtwDesc = stringResource(Res.string.tool_shortcut_desc_rtw)
    val sectionTitle = stringResource(Res.string.learn_section_landing)

    Text(
        text = toolsTitle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 8.dp)
    )

    // Tool: Riding the Wave
    ToolShortcutCard(
        title = rtwTitle,
        description = rtwDesc,
        icon = Icons.Default.GraphicEq,
        onClick = { onNavigateToTool("5") }
    )

    // Learn Questions
    Text(
        text = sectionTitle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp)
    )

    LearnQuestionsCard(
        sectionId = "landing",
        onNavigateToLearn = onNavigateToLearn
    )
}

@Composable
fun ToolShortcutCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun OnLandCard(
    title: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Icon(
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

/**
 * Card showing learn questions for a section
 */
@Composable
fun LearnQuestionsCard(
    sectionId: String,
    onNavigateToLearn: (String) -> Unit
) {
    // Get questions based on section
    val questions = when (sectionId) {
        "takeoff" -> listOf(
            "takeoff_1" to stringResource(Res.string.takeoff_q1),
            "takeoff_3" to stringResource(Res.string.takeoff_q3),
            "takeoff_5" to stringResource(Res.string.takeoff_q5)
        )

        "flight" -> listOf(
            "flight_1" to stringResource(Res.string.flight_q1),
            "flight_2" to stringResource(Res.string.flight_q2),
            "flight_3" to stringResource(Res.string.flight_q3)
        )

        "landing" -> listOf(
            "landing_1" to stringResource(Res.string.landing_q1),
            "landing_2" to stringResource(Res.string.landing_q2),
            "landing_3" to stringResource(Res.string.landing_q3)
        )

        else -> emptyList()
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            questions.forEachIndexed { index, (id, question) ->
                QuestionListItem(
                    question = question,
                    onClick = { onNavigateToLearn(id) }
                )

                if (index < questions.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionListItem(
    question: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
    }
}
