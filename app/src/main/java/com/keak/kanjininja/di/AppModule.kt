package com.keak.kanjininja.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.keak.kanjininja.BuildConfig
import com.keak.kanjininja.network.AppService
import com.keak.kanjininja.network.NetworkInterceptor
import com.keak.kanjininja.preference.DataStoreManager
import com.keak.kanjininja.preference.KanjiDao
import com.keak.kanjininja.preference.KanjiDatabase
import com.keak.kanjininja.preference.RoomRepository
import com.keak.kanjininja.preference.RoomRepositoryImpl
import com.keak.kanjininja.screens.kanjipreview.KanjiPreviewScreenRepository
import com.keak.kanjininja.screens.kanjipreview.KanjiPreviewScreenRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(
        @ApplicationContext
        app: Context,
    ): Application {
        return app as Application
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideAppService(
        @PpOkHttpClient ppOkHttpClient: OkHttpClient,
    ): AppService {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(BuildConfig.BaseUrl)
            .client(ppOkHttpClient)
            .build()
            .create(AppService::class.java)
    }

    @Provides
    @Singleton
    @PpOkHttpClient
    fun provideOkHttpClient(
        networkInterceptor: NetworkInterceptor,
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder().cache(null).connectTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(networkInterceptor)
            .addInterceptor(logging)
            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))

        return client.build()

    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context,
    ): DataStoreManager {
        return DataStoreManager(app)
    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KanjiDatabase {
        return Room.databaseBuilder(
            context,
            KanjiDatabase::class.java,
            "kanji_db"
        ).build()
    }
    @Provides
    fun provideKanjiDao(db: KanjiDatabase): KanjiDao = db.kanjiDao()

    @Provides
    fun provideKanjiRepository(dao: KanjiDao): RoomRepository = RoomRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideQuizRepository(quizScreenRepository: KanjiPreviewScreenRepositoryImpl): KanjiPreviewScreenRepository =
        quizScreenRepository
}

@Retention
@Qualifier
annotation class PpOkHttpClient