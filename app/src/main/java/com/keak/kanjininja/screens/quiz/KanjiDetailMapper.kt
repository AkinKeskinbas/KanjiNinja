package com.keak.kanjininja.screens.quiz

import com.keak.kanjininja.extensions.extractKanaInParentheses
import com.keak.kanjininja.extensions.takeBeforeParensAndRemoveSpaces
import com.keak.kanjininja.network.ApiError
import com.keak.kanjininja.network.ApiException
import com.keak.kanjininja.network.ApiResult
import com.keak.kanjininja.network.ApiSuccess
import com.keak.kanjininja.network.response.KanjiDetailResponse
import javax.inject.Inject
import kotlin.collections.orEmpty

class KanjiDetailMapper @Inject constructor() {
    fun mapKanjiDetailToViewItem(response: ApiResult<KanjiDetailResponse>): ApiResult<KanjiDetailViewItem> {
        return when (response) {
            is ApiSuccess -> {
                ApiSuccess(
                    KanjiDetailViewItem(
                        kanjiCharacter = response.data.kanjiDetail?.character.orEmpty(),
                        kunyomiEng = response.data.kanjiDetail?.kunyomi?.romaji.orEmpty(),
                        kunyomiJa = response.data.kanjiDetail?.kunyomi?.hiragana.orEmpty(),
                        onyomiEng = response.data.kanjiDetail?.onyomi?.romaji.orEmpty(),
                        onyomiJa = response.data.kanjiDetail?.onyomi?.katakana.orEmpty(),
                        meaningEng = response.data.kanjiDetail?.meaning?.english.orEmpty(),
                        kanjiDrawCount = response.data.kstroke.toString(),
                        kanjiImage = response.data.kanjiDetail?.strokes?.images?.last().orEmpty(),
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
                        hint = response.data.mnHint.orEmpty(),
                        questionAndAnswers = response.data.examples?.map{ examples->
                            QuestionAndAnswer(
                                question = examples.japanese?.takeBeforeParensAndRemoveSpaces().orEmpty(),
                                answer = examples.japanese?.extractKanaInParentheses().orEmpty()
                            )
                        }.orEmpty()
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