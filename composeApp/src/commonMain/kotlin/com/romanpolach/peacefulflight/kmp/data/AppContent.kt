package com.romanpolach.peacefulflight.kmp.data

import com.romanpolach.peacefulflight.kmp.model.*
import peacefulflight.composeapp.generated.resources.*

/**
 * Static app content - tools, categories, and learn sections
 * Matches the original Android app content exactly.
 */
object AppContent {

    fun getLearnItemById(id: String): LearnItem? {
        return learnSections.flatMap { it.items }.find { it.id == id }
    }

    val learnSections = listOf(
        LearnSection(
            id = "takeoff",
            titleRes = Res.string.learn_section_takeoff,
            imageRes = Res.drawable.img_takeoff,
            items = listOf(
                LearnItem("takeoff_1", Res.string.takeoff_q1, Res.string.takeoff_a1),
                LearnItem("takeoff_3", Res.string.takeoff_q3, Res.string.takeoff_a3),
                LearnItem(
                    "takeoff_5",
                    Res.string.takeoff_q1,
                    Res.string.takeoff_a1
                ), // Placeholder for missing answers
                LearnItem("takeoff_6", Res.string.takeoff_q1, Res.string.takeoff_a1),
                LearnItem("takeoff_7", Res.string.takeoff_q1, Res.string.takeoff_a1),
                LearnItem("takeoff_8", Res.string.takeoff_q1, Res.string.takeoff_a1)
            )
        ),
        LearnSection(
            id = "flight",
            titleRes = Res.string.learn_section_flight,
            imageRes = Res.drawable.img_flight,
            items = listOf(
                LearnItem("flight_1", Res.string.takeoff_q1, Res.string.takeoff_a1),
                LearnItem("flight_2", Res.string.takeoff_q1, Res.string.takeoff_a1),
                LearnItem("flight_3", Res.string.takeoff_q1, Res.string.takeoff_a1),
                // ... more items will be added properly later
            )
        ),
        LearnSection(
            id = "landing",
            titleRes = Res.string.learn_section_landing,
            imageRes = Res.drawable.img_landing,
            items = listOf(
                LearnItem("landing_1", Res.string.takeoff_q1, Res.string.takeoff_a1),
                // ... more items will be added properly later
            )
        )
    )

    val tools = listOf(
        Tool("3", Res.string.tool_gforce, Res.string.tool_shortcut_desc_gforce, "Graph"),
        Tool("5", Res.string.rtw2_title, Res.string.tool_shortcut_desc_rtw, "Wave"),
        Tool("6", Res.string.ptw_title, Res.string.tool_shortcut_desc_ptw, "Clock"),
        Tool("7", Res.string.wo_title, Res.string.tool_shortcut_desc_wo, "Trophy"),
        Tool("8", Res.string.ftf_title, Res.string.tool_shortcut_desc_ftf, "Cloud"),
        Tool("9", Res.string.rc_title, Res.string.tool_shortcut_desc_rc, "Chart"),
        Tool("10", Res.string.sf_title, Res.string.tool_shortcut_desc_sf, "Shield"),
        Tool("11", Res.string.am_title, Res.string.tool_shortcut_desc_am, "Meditation"),
        Tool("12", Res.string.sc_title, Res.string.tool_shortcut_desc_sc, "Heart"),
        Tool("13", Res.string.ct_title, Res.string.tool_shortcut_desc_ct, "Brain"),
        Tool("14", Res.string.voice_settings, Res.string.tool_shortcut_desc_voice, "Voice")
    )

    val categories = listOf(
        Category("A", Res.string.cat_turbulence_title, Res.string.cat_turbulence_desc),
        Category("B", Res.string.cat_sounds_title, Res.string.cat_sounds_desc),
        Category("C", Res.string.cat_weather_title, Res.string.cat_weather_desc),
        Category("D", Res.string.cat_mechanical_title, Res.string.cat_mechanical_desc),
        Category("E", Res.string.cat_crew_title, Res.string.cat_crew_desc)
    )
}
