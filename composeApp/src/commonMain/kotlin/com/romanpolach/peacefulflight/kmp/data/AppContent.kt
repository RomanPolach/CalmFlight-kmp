package com.romanpolach.peacefulflight.kmp.data

import com.romanpolach.peacefulflight.kmp.model.LearnItem
import com.romanpolach.peacefulflight.kmp.model.LearnSection
import com.romanpolach.peacefulflight.kmp.model.Tool
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.airport_runway_lights
import peacefulflight.composeapp.generated.resources.am_title
import peacefulflight.composeapp.generated.resources.bird_strike_test
import peacefulflight.composeapp.generated.resources.crosswind_landing_a380
import peacefulflight.composeapp.generated.resources.ct_title
import peacefulflight.composeapp.generated.resources.deicing_a330
import peacefulflight.composeapp.generated.resources.egpws_display
import peacefulflight.composeapp.generated.resources.flight_a1
import peacefulflight.composeapp.generated.resources.flight_a10
import peacefulflight.composeapp.generated.resources.flight_a11
import peacefulflight.composeapp.generated.resources.flight_a2
import peacefulflight.composeapp.generated.resources.flight_a3
import peacefulflight.composeapp.generated.resources.flight_a4
import peacefulflight.composeapp.generated.resources.flight_a5
import peacefulflight.composeapp.generated.resources.flight_a6
import peacefulflight.composeapp.generated.resources.flight_a7
import peacefulflight.composeapp.generated.resources.flight_a8
import peacefulflight.composeapp.generated.resources.flight_q1
import peacefulflight.composeapp.generated.resources.flight_q10
import peacefulflight.composeapp.generated.resources.flight_q11
import peacefulflight.composeapp.generated.resources.flight_q2
import peacefulflight.composeapp.generated.resources.flight_q3
import peacefulflight.composeapp.generated.resources.flight_q4
import peacefulflight.composeapp.generated.resources.flight_q5
import peacefulflight.composeapp.generated.resources.flight_q6
import peacefulflight.composeapp.generated.resources.flight_q7
import peacefulflight.composeapp.generated.resources.flight_q8
import peacefulflight.composeapp.generated.resources.ftf_title
import peacefulflight.composeapp.generated.resources.g_force_monitor
import peacefulflight.composeapp.generated.resources.gforce_explanation_title
import peacefulflight.composeapp.generated.resources.img_flight
import peacefulflight.composeapp.generated.resources.img_landing
import peacefulflight.composeapp.generated.resources.img_takeoff
import peacefulflight.composeapp.generated.resources.img_title_airport_lights
import peacefulflight.composeapp.generated.resources.img_title_bird_strike_test
import peacefulflight.composeapp.generated.resources.img_title_crosswind_landing
import peacefulflight.composeapp.generated.resources.img_title_deicing
import peacefulflight.composeapp.generated.resources.img_title_egpws
import peacefulflight.composeapp.generated.resources.img_title_rat
import peacefulflight.composeapp.generated.resources.img_title_wing_flex
import peacefulflight.composeapp.generated.resources.landing_a1
import peacefulflight.composeapp.generated.resources.landing_a2
import peacefulflight.composeapp.generated.resources.landing_a3
import peacefulflight.composeapp.generated.resources.landing_a4
import peacefulflight.composeapp.generated.resources.landing_a5
import peacefulflight.composeapp.generated.resources.landing_a9
import peacefulflight.composeapp.generated.resources.landing_q1
import peacefulflight.composeapp.generated.resources.landing_q2
import peacefulflight.composeapp.generated.resources.landing_q3
import peacefulflight.composeapp.generated.resources.landing_q4
import peacefulflight.composeapp.generated.resources.landing_q5
import peacefulflight.composeapp.generated.resources.landing_q9
import peacefulflight.composeapp.generated.resources.learn_section_flight
import peacefulflight.composeapp.generated.resources.learn_section_landing
import peacefulflight.composeapp.generated.resources.learn_section_takeoff
import peacefulflight.composeapp.generated.resources.ptw_title
import peacefulflight.composeapp.generated.resources.ram_air_turbine_a320
import peacefulflight.composeapp.generated.resources.rc_empty_title
import peacefulflight.composeapp.generated.resources.rc_title
import peacefulflight.composeapp.generated.resources.rtw2_title
import peacefulflight.composeapp.generated.resources.sca_title
import peacefulflight.composeapp.generated.resources.takeoff_a1
import peacefulflight.composeapp.generated.resources.takeoff_a3
import peacefulflight.composeapp.generated.resources.takeoff_a5
import peacefulflight.composeapp.generated.resources.takeoff_a6
import peacefulflight.composeapp.generated.resources.takeoff_a7
import peacefulflight.composeapp.generated.resources.takeoff_a8
import peacefulflight.composeapp.generated.resources.takeoff_q1
import peacefulflight.composeapp.generated.resources.takeoff_q3
import peacefulflight.composeapp.generated.resources.takeoff_q5
import peacefulflight.composeapp.generated.resources.takeoff_q6
import peacefulflight.composeapp.generated.resources.takeoff_q7
import peacefulflight.composeapp.generated.resources.takeoff_q8
import peacefulflight.composeapp.generated.resources.tool_shortcut_desc_ftf
import peacefulflight.composeapp.generated.resources.tool_shortcut_desc_ptw
import peacefulflight.composeapp.generated.resources.tool_shortcut_desc_rtw
import peacefulflight.composeapp.generated.resources.tool_shortcut_desc_wo
import peacefulflight.composeapp.generated.resources.voice_settings_title
import peacefulflight.composeapp.generated.resources.wing_flex_787
import peacefulflight.composeapp.generated.resources.wo2_title

