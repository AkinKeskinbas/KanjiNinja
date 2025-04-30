package com.keak.kanjininja.network.response

import com.keak.base.extensions.EMPTY_STRING
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KanjiDetailResponse(
    @SerialName ("_id")
    val id: String? = EMPTY_STRING,
    @SerialName ("_rev")
    val rev: String?= EMPTY_STRING,
    @SerialName ("rad_name_ja")
    val radNameJa: String?= EMPTY_STRING,
    @SerialName ("grade")
    val grade: Int?= 0,
    @SerialName ("hint_group")
    val hintGroup: Int?= 0,
    @SerialName ("kunyomi")
    val kunyomi: String?= EMPTY_STRING,
    @SerialName ("meaning")
    val meaning: String?= EMPTY_STRING,
    @SerialName ("kstroke")
    val kstroke: Int?= 0,
    @SerialName ("examples")
    val examples: List<Example>? = emptyList(),
    @SerialName ("kunyomi_ja")
    val kunyomiJa: String?= EMPTY_STRING,
    @SerialName ("ka_utf")
    val kaUtf: String?= EMPTY_STRING,
    @SerialName ("luminous")
    val luminous: String?= EMPTY_STRING,
    @SerialName ("rad_name_search")
    val radNameSearch: List<String>? = emptyList(),
    @SerialName ("rad_order")
    val radOrder: Int? = 0,
    @SerialName ("txt_books")
    val txtBooks: List<TextBook>? = emptyList(),
    @SerialName ("kname")
    val kName: String?= EMPTY_STRING,
    @SerialName ("rad_utf")
    val radUtf: String?= EMPTY_STRING,
    @SerialName ("stroke_times")
    val strokeTimes: List<Double>? = emptyList(),
    @SerialName ("kunyomi_ka_display")
    val kunyomiKaDisplay: String?= EMPTY_STRING,
    @SerialName ("dick")
    val dick: String?= EMPTY_STRING,
    @SerialName ("rad_name")
    val radName: String?= EMPTY_STRING,
    @SerialName ("dicn")
    val dicn: String?= EMPTY_STRING,
    @SerialName ("mn_hint")
    val mnHint: String?= EMPTY_STRING,
    @SerialName ("rad_stroke")
    val radStroke: Int?= 0,
    @SerialName ("onyomi_ja")
    val onyomiJa: String?= EMPTY_STRING,
    @SerialName ("rad_meaning")
    val radMeaning: String?= EMPTY_STRING,
    @SerialName ("onyomi")
    val onyomi: String?= EMPTY_STRING,
    @SerialName ("ka_id")
    val kaId: String?= EMPTY_STRING,
    @SerialName ("onyomi_search")
    val onyomiSearch: List<String>? = emptyList(),
    @SerialName ("kunyomi_search")
    val kunyomiSearch: List<String>? = emptyList(),
    @SerialName ("meaning_search")
    val meaningSearch: List<String>? = emptyList(),
    @SerialName ("onyomi_ja_search")
    val onyomiJaSearch: List<String>? = emptyList(),
    @SerialName ("kunyomi_ja_search")
    val kunyomiJaSearch: List<String>? = emptyList(),
    @SerialName ("kanji")
    val kanjiDetail:KanjiDetail? = null,
    @SerialName ("radical")
    val radical: RadicalKanji? = null
)

@Serializable
data class RadicalKanji(
    @SerialName ("character")
    val character: String?,
    @SerialName ("strokes")
    val strokes: Int?,
    @SerialName ("image")
    val image: String?,
    @SerialName ("animation")
    val animation: List<String>?
)


@Serializable
data class KanjiDetail(
    @SerialName ("character")
    val character: String?,
    @SerialName ("meaning")
    val meaning : Meaning?,
    @SerialName ("strokes")
    val strokes:Strokes?,
    @SerialName ("onyomi")
    val onyomi:Onyomi?,
    @SerialName ("kunyomi")
    val kunyomi: Kunyomi?,
    @SerialName ("video")
    val kanjiVideo: KanjiVideo?
)

@Serializable
data class KanjiVideo(
    val poster: String?,
    val mp4: String?,
    val webm: String?
)

@Serializable
data class Onyomi(
    @SerialName ("romaji")
    val romaji: String?,
    @SerialName ("katakana")
    val katakana: String?,
)

@Serializable
data class Kunyomi(
    @SerialName ("romaji")
    val romaji: String?,
    @SerialName ("hiragana")
    val hiragana: String?,
)
@Serializable
data class Strokes(
    @SerialName ("count")
    val count: Int?,
    @SerialName ("timings")
    val timing: List<Double>?,
    @SerialName ("images")
    val images: List<String>?
)
@Serializable
data class Example(
    @SerialName ("japanese")
    val japanese: String?,
    @SerialName ("meaning")
    val meaning: Meaning?,
    @SerialName ("audio")
    val audio: Audio?,
)
@Serializable
data class Meaning(
    @SerialName ("english")
    val english: String?,
)
@Serializable
data class Audio(
    @SerialName ("opus")
    val opus: String?,
    @SerialName ("aac")
    val aac: String?,
    @SerialName ("ogg")
    val ogg: String?,
    @SerialName ("mp3")
    val mp3: String?,
)
@Serializable
data class TextBook(
    @SerialName ("chapter")
    val chapter: String?,
    @SerialName ("txt_bk")
    val txtBk: String?,
)