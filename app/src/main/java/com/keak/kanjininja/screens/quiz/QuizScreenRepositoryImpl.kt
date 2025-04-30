package com.keak.kanjininja.screens.quiz

import com.keak.kanjininja.network.ApiResult
import com.keak.kanjininja.network.AppService
import com.keak.kanjininja.network.handleApi
import com.keak.kanjininja.network.response.KanjiByGradeResponse
import com.keak.kanjininja.network.response.KanjiDetailResponse
import javax.inject.Inject

class QuizScreenRepositoryImpl @Inject constructor (
    val appService: AppService
): QuizScreenRepository {
    override suspend fun getKanjiByGrade(grade: String): ApiResult<KanjiByGradeResponse>  =
        handleApi{
            appService.getAllKanjiByGrade(grade)
        }

    override suspend fun getKanjiDetail(kanjiName: String): ApiResult<KanjiDetailResponse>  =
        handleApi {
            appService.getOneKanjiByCharacter(kanjiCharacter = kanjiName)
        }

}