/**
 * Static app content - tools, categories, and learn sections
 * Replicated EXACTLY from the original Android app.
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
                LearnItem("takeoff_5", Res.string.takeoff_q5, Res.string.takeoff_a5),
                LearnItem("takeoff_6", Res.string.takeoff_q6, Res.string.takeoff_a6),
                LearnItem("takeoff_7", Res.string.takeoff_q7, Res.string.takeoff_a7),
                LearnItem(
                    "takeoff_8",
                    Res.string.takeoff_q8,
                    Res.string.takeoff_a8,
                    Res.drawable.bird_strike_test,
                    Res.string.img_title_bird_strike_test
                )
            )
        ),
        LearnSection(
            id = "flight",
            titleRes = Res.string.learn_section_flight,
            imageRes = Res.drawable.img_flight,
            items = listOf(
                LearnItem("flight_1", Res.string.flight_q1, Res.string.flight_a1),
                LearnItem("flight_2", Res.string.flight_q2, Res.string.flight_a2),
                LearnItem("flight_3", Res.string.flight_q3, Res.string.flight_a3),
                LearnItem("flight_4", Res.string.flight_q4, Res.string.flight_a4),
                LearnItem("flight_5", Res.string.flight_q5, Res.string.flight_a5),
                LearnItem("flight_6", Res.string.flight_q6, Res.string.flight_a6),
                LearnItem("flight_7", Res.string.flight_q7, Res.string.flight_a7),
                LearnItem(
                    "flight_8",
                    Res.string.flight_q8,
                    Res.string.flight_a8,
                    Res.drawable.deicing_a330,
                    Res.string.img_title_deicing
                ),
                LearnItem(
                    "flight_10",
                    Res.string.flight_q10,
                    Res.string.flight_a10,
                    Res.drawable.ram_air_turbine_a320,
                    Res.string.img_title_rat
                ),
                LearnItem("flight_11", Res.string.flight_q11, Res.string.flight_a11)
            )
        ),
        LearnSection(
            id = "landing",
            titleRes = Res.string.learn_section_landing,
            imageRes = Res.drawable.img_landing,
            items = listOf(
                LearnItem("landing_1", Res.string.landing_q1, Res.string.landing_a1),
                LearnItem(
                    "landing_2",
                    Res.string.landing_q2,
                    Res.string.landing_a2,
                    Res.drawable.wing_flex_787,
                    Res.string.img_title_wing_flex
                ),
                LearnItem("landing_3", Res.string.landing_q3, Res.string.landing_a3),
                LearnItem(
                    "landing_4",
                    Res.string.landing_q4,
                    Res.string.landing_a4,
                    Res.drawable.airport_runway_lights,
                    Res.string.img_title_airport_lights
                ),
                LearnItem(
                    "landing_5",
                    Res.string.landing_q5,
                    Res.string.landing_a5,
                    Res.drawable.crosswind_landing_a380,
                    Res.string.img_title_crosswind_landing
                ),
                LearnItem(
                    "landing_9",
                    Res.string.landing_q9,
                    Res.string.landing_a9,
                    Res.drawable.egpws_display,
                    Res.string.img_title_egpws
                )
            )
        )
    )

    val tools = listOf(
        Tool("3", Res.string.g_force_monitor, Res.string.gforce_explanation_title, "Graph"),
        Tool("5", Res.string.rtw2_title, Res.string.tool_shortcut_desc_rtw, "Wave"),
        Tool("6", Res.string.ptw_title, Res.string.tool_shortcut_desc_ptw, "Clock"),
        Tool("7", Res.string.wo2_title, Res.string.tool_shortcut_desc_wo, "Trophy"),
        Tool("8", Res.string.ftf_title, Res.string.tool_shortcut_desc_ftf, "Cloud"),
        Tool("9", Res.string.rc_title, Res.string.rc_empty_title, "Chart"),
        Tool("11", Res.string.am_title, Res.string.am_title, "Meditation"),
        Tool("12", Res.string.sca_title, Res.string.sca_title, "Heart"),
        Tool("13", Res.string.ct_title, Res.string.ct_title, "Brain"),
        Tool("14", Res.string.voice_settings_title, Res.string.voice_settings_title, "Voice")
    )
}
