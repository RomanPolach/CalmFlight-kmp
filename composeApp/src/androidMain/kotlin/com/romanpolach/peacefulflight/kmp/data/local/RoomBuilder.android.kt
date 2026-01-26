package com.romanpolach.peacefulflight.kmp.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

actual class RoomBuilder(private val context: Context) {
    actual fun builder(): RoomDatabase.Builder<AppDatabase> {
        val dbFile = context.getDatabasePath("peacefulflight.db")
        return Room.databaseBuilder<AppDatabase>(
            context = context.applicationContext,
            name = dbFile.absolutePath
        ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
    }
}
