package com.keak.kanjininja.preference

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KanjiEntity::class], version = 1)
abstract class KanjiDatabase : RoomDatabase() {
    abstract fun kanjiDao(): KanjiDao
}