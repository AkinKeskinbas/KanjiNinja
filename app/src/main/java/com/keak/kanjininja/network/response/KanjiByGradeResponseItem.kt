package com.keak.kanjininja.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KanjiByGradeResponseItem(
    @SerialName("kanji")
    val kanji: Kanji?,
    @SerialName("radical")
    val radical: Radical?
)