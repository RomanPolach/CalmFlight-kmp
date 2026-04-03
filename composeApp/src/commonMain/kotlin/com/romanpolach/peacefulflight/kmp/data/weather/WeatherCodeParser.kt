package com.romanpolach.peacefulflight.kmp.data.weather

/**
 * WMO Weather Code Parser
 * Provides precise weather descriptions and flight condition interpretations
 * Designed for nervous flyer app - balances accuracy with reassurance
 */
object WeatherCodeParser {

    /**
     * Flight condition categories for aviation context
     */
    enum class FlightCondition {
        EXCELLENT,  // Perfect flying conditions
        GOOD,       // Safe, minor considerations
        CAUTION,    // Proceed with awareness
        POOR        // Challenging conditions
    }

    data class WeatherInfo(
        val description: String,
        val flightCondition: FlightCondition,
        val passengerMessage: String,      // Reassuring message for nervous flyers
        val technicalInfo: String          // Accurate info for pilots/technical users
    )

    private val clearSkyInfo = WeatherInfo(
        description = "Clear sky",
        flightCondition = FlightCondition.EXCELLENT,
        passengerMessage = "Beautiful clear skies - perfect conditions for a smooth flight",
        technicalInfo = "VFR conditions, unlimited visibility"
    )

    private val mainlyClearInfo = WeatherInfo(
        description = "Mainly clear",
        flightCondition = FlightCondition.EXCELLENT,
        passengerMessage = "Mostly clear with excellent visibility",
        technicalInfo = "Excellent VFR conditions"
    )

    private val partlyCloudyInfo = WeatherInfo(
        description = "Partly cloudy",
        flightCondition = FlightCondition.GOOD,
        passengerMessage = "Some clouds present - normal flying weather",
        technicalInfo = "Good VFR conditions"
    )

    private val overcastInfo = WeatherInfo(
        description = "Overcast",
        flightCondition = FlightCondition.GOOD,
        passengerMessage = "Cloudy skies - aircraft handle this routinely",
        technicalInfo = "Overcast, possible IFR conditions"
    )

    private val fogInfo = WeatherInfo(
        description = "Fog",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Foggy conditions - pilots use instruments for safe navigation",
        technicalInfo = "Reduced visibility, IFR procedures"
    )

    private val rimeFogInfo = WeatherInfo(
        description = "Depositing rime fog",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Fog present - aircraft equipped with de-icing systems",
        technicalInfo = "Limited visibility, icing possible"
    )

    private val lightDrizzleInfo = WeatherInfo(
        description = "Light drizzle",
        flightCondition = FlightCondition.GOOD,
        passengerMessage = "Light drizzle - planes fly safely in light rain",
        technicalInfo = "Minor precipitation, good visibility"
    )

    private val moderateDrizzleInfo = WeatherInfo(
        description = "Moderate drizzle",
        flightCondition = FlightCondition.GOOD,
        passengerMessage = "Wet conditions - aircraft designed for all-weather operation",
        technicalInfo = "Moderate precipitation, adequate visibility"
    )

    private val denseDrizzleInfo = WeatherInfo(
        description = "Dense drizzle",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Heavy drizzle - pilots trained for these conditions",
        technicalInfo = "Heavy drizzle, reduced visibility"
    )

    private val lightFreezingDrizzleInfo = WeatherInfo(
        description = "Light freezing drizzle",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Cold weather precipitation - de-icing systems active",
        technicalInfo = "Light icing conditions, de-icing required"
    )

    private val denseFreezingDrizzleInfo = WeatherInfo(
        description = "Dense freezing drizzle",
        flightCondition = FlightCondition.POOR,
        passengerMessage = "Winter weather - aircraft have advanced ice protection",
        technicalInfo = "Significant icing risk, enhanced monitoring"
    )

    private val slightRainInfo = WeatherInfo(
        description = "Slight rain",
        flightCondition = FlightCondition.GOOD,
        passengerMessage = "Light rain - planes are built to fly in wet weather",
        technicalInfo = "Light rain, good conditions"
    )

    private val moderateRainInfo = WeatherInfo(
        description = "Moderate rain",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Rainy weather - standard conditions for commercial aviation",
        technicalInfo = "Moderate rain, reduced visibility"
    )

    private val heavyRainInfo = WeatherInfo(
        description = "Heavy rain",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Heavy rain - pilots use instruments for precise navigation",
        technicalInfo = "Heavy precipitation, limited visibility"
    )

    private val lightFreezingRainInfo = WeatherInfo(
        description = "Light freezing rain",
        flightCondition = FlightCondition.POOR,
        passengerMessage = "Cold rain - aircraft equipped with comprehensive de-icing",
        technicalInfo = "Icing conditions, careful monitoring required"
    )

    private val heavyFreezingRainInfo = WeatherInfo(
        description = "Heavy freezing rain",
        flightCondition = FlightCondition.POOR,
        passengerMessage = "Winter precipitation - flights may be delayed for safety",
        technicalInfo = "Severe icing risk, challenging conditions"
    )

    private val slightSnowFallInfo = WeatherInfo(
        description = "Slight snow fall",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Light snow - airports have snow removal teams ready",
        technicalInfo = "Light snow, visibility reduced"
    )

    private val moderateSnowFallInfo = WeatherInfo(
        description = "Moderate snow fall",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Snowy conditions - de-icing procedures in effect",
        technicalInfo = "Moderate snow, limited visibility"
    )

