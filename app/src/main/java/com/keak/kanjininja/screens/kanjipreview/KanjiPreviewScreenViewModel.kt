package com.keak.kanjininja.screens.kanjipreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keak.kanjininja.extensions.EMPTY_STRING
import com.keak.kanjininja.network.onError
import com.keak.kanjininja.network.onException
import com.keak.kanjininja.network.onSuccess
import com.keak.kanjininja.preference.GetRandomKanjiUseCase
import com.keak.kanjininja.screens.quiz.KanjiDetailUseCase
import com.keak.kanjininja.screens.quiz.KanjiDetailViewItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class KanjiPreviewScreenViewModel @Inject constructor(
    private val kanjiPreviewScreenUseCase: KanjiPreviewScreenUseCase,
    private val getRandomKanjiUseCase: GetRandomKanjiUseCase

) : ViewModel() {

    private val _quizScreenState = MutableStateFlow<QuizScreenState>(QuizScreenState.Initial)
    val quizScreenState: StateFlow<QuizScreenState> = _quizScreenState


    private val _selectedKanji = MutableStateFlow<String?>(null)
    val selectedKanji: StateFlow<String?> = _selectedKanji

    fun getAllKanjiAndDetailByGrade(grade: String) {
        viewModelScope.launch {


            kanjiPreviewScreenUseCase.getAllKanjiByGrade(grade)
                .onSuccess { kanjiList ->
                    getRandomKanjiUseCase.insertKanjiList(kanjiList)
                    val selected = getRandomKanjiUseCase.getRandomKanji(_selectedKanji.value)?.kanji
                    Timber.tag("RoomUseCase").d("Selected Kanji --> $selected")
                    if (selected == null) {
                        _quizScreenState.emit(QuizScreenState.Error(0, "No kanji found"))
                        return@onSuccess
                    }

                    _selectedKanji.value = selected

                    kanjiPreviewScreenUseCase.getKanjiDetail(selected)
                        .onSuccess { detail ->
                            _quizScreenState.emit(
                                QuizScreenState.Success(
                                    kanjiList = kanjiList,
                                    selectedKanjiDetail = detail
                                )
                            )
                        }
                        .onError { code, message ->
                            _quizScreenState.emit(
                                QuizScreenState.Error(code, "Detail error: $message")
                            )
                        }
                        .onException { e ->
                            _quizScreenState.emit(
                                QuizScreenState.Exception(e)
                            )
                        }
                }
                .onError { code, message ->
                    _quizScreenState.emit(QuizScreenState.Error(code, message))
                }
                .onException { e ->
                    _quizScreenState.emit(QuizScreenState.Exception(e))
                }
        }
    }

}

sealed class KanjiScreenState {
    object ShowDetail : KanjiScreenState()
    object ShowQuiz : KanjiScreenState()
}

sealed class QuizScreenState {
    data object Loading : QuizScreenState()
    data object Initial : QuizScreenState()
    class Success(
        val kanjiList: List<KanjiByGradeViewItem>,
        val selectedKanjiDetail: KanjiDetailViewItem,
    ) : QuizScreenState()

    class Error(
        val code: Int = 0,
        val message: String? = EMPTY_STRING,
    ) : QuizScreenState()

    class Exception(
        val throwable: Throwable,
    ) : QuizScreenState()
}