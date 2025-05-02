package com.keak.kanjininja.preference;

import com.keak.kanjininja.screens.kanjipreview.KanjiByGradeViewItem
import timber.log.Timber
import javax.inject.Inject

class GetRandomKanjiUseCase @Inject constructor(
    private val repo: RoomRepository,
) {
    suspend fun getRandomKanji(previousKanji: String?): KanjiByGradeViewItem? {
        Timber.tag("RoomUseCase").d("Previous Kanji --> $previousKanji")
        return repo.getRandomKanji(previousKanji)
    }

    suspend fun insertKanjiList(kanjiList: List<KanjiByGradeViewItem>) {
        repo.insertKanjiList(kanjiList)
        Timber.tag("RoomUseCase").d("Kanji List Inserted!")
    }
}