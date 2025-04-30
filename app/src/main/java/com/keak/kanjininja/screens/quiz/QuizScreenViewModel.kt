package com.keak.kanjininja.screens.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keak.base.extensions.EMPTY_STRING
import com.keak.kanjininja.network.onError
import com.keak.kanjininja.network.onException
import com.keak.kanjininja.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class QuizScreenViewModel @Inject constructor(
    private val kanjiQuizScreenUseCase: KanjiQuizScreenUseCase,
    private val kanjiDetailUseCase: KanjiDetailUseCase,
) : ViewModel() {
    private val _quizScreenState: MutableStateFlow<QuizScreenState> =
        MutableStateFlow(QuizScreenState.Initial)
    val quizScreenState: StateFlow<QuizScreenState?> get() = _quizScreenState


    var quizScreenEvent: QuizScreenEvents by mutableStateOf(QuizScreenEvents.Initial)

    var selectedKanji by mutableStateOf(EMPTY_STRING)

    fun getAllKanjiByGrade(grade: String) {
        viewModelScope.launch {
            _quizScreenState.emit(QuizScreenState.Loading)
            kanjiQuizScreenUseCase.getAllKanjiByGrade(grade = grade).onSuccess { kanjiItem ->
                _quizScreenState.emit(QuizScreenState.Success(kanjiList = kanjiItem))
                selectedKanji = kanjiItem.random().kanji
                getKanjiDetail()
            }.onError { code, message ->
                _quizScreenState.emit(QuizScreenState.Error(code, message))
            }.onException { exception ->
                _quizScreenState.emit(QuizScreenState.Exception(exception))
            }
        }
    }

    private fun getKanjiDetail() {
        viewModelScope.launch {
            kanjiDetailUseCase.getKanjiDetail(selectedKanji).onSuccess { kanjiDetail ->
                quizScreenEvent = QuizScreenEvents.GetKanjiDetail(kanjiDetail)
            }.onError { code, message ->
                Timber.log(1, "$message")
            }.onException { throwable ->
                Timber.log(1, "${throwable.message}")
            }
        }
    }
}

sealed class QuizScreenEvents {
    data object Clear : QuizScreenEvents()
    data object Initial : QuizScreenEvents()
    class GetKanjiDetail(
        val kanjiDetailViewItem: KanjiDetailViewItem,
    ) : QuizScreenEvents()
}

sealed class QuizScreenState {
    data object Loading : QuizScreenState()
    data object Initial : QuizScreenState()
    class Success(
        val kanjiList: List<KanjiByGradeViewItem>,
    ) : QuizScreenState()

    class Error(
        val code: Int = 0,
        val message: String? = EMPTY_STRING,
    ) : QuizScreenState()

    class Exception(
        val throwable: Throwable,
    ) : QuizScreenState()
}