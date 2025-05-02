package com.keak.kanjininja.screens.quiz

data class KanjiDetailViewItem(
    val kanjiCharacter: String,
    val kunyomiEng: String,
    val kunyomiJa: String,
    val onyomiEng: String,
    val onyomiJa: String,
    val meaningEng: String,
    val kanjiDrawCount: String,
    val kanjiDrawingPhotos: List<String>,
    val kanjiVideo: String,
    val kanjiImage: String,
    val examples: List<KanjiExample>,
    val kanjiAnimationLast: List<String>,
    val hint: String
)

data class KanjiExample(
    val japanese: String,
    val meaning: String,
    val audio: String,
)

data class Meaning(
    val english: String,
)

data class Audio(
    val mp3: String,
)
