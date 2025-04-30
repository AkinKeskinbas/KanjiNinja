package com.keak.kanjininja.screens.quiz

import com.keak.kanjininja.network.ApiResult
import javax.inject.Inject

class KanjiQuizScreenUseCase @Inject constructor(
    private val quizScreenRepository: QuizScreenRepository,
    private val quizScreenMapper: QuizScreenMapper,
) {
    suspend fun getAllKanjiByGrade(grade: String): ApiResult<List<KanjiByGradeViewItem>> {
        return quizScreenMapper.mapKanjiByGradeResponse(quizScreenRepository.getKanjiByGrade(grade))
    }
}