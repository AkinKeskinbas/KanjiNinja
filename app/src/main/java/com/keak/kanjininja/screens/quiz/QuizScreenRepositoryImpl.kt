package com.keak.kanjininja.screens.quiz

import com.keak.kanjininja.network.ApiResult
import com.keak.kanjininja.network.AppService
import com.keak.kanjininja.network.handleApi
import com.keak.kanjininja.network.response.KanjiDetailResponse
import javax.inject.Inject

class QuizScreenRepositoryImpl @Inject constructor(
    private val appService: AppService,
) : QuizScreenRepository {
    override suspend fun getQuestion(kanji: String): ApiResult<KanjiDetailResponse> =
        handleApi {
            appService.getOneKanjiByCharacter(kanjiCharacter = kanji)
        }
}