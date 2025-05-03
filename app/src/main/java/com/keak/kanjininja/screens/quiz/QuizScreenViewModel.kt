package com.keak.kanjininja.screens.quiz



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keak.kanjininja.extensions.EMPTY_STRING
import com.keak.kanjininja.navigation.Router
import com.keak.kanjininja.network.onError
import com.keak.kanjininja.network.onException
import com.keak.kanjininja.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuizScreenViewModel @Inject constructor(
    private val useCase: KanjiDetailUseCase
) : ViewModel() {

    private val _kanjiDetail = MutableStateFlow<KanjiDetailViewItem?>(null)
    private val _currentIndex = MutableStateFlow(0)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<QuizUiModel> = combine(
        _kanjiDetail,
        _currentIndex,
        _isLoading,
        _errorMessage
    ) { detail, index, loading, error ->
        QuizUiModel(
            isLoading = loading,
            errorMessage = error,
            kanjiDetail = detail,
            currentQuestion = detail?.questionAndAnswers?.getOrNull(index)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuizUiModel())

    fun getQuestionAndDetails(kanji: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            useCase.getQuestion(kanji)
                .onSuccess { detail ->
                    _kanjiDetail.value = detail
                    _currentIndex.value = 0
                    //Timber.tag("Question").d("QuestionList-->${detail.questionAndAnswers}")
                }
                .onError { _, message ->
                    _errorMessage.value = "Detail error: $message"
                }
                .onException { e ->
                    _errorMessage.value = e.message
                }

            _isLoading.value = false
        }
    }

    fun onNextQuestion(router: Router, onNextQuestionUIAction:()-> Unit) {
        viewModelScope.launch{
            delay(500)
            onNextQuestionUIAction.invoke()
            val detail = _kanjiDetail.value ?: return@launch
            val nextIndex = _currentIndex.value + 1
            if (nextIndex >= detail.examples.size) {
                goToKanjiPreviewScreenToNewKanji(router)
            } else {
                _currentIndex.value = nextIndex
            }
        }

    }

    private fun goToKanjiPreviewScreenToNewKanji(router: Router) {
        viewModelScope.launch {
            _currentIndex.value = 0
            router.goToKanjiPreviewScreen()
        }
    }
}
data class QuizUiModel(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val kanjiDetail: KanjiDetailViewItem? = null,
    val currentQuestion: QuestionAndAnswer? = null
)