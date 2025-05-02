package com.keak.kanjininja.screens.kanjipreview

import com.keak.kanjininja.network.ApiError
import com.keak.kanjininja.network.ApiException
import com.keak.kanjininja.network.ApiResult
import com.keak.kanjininja.network.ApiSuccess
import com.keak.kanjininja.network.response.KanjiByGradeResponse
import com.keak.kanjininja.network.response.KanjiDetailResponse
import com.keak.kanjininja.screens.quiz.KanjiDetailViewItem
import com.keak.kanjininja.screens.quiz.KanjiExample
import javax.inject.Inject
import kotlin.collections.orEmpty

class KanjiPreviewScreenMapper @Inject constructor() {
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
    fun mapKanjiDetailToViewItem(response: ApiResult<KanjiDetailResponse>): ApiResult<KanjiDetailViewItem> {
        return when (response) {
            is ApiSuccess -> {
                ApiSuccess(
                    KanjiDetailViewItem(
                        kanjiCharacter = response.data.kanjiDetail?.character.orEmpty(),
                        kunyomiEng = response.data.kanjiDetail?.kunyomi?.romaji.orEmpty(),
                        kunyomiJa = response.data.kanjiDetail?.kunyomi?.hiragana.orEmpty(),
                        onyomiEng = response.data.kanjiDetail?.onyomi?.katakana.orEmpty(),
                        onyomiJa = response.data.kanjiDetail?.onyomi?.romaji.orEmpty(),
                        meaningEng = response.data.kanjiDetail?.meaning?.english.orEmpty(),
                        kanjiDrawCount = response.data.kstroke.toString(),
                        kanjiImage = response.data.radical?.image.orEmpty(),
                        kanjiVideo = response.data.kanjiDetail?.kanjiVideo?.mp4.orEmpty(),
                        kanjiDrawingPhotos = response.data.kanjiDetail?.strokes?.images.orEmpty(),
                        examples = response.data.examples?.map { example ->
                            KanjiExample(
                                japanese = example.japanese.orEmpty(),
                                meaning = example.meaning?.english.orEmpty(),
                                audio = example.audio?.mp3.orEmpty()
                            )
                        }.orEmpty(),
                        kanjiAnimationLast = response.data.radical?.animation?.map { it }.orEmpty(),
                        hint = response.data.mnHint.orEmpty()
                    )
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