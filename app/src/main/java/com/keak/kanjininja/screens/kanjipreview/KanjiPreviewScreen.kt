package com.keak.kanjininja.screens.kanjipreview

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keak.kanjininja.R
import com.keak.kanjininja.common.BaseAsyncImageComponent
import com.keak.kanjininja.common.Media3VideoPlayer
import com.keak.kanjininja.navigation.Router
import com.keak.kanjininja.ui.theme.CyberPunkYellow
import com.keak.kanjininja.ui.theme.WhiteSmoke
import kotlinx.coroutines.delay

@Composable
fun KanjiPreviewScreen(modifier: Modifier = Modifier, router: Router) {
    val quizScreenViewModel: KanjiPreviewScreenViewModel = hiltViewModel()

    val quizScreenState by quizScreenViewModel.quizScreenState.collectAsStateWithLifecycle()
    var isClicked by remember { mutableStateOf(false) }
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
    val interactionSource = remember { MutableInteractionSource() }
    // Reset state after animation
    LaunchedEffect(isClicked) {
        if (isClicked) {
            delay(600) // animation duration
            isClicked = false
        }
    }

    LaunchedEffect(Unit) {
        quizScreenViewModel.getAllKanjiAndDetailByGrade(grade = "1")
    }


    quizScreenState.let { safeState ->
        when (safeState) {
            is QuizScreenState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),

                    ) {

                    KanjiViewComponent(
                        modifier = Modifier,
                        kanjiImage = safeState.selectedKanjiDetail.kanjiImage,
                        animImageOne = safeState.selectedKanjiDetail.kanjiAnimationLast.getOrNull(
                            1
                        )
                            .orEmpty(),
                        animImageTwo = safeState.selectedKanjiDetail.kanjiAnimationLast.getOrNull(
                            0
                        )
                            .orEmpty(),
                        onyomi = safeState.selectedKanjiDetail.onyomiJa + safeState.selectedKanjiDetail.onyomiEng,
                        kunyomi = safeState.selectedKanjiDetail.kunyomiJa + safeState.selectedKanjiDetail.kunyomiEng,
                        hint = safeState.selectedKanjiDetail.hint,
                        engMeaning = safeState.selectedKanjiDetail.meaningEng,
                        kanjiVideo = safeState.selectedKanjiDetail.kanjiVideo,
                    )
                    Spacer(Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.width(300.dp),
                            onClick = {

                            },
                            colors = ButtonColors(
                                containerColor = CyberPunkYellow,
                                contentColor = WhiteSmoke,
                                disabledContainerColor = CyberPunkYellow.copy(alpha = 0.3f),
                                disabledContentColor = WhiteSmoke
                            )
                        ) {
                            Text(
                                text = "Show Examples",
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

                                    isClicked = true
                                    quizScreenViewModel.getAllKanjiAndDetailByGrade(grade = "1")
                                }
                                .graphicsLayer {
                                    rotationZ = rotation
                                    scaleX = bounceScale
                                    scaleY = bounceScale
                                }
                        )
                    }
                    Spacer(Modifier.height(16.dp))
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
        }
    }
}


@Composable
private fun KanjiViewComponent(
    modifier: Modifier = Modifier,
    kanjiImage: String,
    animImageOne: String,
    animImageTwo: String,
    onyomi: String,
    kunyomi: String,
    hint: String,
    engMeaning: String,
    kanjiVideo: String,
    // soundPlayAction: ()-> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(color = CyberPunkYellow, shape = RoundedCornerShape(16.dp))
            .border(color = Color.Black, width = 1.dp, shape = RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            BaseAsyncImageComponent(
                stringImage = kanjiImage,
                modifier = modifier.size(80.dp),
                contentScale = ContentScale.Fit
            )

            BaseAsyncImageComponent(
                stringImage = animImageOne,
                modifier = modifier.size(80.dp)
            )
            BaseAsyncImageComponent(
                stringImage = animImageTwo,
                modifier = modifier.size(80.dp)
            )

        }
        Spacer(Modifier.height(16.dp))
        Media3VideoPlayer(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp)),
            videoUrl = kanjiVideo
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "onyomi: $onyomi",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )

        Text(
            text = "kunyomi: $kunyomi",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "eng-meaning: $engMeaning",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "hint: $hint",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Spacer(Modifier.height(8.dp))
    }


}

@Preview
@Composable
private fun KanjiViewComponentPreview() {
    KanjiViewComponent(
        kanjiImage = "https://media.kanjialive.com/rad_frames/oto2.svg",
        animImageOne = "https://media.kanjialive.com/rad_frames/oto2.svg",
        animImageTwo = "https://media.kanjialive.com/rad_frames/oto2.svg",
        onyomi = "test",
        kunyomi = "asd",
        hint = "Sound of the temple bell, when the sun 日 stands 立 on the horizon.",
        engMeaning = "tes",
        kanjiVideo = "test",
    )
}