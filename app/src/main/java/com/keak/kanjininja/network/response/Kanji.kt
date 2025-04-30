package com.keak.kanjininja.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Kanji(
    @SerialName("character")
    val character: String?,
    @SerialName("stroke")
    val stroke: Int?
)