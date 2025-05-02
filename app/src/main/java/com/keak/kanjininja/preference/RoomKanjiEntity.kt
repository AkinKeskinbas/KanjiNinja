package com.keak.kanjininja.preference

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kanji")
data class KanjiEntity(
    @PrimaryKey val id: Int,
    val kanji: String,
    val stroke: String
)