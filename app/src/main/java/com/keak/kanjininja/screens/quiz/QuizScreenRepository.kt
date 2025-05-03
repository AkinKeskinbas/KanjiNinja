package com.keak.kanjininja.screens.quiz

import com.keak.kanjininja.network.ApiResult
import com.keak.kanjininja.network.response.KanjiDetailResponse

interface QuizScreenRepository {
    suspend fun getQuestion(kanji: String):ApiResult<KanjiDetailResponse>
}