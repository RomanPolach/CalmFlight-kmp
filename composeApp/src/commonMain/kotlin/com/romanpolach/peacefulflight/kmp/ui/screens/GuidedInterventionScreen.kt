package com.romanpolach.peacefulflight.kmp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.ui.components.ContentCard
import com.romanpolach.peacefulflight.kmp.ui.components.PrimaryButton
import com.romanpolach.peacefulflight.kmp.ui.components.StandardTopBar
import com.romanpolach.peacefulflight.kmp.viewmodel.GuidedInterventionViewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.continue_btn
import peacefulflight.composeapp.generated.resources.finish_btn

@OptIn(KoinExperimentalAPI::class)
@Composable
fun GuidedInterventionScreen(
    titleRes: StringResource,
    steps: List<StringResource>,
    viewModel: GuidedInterventionViewModel = koinViewModel(),
    onFinish: () -> Unit
) {
    // Initialize ViewModel with steps only once
    LaunchedEffect(steps) {
        viewModel.initialize(steps)
    }

    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Handle empty initial state safely
    val currentTextRes = uiState.currentTextRes ?: return

    val currentText = stringResource(currentTextRes)
    val title = stringResource(titleRes)

    // Auto-scroll to top when step changes
    LaunchedEffect(uiState.currentStepIndex) {
        scrollState.animateScrollTo(0)
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = title,
                onBackClick = onFinish
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shadowElevation = 4.dp
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleTts(currentText) },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (uiState.isAutoPlayEnabled) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                contentDescription = "Read aloud",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                ContentCard(text = currentText)

                Spacer(modifier = Modifier.height(20.dp))

                if (!uiState.isLastStep) {
                    val nextStepIndex = uiState.currentStepIndex + 1
                    val nextStepText =
                        if (nextStepIndex < steps.size) stringResource(steps[nextStepIndex]) else ""

                    PrimaryButton(
                        text = stringResource(Res.string.continue_btn),
                        onClick = { viewModel.nextStep(nextStepText) },
                        modifier = Modifier.fillMaxWidth(0.6f)
                    )
                } else {
                    // Finish button on last step
                    PrimaryButton(
                        text = stringResource(Res.string.finish_btn),
                        onClick = onFinish,
                        modifier = Modifier.fillMaxWidth(0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
