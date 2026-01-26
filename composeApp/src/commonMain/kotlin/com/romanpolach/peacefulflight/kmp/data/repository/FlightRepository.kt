package com.romanpolach.peacefulflight.kmp.data.repository

import com.romanpolach.peacefulflight.kmp.data.local.AppDatabase
import com.romanpolach.peacefulflight.kmp.data.local.FlightSession
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

/**
 * Flight repository - manages flight session data using Room Multiplatform
 */
class FlightRepository(private val database: AppDatabase) {

    private val flightDao = database.flightDao()

    val allFlights: Flow<List<FlightSession>> = flightDao.getAllFlights()

    suspend fun startFlight(expectedFear: Int): Long {
        val flight = FlightSession(
            startTime = Clock.System.now().toEpochMilliseconds(),
            expectedFear = expectedFear
        )
        return flightDao.insertFlight(flight)
    }

    suspend fun endFlight(flightId: Long, actualFear: Int) {
        val flight = flightDao.getFlightById(flightId)
        if (flight != null) {
            val updatedFlight = flight.copy(
                endTime = Clock.System.now().toEpochMilliseconds(),
                actualFear = actualFear
            )
            flightDao.updateFlight(updatedFlight)
        }
    }

    suspend fun getFlight(id: Long): FlightSession? {
        return flightDao.getFlightById(id)
    }

    suspend fun getActiveFlightWithin24Hours(): FlightSession? {
        val cutoffTime = Clock.System.now().toEpochMilliseconds() - (24 * 60 * 60 * 1000)
        return flightDao.getUnfinishedFlightAfter(cutoffTime)
    }
}
