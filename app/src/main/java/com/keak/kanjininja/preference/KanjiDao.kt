package com.keak.kanjininja.preference;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
interface KanjiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(kanjiList:List<KanjiEntity>)

    @Query("SELECT * FROM kanji")
    suspend fun getAll(): List<KanjiEntity>?

    @Query("SELECT * FROM kanji ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomKanji(): KanjiEntity?
}