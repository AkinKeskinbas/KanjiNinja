package com.keak.kanjininja.screens.kanjipreview

import com.keak.kanjininja.network.ApiResult
import com.keak.kanjininja.network.response.KanjiByGradeResponse
import com.keak.kanjininja.network.response.KanjiDetailResponse

interface KanjiPreviewScreenRepository {
    suspend fun getKanjiByGrade(grade: String): ApiResult<KanjiByGradeResponse>
    suspend fun getKanjiDetail(kanjiName: String): ApiResult<KanjiDetailResponse>
}