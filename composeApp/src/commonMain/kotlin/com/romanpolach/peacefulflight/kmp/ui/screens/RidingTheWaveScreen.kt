package com.romanpolach.peacefulflight.kmp.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.ui.components.AnxietyRatingBar
import com.romanpolach.peacefulflight.kmp.ui.components.ContentCard
import com.romanpolach.peacefulflight.kmp.ui.components.PrimaryButton
import com.romanpolach.peacefulflight.kmp.ui.components.StandardTopBar
import com.romanpolach.peacefulflight.kmp.viewmodel.RidingTheWaveViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.close_btn
import peacefulflight.composeapp.generated.resources.congrats_msg
import peacefulflight.composeapp.generated.resources.congrats_title
import peacefulflight.composeapp.generated.resources.continue_btn
import peacefulflight.composeapp.generated.resources.feedback_improving
import peacefulflight.composeapp.generated.resources.feedback_steady
import peacefulflight.composeapp.generated.resources.feedback_worsening
import peacefulflight.composeapp.generated.resources.finish_btn
import peacefulflight.composeapp.generated.resources.rtw2_intro
import peacefulflight.composeapp.generated.resources.rtw2_step_1
import peacefulflight.composeapp.generated.resources.rtw2_step_10
import peacefulflight.composeapp.generated.resources.rtw2_step_11
import peacefulflight.composeapp.generated.resources.rtw2_step_2
import peacefulflight.composeapp.generated.resources.rtw2_step_3
import peacefulflight.composeapp.generated.resources.rtw2_step_4
import peacefulflight.composeapp.generated.resources.rtw2_step_5
import peacefulflight.composeapp.generated.resources.rtw2_step_6
import peacefulflight.composeapp.generated.resources.rtw2_step_7
import peacefulflight.composeapp.generated.resources.rtw2_step_8
import peacefulflight.composeapp.generated.resources.rtw2_step_9
import peacefulflight.composeapp.generated.resources.rtw2_title

@OptIn(KoinExperimentalAPI::class)
@Composable
fun RidingTheWaveScreen(
    viewModel: RidingTheWaveViewModel = koinViewModel(),
    onFinish: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Initialize steps
    val steps = listOf(
        Res.string.rtw2_intro,
        Res.string.rtw2_step_1,
        Res.string.rtw2_step_2,
        Res.string.rtw2_step_3,
        Res.string.rtw2_step_4,
        Res.string.rtw2_step_5,
        Res.string.rtw2_step_6,
        Res.string.rtw2_step_7,
        Res.string.rtw2_step_8,
        Res.string.rtw2_step_9,
        Res.string.rtw2_step_10,
        Res.string.rtw2_step_11
    )

    LaunchedEffect(Unit) {
        viewModel.initialize(steps)
    }

    // Handle uninitialized state
    val currentTextRes = uiState.currentTextRes ?: return
    val currentText = stringResource(currentTextRes)

    // Auto-play logic
    LaunchedEffect(currentText) {
        viewModel.onStepContentChanged(currentText)
    }

    // Auto-scroll to top when step changes
    LaunchedEffect(uiState.currentStepIndex) {
        scrollState.animateScrollTo(0)
    }

    if (uiState.showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.closeDialog(onFinish) },
            confirmButton = {
                TextButton(onClick = { viewModel.closeDialog(onFinish) }) {
                    Text(
                        stringResource(Res.string.close_btn),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            title = {
                Text(
                    stringResource(Res.string.congrats_title),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    stringResource(Res.string.congrats_msg),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = stringResource(Res.string.rtw2_title),
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { viewModel.toggleTts(currentText) },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainer,
                                shape = MaterialTheme.shapes.medium
                            )
                    ) {
                        Icon(
                            imageVector = if (uiState.isAutoPlayEnabled) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Read aloud",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                ContentCard(text = currentText)

                Spacer(modifier = Modifier.height(20.dp))

                if (!uiState.isLastStep) {
                    PrimaryButton(
                        text = stringResource(Res.string.continue_btn),
                        onClick = { viewModel.nextStep() },
                        modifier = Modifier.fillMaxWidth(0.6f)
                    )
                } else {
                    PrimaryButton(
                        text = stringResource(Res.string.finish_btn),
                        onClick = { viewModel.finishSession(onFinish) },
                        modifier = Modifier.fillMaxWidth(0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLastStep || uiState.currentStepIndex == 0) {
                AnxietyRatingBar(
                    rating = uiState.anxietyScore,
                    onRatingChanged = { viewModel.updateAnxietyScore(it) },
                    onSubmitRating = {
                        viewModel.submitRating(
                            Res.string.feedback_improving,
                            Res.string.feedback_worsening,
                            Res.string.feedback_steady
                        )
                    },
                    feedbackMessage = uiState.feedbackMessageRes?.let { stringResource(it) }
                )
            }
        }
    }
}