    private val heavySnowFallInfo = WeatherInfo(
        description = "Heavy snow fall",
        flightCondition = FlightCondition.POOR,
        passengerMessage = "Heavy snow - flights may wait for better conditions",
        technicalInfo = "Heavy snow, significantly reduced visibility"
    )

    private val snowGrainsInfo = WeatherInfo(
        description = "Snow grains",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Winter precipitation - normal for cold weather flying",
        technicalInfo = "Snow grains, reduced visibility"
    )

    private val slightRainShowersInfo = WeatherInfo(
        description = "Slight rain showers",
        flightCondition = FlightCondition.GOOD,
        passengerMessage = "Passing showers - brief and manageable",
        technicalInfo = "Light showers, generally favorable"
    )

    private val moderateRainShowersInfo = WeatherInfo(
        description = "Moderate rain showers",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Rain showers - may cause brief bumps, perfectly safe",
        technicalInfo = "Moderate showers, possible turbulence"
    )

    private val violentRainShowersInfo = WeatherInfo(
        description = "Violent rain showers",
        flightCondition = FlightCondition.POOR,
        passengerMessage = "Heavy showers - pilots can navigate around weather cells",
        technicalInfo = "Intense showers, turbulence likely"
    )

    private val slightSnowShowersInfo = WeatherInfo(
        description = "Slight snow showers",
        flightCondition = FlightCondition.CAUTION,
        passengerMessage = "Snow showers - aircraft equipped for winter operations",
        technicalInfo = "Light snow showers, variable visibility"
    )

    private val heavySnowShowersInfo = WeatherInfo(
        description = "Heavy snow showers",
        flightCondition = FlightCondition.POOR,
        passengerMessage = "Heavy snow showers - flights may be rescheduled for safety",
        technicalInfo = "Heavy snow, significantly reduced visibility"
    )

    private val thunderstormInfo = WeatherInfo(
        description = "Thunderstorm",
        flightCondition = FlightCondition.POOR,
        passengerMessage = "Thunderstorm activity - pilots route around storm cells",
        technicalInfo = "Thunderstorm, turbulence and lightning present"
    )

    private val thunderstormWithSlightHailInfo = WeatherInfo(
        description = "Thunderstorm with slight hail",
        flightCondition = FlightCondition.POOR,
        passengerMessage = "Storm with hail - flights typically delayed until it passes",
        technicalInfo = "Thunderstorm with hail, avoid area"
    )

    private val thunderstormWithHeavyHailInfo = WeatherInfo(
        description = "Thunderstorm with heavy hail",
        flightCondition = FlightCondition.POOR,
        passengerMessage = "Severe storm - flights wait for weather to clear",
        technicalInfo = "Severe thunderstorm with heavy hail"
    )

    private val unknownWeatherInfo = WeatherInfo(
        description = "Weather information unavailable",
        flightCondition = FlightCondition.GOOD,
        passengerMessage = "Weather data is being updated",
        technicalInfo = "Weather code unknown"
    )

    /**
     * Get complete weather information for a WMO code
     */
    fun getWeatherInfo(code: Int): WeatherInfo {
        return when (code) {
            // Clear conditions
            0 -> clearSkyInfo

            // Partly cloudy conditions
            1 -> mainlyClearInfo

            2 -> partlyCloudyInfo

            3 -> overcastInfo

            // Fog conditions
            45 -> fogInfo

            48 -> rimeFogInfo

            // Drizzle conditions
            51 -> lightDrizzleInfo

            53 -> moderateDrizzleInfo

            55 -> denseDrizzleInfo

            // Freezing drizzle
            56 -> lightFreezingDrizzleInfo

            57 -> denseFreezingDrizzleInfo

            // Rain conditions
            61 -> slightRainInfo

            63 -> moderateRainInfo

            65 -> heavyRainInfo

            // Freezing rain
            66 -> lightFreezingRainInfo

            67 -> heavyFreezingRainInfo

            // Snow conditions
            71 -> slightSnowFallInfo

            73 -> moderateSnowFallInfo

            75 -> heavySnowFallInfo

            // Snow grains
            77 -> snowGrainsInfo

            // Rain showers
            80 -> slightRainShowersInfo

            81 -> moderateRainShowersInfo

            82 -> violentRainShowersInfo

            // Snow showers
            85 -> slightSnowShowersInfo

            86 -> heavySnowShowersInfo

            // Thunderstorms
            95 -> thunderstormInfo

            96 -> thunderstormWithSlightHailInfo

            99 -> thunderstormWithHeavyHailInfo

            // Unknown code
            else -> unknownWeatherInfo
        }
    }

    /**
     * Get just the weather description
     */
    fun getDescription(code: Int): String = getWeatherInfo(code).description

    /**
     * Get just the flight condition
     */
    fun getFlightCondition(code: Int): FlightCondition = getWeatherInfo(code).flightCondition

    /**
     * Get passenger-friendly message (reassuring)
     */
    fun getPassengerMessage(code: Int): String = getWeatherInfo(code).passengerMessage

    /**
     * Get technical flight information (accurate for pilots)
     */
    fun getTechnicalInfo(code: Int): String = getWeatherInfo(code).technicalInfo

    /**
     * Check if weather is suitable for flying (Excellent or Good conditions)
     */
    fun isSuitableForFlying(code: Int): Boolean {
        val condition = getFlightCondition(code)
        return condition == FlightCondition.EXCELLENT || condition == FlightCondition.GOOD
    }
}
