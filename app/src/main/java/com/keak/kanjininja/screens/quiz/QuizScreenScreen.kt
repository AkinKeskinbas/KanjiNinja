package com.keak.kanjininja.screens.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keak.kanjininja.R
import com.keak.kanjininja.extensions.EMPTY_STRING
import com.keak.kanjininja.navigation.Router
import com.keak.kanjininja.ui.theme.CyberPunkYellow
import com.keak.kanjininja.ui.theme.DarkPastelGreen
import com.keak.kanjininja.ui.theme.WhiteSmoke
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(modifier: Modifier = Modifier, router: Router, kanji: String) {

    val quizScreenViewModel: QuizScreenViewModel = hiltViewModel()

    val quizScreenState by quizScreenViewModel.uiState.collectAsStateWithLifecycle()
    var isQuestionAnswered by remember { mutableStateOf(false) }
    var userAnswerText by remember { mutableStateOf(EMPTY_STRING) }
    LaunchedEffect(Unit) {
        quizScreenViewModel.getQuestionAndDetails(kanji)
    }

    //  var progress by remember { mutableStateOf(0.0f) }

//    LaunchedEffect(Unit) {
//        // Progress'i 0'dan 1'e kadar yavaşça arttıralım.
//        while (progress < 0.5f) {
//            delay(50) // 50ms aralıklarla artırıyoruz
//            progress += 0.02f
//        }
//    }
    when {
        quizScreenState.isLoading -> {
            CircularProgressIndicator()
        }

        quizScreenState.errorMessage != null -> {
            Text("Error: ${quizScreenState.errorMessage}")
        }

        quizScreenState.currentQuestion != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 16.dp, vertical = 32.dp)
                            .fillMaxWidth()
                            .background(color = CyberPunkYellow, shape = RoundedCornerShape(16.dp))
                            .border(
                                color = Color.Black,
                                width = 1.dp,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = quizScreenState.currentQuestion?.question.orEmpty(),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        AnimatedVisibility(isQuestionAnswered) {
                            Text(
                                text = quizScreenState.currentQuestion?.answer.orEmpty(),
                                style = MaterialTheme.typography.titleLarge,
                                color = DarkPastelGreen,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    this@Column.AnimatedVisibility(
                        visible = isQuestionAnswered,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = userAnswerText,
                                style = MaterialTheme.typography.titleLarge,
                                color = if (userAnswerText == quizScreenState.currentQuestion?.answer) CyberPunkYellow else Color.Red,
                            )
                            Spacer(Modifier.width(8.dp))
                            if (userAnswerText == quizScreenState.currentQuestion?.answer) {
                                Icon(
                                    Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.Green,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Icon(
                                    Icons.Rounded.Clear,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                        }

                    }
                    KeyboardSafeTextField(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        isQuestionAnswered = isQuestionAnswered
                    ) { userAnswer ->
                        userAnswerText = userAnswer
                        if (isQuestionAnswered.not()) {
                            isQuestionAnswered = true
                        } else {
                            quizScreenViewModel.onNextQuestion(router = router) {
                                isQuestionAnswered = false
                            }
                        }
//                        if (userAnswer == quizScreenState.currentQuestion?.answer) {
//                            Timber.tag("Question").d("Answer Correct $userAnswer")
//                        } else {
//                            Timber.tag("Question")
//                                .d("Answer Wrong Correct Answer is--> ${quizScreenState.currentQuestion?.answer}")
//                        }

                    }
                }

            }
        }

        else -> {
            Text("Henüz veri yok.")
        }
    }
}

@Composable
fun KeyboardSafeTextField(
    modifier: Modifier,
    isQuestionAnswered: Boolean,
    actionDone: (String) -> Unit,

    ) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    var isClicked by remember { mutableStateOf(false) }
    val screenWidth = LocalWindowInfo.current.containerSize.width
    val rotation by animateFloatAsState(
        targetValue = if (isClicked) 120f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "rotationAnim"
    )
    val bounceScale by animateFloatAsState(
        targetValue = if (isClicked) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "bounceAnim"
    )

    LaunchedEffect(isClicked) {
        if (isClicked) {
            delay(600) // animation duration
            isClicked = false
        }
    }
    Column(
        modifier = modifier
            .imePadding() // keyboard'dan yukarıda kalmasını sağlar
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Answer with hiragana...") },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CyberPunkYellow.copy(alpha = 0.3f),
                focusedContainerColor = WhiteSmoke,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = WhiteSmoke
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    actionDone.invoke(text)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    if (isQuestionAnswered) text = EMPTY_STRING
                }
            )
        )
        Spacer(Modifier.height(8.dp))
        AnimatedVisibility(visible = text.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier.width((screenWidth / 2).dp),
                    enabled = text.isNotEmpty(),
                    onClick = {
                        actionDone.invoke(text)
                        //quizScreenViewModel.onNextQuestion(router = router)
                    },
                    colors = ButtonColors(
                        containerColor = CyberPunkYellow,
                        contentColor = WhiteSmoke,
                        disabledContainerColor = CyberPunkYellow.copy(alpha = 0.3f),
                        disabledContentColor = WhiteSmoke
                    )
                ) {
                    Text(
                        text = if (isQuestionAnswered) "Next Question" else "Submit Answer",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.refresh),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (isQuestionAnswered) text = EMPTY_STRING
                            isClicked = true
                            //quizScreenViewModel.getAllKanjiAndDetailByGrade(grade = "1")
                        }
                        .graphicsLayer {
                            rotationZ = rotation
                            scaleX = bounceScale
                            scaleY = bounceScale
                        }
                )
            }
        }

    }
}