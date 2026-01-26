package com.romanpolach.peacefulflight.kmp.data.local

import androidx.room.RoomDatabase

/**
 * Platform-specific provider for Room Database Builder
 */
expect class RoomBuilder {
    fun builder(): RoomDatabase.Builder<AppDatabase>
}

