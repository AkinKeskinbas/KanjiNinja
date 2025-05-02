package com.keak.kanjininja.preference

import com.keak.kanjininja.screens.kanjipreview.KanjiByGradeViewItem

interface RoomRepository {
    suspend fun insertKanjiList(list: List<KanjiByGradeViewItem>)
    suspend fun getRandomKanji(previousKanji: String?): KanjiByGradeViewItem?
    suspend fun getAllList(): List<KanjiByGradeViewItem>?
}