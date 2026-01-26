package com.romanpolach.peacefulflight.kmp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FlightSession::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flightDao(): FlightDao
}

// Interface for platform-specific database construction
interface DBBuilder {
    fun build(): AppDatabase
}
