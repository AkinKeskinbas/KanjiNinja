package com.keak.kanjininja.network

import com.keak.kanjininja.network.response.KanjiByGradeResponse
import com.keak.kanjininja.network.response.KanjiDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {
    @GET("search/advanced/")
    suspend fun getAllKanjiByGrade(
        @Query("grade") grade: String = "1",
    ): Response<KanjiByGradeResponse>

    @GET("kanji/{character}")
    suspend fun getOneKanjiByCharacter(
        @Path("character") kanjiCharacter: String,
    ): Response<KanjiDetailResponse>
}