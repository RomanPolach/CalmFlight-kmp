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
 * FAQ Question
 */
data class Question(
    val id: String,
    val question: String,
    val answer: String,
    val categoryId: String
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
 * Flight status enum
 */
enum class FlightStatus(val labelRes: StringResource) {
    ON_LAND(com.romanpolach.peacefulflight.kmp.generated.resources.Res.string.status_on_land),
    BOARDING(com.romanpolach.peacefulflight.kmp.generated.resources.Res.string.status_boarding),
    TAKEOFF(com.romanpolach.peacefulflight.kmp.generated.resources.Res.string.status_takeoff),
    CRUISE(com.romanpolach.peacefulflight.kmp.generated.resources.Res.string.status_cruise),
    TURBULENCE(com.romanpolach.peacefulflight.kmp.generated.resources.Res.string.status_turbulence),
    LANDING(com.romanpolach.peacefulflight.kmp.generated.resources.Res.string.status_landing)
}
