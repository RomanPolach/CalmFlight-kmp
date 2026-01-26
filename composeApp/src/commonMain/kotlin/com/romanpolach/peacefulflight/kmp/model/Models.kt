package com.romanpolach.peacefulflight.kmp.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Learn section containing educational content about flight phases
 */
data class LearnSection(
    val id: String,
    val titleRes: StringResource,
    val imageRes: DrawableResource? = null,
    val items: List<LearnItem>
)

/**
 * Individual learn item with question/answer content
 */
data class LearnItem(
    val id: String,
    val questionRes: StringResource,
    val answerRes: StringResource,
    val imageRes: DrawableResource? = null,
    val imageTitleRes: StringResource? = null
)

/**
 * FAQ Category
 */
data class Category(
    val id: String,
    val titleRes: StringResource,
    val descriptionRes: StringResource
)

/**
 * Tool available to help anxious flyers
 */
data class Tool(
    val id: String,
    val nameRes: StringResource,
    val descriptionRes: StringResource,
    val iconName: String
)

/**
 * Flight status enum - simple version without resource dependencies
 */
enum class FlightStatus(val label: String) {
    BOARDING("Boarding"),
    TAKEOFF("Takeoff"),
    CRUISE("Cruising"),
    LANDING("Landing")
}
