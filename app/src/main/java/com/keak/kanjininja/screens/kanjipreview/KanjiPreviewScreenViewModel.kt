package com.keak.kanjininja.screens.kanjipreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keak.kanjininja.extensions.EMPTY_STRING
import com.keak.kanjininja.network.onError
import com.keak.kanjininja.network.onException
import com.keak.kanjininja.network.onSuccess
import com.keak.kanjininja.preference.GetRandomKanjiUseCase
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
    private val getRandomKanjiUseCase: GetRandomKanjiUseCase,

    ) : ViewModel() {

    private val _kanjiPreviewScreenState =
        MutableStateFlow<KanjiPreviewScreenState>(KanjiPreviewScreenState.Initial)
    val kanjiPreviewScreenState: StateFlow<KanjiPreviewScreenState> = _kanjiPreviewScreenState


    private val _selectedKanji = MutableStateFlow<String?>(null)
    val selectedKanji: StateFlow<String?> = _selectedKanji

    fun getAllKanjiAndDetailByGrade(grade: String) {
        viewModelScope.launch {
            _kanjiPreviewScreenState.emit(KanjiPreviewScreenState.Loading)
            kanjiPreviewScreenUseCase.getAllKanjiByGrade(grade)
                .onSuccess { kanjiList ->
                    getRandomKanjiUseCase.insertKanjiList(kanjiList, grade = grade)
                    val selected = getRandomKanjiUseCase.getRandomKanji(
                        _selectedKanji.value,
                        grade = grade
                    )?.kanji
                    Timber.tag("RoomUseCase").d("Selected Kanji --> $selected")
                    if (selected == null) {
                        _kanjiPreviewScreenState.emit(
                            KanjiPreviewScreenState.Error(
                                0,
                                "No kanji found"
                            )
                        )
                        return@onSuccess
                    }

                    _selectedKanji.value = selected

                    kanjiPreviewScreenUseCase.getKanjiDetail(selected)
                        .onSuccess { detail ->
                            _kanjiPreviewScreenState.emit(
                                KanjiPreviewScreenState.Success(
                                    kanjiList = kanjiList,
                                    selectedKanjiDetail = detail
                                )
                            )
                        }
                        .onError { code, message ->
                            _kanjiPreviewScreenState.emit(
                                KanjiPreviewScreenState.Error(code, "Detail error: $message")
                            )
                        }
                        .onException { e ->
                            _kanjiPreviewScreenState.emit(
                                KanjiPreviewScreenState.Exception(e)
                            )
                        }
                }
                .onError { code, message ->
                    _kanjiPreviewScreenState.emit(KanjiPreviewScreenState.Error(code, message))
                }
                .onException { e ->
                    _kanjiPreviewScreenState.emit(KanjiPreviewScreenState.Exception(e))
                }
        }
    }

}


sealed class KanjiPreviewScreenState {
    data object Loading : KanjiPreviewScreenState()
    data object Initial : KanjiPreviewScreenState()
    class Success(
        val kanjiList: List<KanjiByGradeViewItem>,
        val selectedKanjiDetail: KanjiDetailViewItem,
    ) : KanjiPreviewScreenState()

    class Error(
        val code: Int = 0,
        val message: String? = EMPTY_STRING,
    ) : KanjiPreviewScreenState()

    class Exception(
        val throwable: Throwable,
    ) : KanjiPreviewScreenState()
}