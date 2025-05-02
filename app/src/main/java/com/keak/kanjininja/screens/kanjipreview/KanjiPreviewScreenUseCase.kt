package com.keak.kanjininja.screens.kanjipreview

import com.keak.kanjininja.network.ApiResult
import com.keak.kanjininja.network.ApiSuccess
import com.keak.kanjininja.preference.RoomRepository
import com.keak.kanjininja.screens.quiz.KanjiDetailViewItem
import timber.log.Timber
import javax.inject.Inject

class KanjiPreviewScreenUseCase @Inject constructor(
    private val quizScreenRepository: KanjiPreviewScreenRepository,
    private val kanjiPreviewScreenMapper: KanjiPreviewScreenMapper,
    private val roomRepository: RoomRepository,
) {
    suspend fun getAllKanjiByGrade(grade: String): ApiResult<List<KanjiByGradeViewItem>> {
        val kanjiList = roomRepository.getAllList()
        return if (kanjiList.isNullOrEmpty()) {
            Timber.tag("RoomUseCase").d("Network Operation Used!")
            kanjiPreviewScreenMapper.mapKanjiByGradeResponse(
                quizScreenRepository.getKanjiByGrade(
                    grade
                )
            )
        } else {
            Timber.tag("RoomUseCase").d("Room Operation Used!")
            ApiSuccess(kanjiList)
        }
    }

    suspend fun getKanjiDetail(kanji: String): ApiResult<KanjiDetailViewItem> {
        return kanjiPreviewScreenMapper.mapKanjiDetailToViewItem(
            quizScreenRepository.getKanjiDetail(
                kanjiName = kanji
            )
        )
    }
}