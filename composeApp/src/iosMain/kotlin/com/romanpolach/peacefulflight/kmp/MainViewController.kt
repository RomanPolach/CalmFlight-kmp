package com.romanpolach.peacefulflight.kmp

import androidx.compose.ui.window.ComposeUIViewController

import com.romanpolach.peacefulflight.kmp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }