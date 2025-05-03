package com.keak.kanjininja.screens.quiz

import com.keak.kanjininja.network.ApiResult
import javax.inject.Inject

class KanjiDetailUseCase @Inject constructor(
    private val kanjiDetailMapper: KanjiDetailMapper,
    private val quizScreenRepository: QuizScreenRepository,
) {
    suspend fun getQuestion(kanji: String): ApiResult<KanjiDetailViewItem> {
        return kanjiDetailMapper.mapKanjiDetailToViewItem(
            quizScreenRepository.getQuestion(
                kanji = kanji
            )
        )
    }
}