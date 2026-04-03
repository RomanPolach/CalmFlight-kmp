package com.romanpolach.peacefulflight.kmp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.ui.components.GForceExplanationCard
import com.romanpolach.peacefulflight.kmp.ui.components.GForceMonitorCard
import com.romanpolach.peacefulflight.kmp.ui.components.StandardTopBar
import com.romanpolach.peacefulflight.kmp.viewmodel.GForceViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.g_force_monitor

@OptIn(KoinExperimentalAPI::class)
@Composable
fun GForceScreen(
    onBackClick: () -> Unit,
    viewModel: GForceViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Start tracking when screen open
    DisposableEffect(Unit) {
        viewModel.startTracking()
        onDispose {
            viewModel.stopTracking()
        }
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = stringResource(Res.string.g_force_monitor),
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GForceMonitorCard(
                uiState = uiState,
                isCompact = false
            )

            GForceExplanationCard()
        }
    }
}
