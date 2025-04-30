package com.keak.kanjininja.screens.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keak.kanjininja.navigation.Router
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreen(modifier: Modifier = Modifier, router: Router) {
    val quizScreenViewModel: QuizScreenViewModel = hiltViewModel()

    val quizScreenState by quizScreenViewModel.quizScreenState.collectAsStateWithLifecycle()
    val quizScreenEvent = quizScreenViewModel.quizScreenEvent

    var kanjiViewItem by remember { mutableStateOf<KanjiDetailViewItem?>(null) }

    LaunchedEffect(Unit) {
        quizScreenViewModel.getAllKanjiByGrade(grade = "1")
    }
    LaunchedEffect(quizScreenEvent) {
        when (quizScreenEvent) {
            QuizScreenEvents.Clear -> {
                return@LaunchedEffect
            }

            is QuizScreenEvents.GetKanjiDetail -> {
                kanjiViewItem = quizScreenEvent.kanjiDetailViewItem
            }

            QuizScreenEvents.Initial -> {}
        }
        quizScreenViewModel.quizScreenEvent = QuizScreenEvents.Clear

    }
    when (quizScreenState) {
        is QuizScreenState.Success -> {
            Column {
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = quizScreenViewModel.selectedKanji
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "kanji: ${kanjiViewItem?.kanjiCharacter}"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "${kanjiViewItem?.kunyomiJa}"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "${kanjiViewItem?.kunyomiEng}"
                    )
                }
            }

        }

        is QuizScreenState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = "${(quizScreenState as QuizScreenState.Error).message}"
                )
            }
        }

        is QuizScreenState.Exception -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = "${(quizScreenState as QuizScreenState.Exception).throwable.message}"
                )
            }
        }

        QuizScreenState.Initial -> {}
        QuizScreenState.Loading -> {}
        null -> {}
    }

}