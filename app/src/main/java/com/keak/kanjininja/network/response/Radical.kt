package com.keak.kanjininja.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Radical(
    @SerialName("character")
    val character: String?,
    @SerialName("order")
    val order: Int?,
    @SerialName("stroke")
    val stroke: Int?
)