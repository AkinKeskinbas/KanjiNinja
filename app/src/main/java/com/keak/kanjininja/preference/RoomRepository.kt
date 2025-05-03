package com.keak.kanjininja.preference

import com.keak.kanjininja.screens.kanjipreview.KanjiByGradeViewItem

interface RoomRepository {
    suspend fun insertKanjiList(list: List<KanjiByGradeViewItem>, grade: String)
    suspend fun getRandomKanji(previousKanji: String?,grade: String): KanjiByGradeViewItem?
    suspend fun getAllList(): List<KanjiByGradeViewItem>?
}