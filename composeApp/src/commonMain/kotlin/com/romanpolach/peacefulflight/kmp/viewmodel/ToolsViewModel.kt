package com.romanpolach.peacefulflight.kmp.viewmodel

import androidx.lifecycle.ViewModel
import com.romanpolach.peacefulflight.kmp.data.AppContent
import com.romanpolach.peacefulflight.kmp.model.Tool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for the Tools screen.
 * Migrated from the original Android app.
 */
class ToolsViewModel : ViewModel() {
    private val _tools = MutableStateFlow(AppContent.tools)
    val tools: StateFlow<List<Tool>> = _tools.asStateFlow()
}
