package com.keak.kanjininja.preference

import com.keak.kanjininja.screens.kanjipreview.KanjiByGradeViewItem
import javax.inject.Inject

class RoomRepositoryImpl  @Inject constructor(
    private val dao: KanjiDao
) : RoomRepository {

    override suspend fun insertKanjiList(list: List<KanjiByGradeViewItem>, grade: String) {
        val entities = list.mapIndexed { index, item ->
            KanjiEntity(id = index, kanji = item.kanji, stroke = item.kanjiStroke, grade = grade)
        }
        dao.insertAll(entities)
    }

    override suspend fun getRandomKanji(previousKanji: String?, grade: String): KanjiByGradeViewItem? {
        val all = dao.getAll()
        val filtered = if (previousKanji != null) {
            all?.filterNot { it.kanji == previousKanji && it.grade == grade}
        } else all

        return filtered?.shuffled()?.firstOrNull()?.let {
            KanjiByGradeViewItem(it.kanji, it.stroke)
        }
    }

    override suspend fun getAllList(): List<KanjiByGradeViewItem>? {
       val all = dao.getAll()
       val kanjiList = all?.map { KanjiByGradeViewItem(kanji = it.kanji, kanjiStroke = it.stroke) }.orEmpty()
        return if (all.isNullOrEmpty()) null else kanjiList
    }
}