package com.keak.kanjininja.screens.quiz

import com.keak.kanjininja.network.ApiError
import com.keak.kanjininja.network.ApiException
import com.keak.kanjininja.network.ApiResult
import com.keak.kanjininja.network.ApiSuccess
import com.keak.kanjininja.network.response.KanjiByGradeResponse
import javax.inject.Inject

class QuizScreenMapper @Inject constructor() {
    fun mapKanjiByGradeResponse(response: ApiResult<KanjiByGradeResponse>): ApiResult<List<KanjiByGradeViewItem>> {
        return when (response) {
            is ApiSuccess -> {
                ApiSuccess(
                    response.data.map { kanjiItem ->
                        KanjiByGradeViewItem(
                            kanji = kanjiItem.kanji?.character.orEmpty(),
                            kanjiStroke = kanjiItem.kanji?.stroke.toString()
                        )
                    }
                )
            }

            is ApiError -> {
                ApiError(response.code, response.message)
            }

            is ApiException -> {
                ApiException(response.e)
            }

        }
    }
}