package dev.mindfuldays.app.data.remote

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.mindfuldays.app.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Header
import java.util.Locale
import java.util.concurrent.TimeUnit

// DTOs de la API REST de Gemini (generateContent)
data class GeminiRequest(val contents: List<Content>)
data class Content(val parts: List<Part>)
data class Part(val text: String)

data class GeminiResponse(val candidates: List<Candidate>? = null)
data class Candidate(val content: ContentResponse? = null)
data class ContentResponse(val parts: List<PartResponse>? = null)
data class PartResponse(val text: String? = null)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Header("x-goog-api-key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

class GeminiApiService(
    private val apiKey: String = BuildConfig.GEMINI_API_KEY
) {

    private val api: GeminiApi by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(HttpLoggingInterceptor().apply {
                // La clave viaja en un header y se oculta en Logcat
                level = HttpLoggingInterceptor.Level.BODY
                redactHeader("x-goog-api-key")
            })
            .build()
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApi::class.java)
    }

    suspend fun generateReflection(
        attitudeTitle: String,
        languageCode: String = Locale.getDefault().language
    ): String = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) return@withContext offlineFallback(languageCode)

        val language = when (languageCode) {
            "es" -> "Spanish"
            "pt" -> "Portuguese"
            else -> "English"
        }
        val prompt = "Write a short, inspiring mindfulness reflection about the attitude " +
            "\"$attitudeTitle\" (Jon Kabat-Zinn). Maximum 2 sentences, plain text, " +
            "no markdown, no quotes. Answer in $language."

        try {
            val response = api.generateContent(
                apiKey,
                GeminiRequest(listOf(Content(listOf(Part(prompt)))))
            )
            response.candidates?.firstOrNull()?.content?.parts
                ?.mapNotNull { it.text }?.joinToString("")?.trim()
                ?.takeIf { it.isNotBlank() }
                ?: offlineFallback(languageCode)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("GeminiApiService", "Error calling Gemini API", e)
            offlineFallback(languageCode)
        }
    }

    private fun offlineFallback(languageCode: String): String = when (languageCode) {
        "es" -> "Respira profundo y trae tu atención al momento presente. (Reflexión sin conexión)"
        "pt" -> "Respire fundo e traga sua atenção para o momento presente. (Reflexão offline)"
        else -> "Breathe deeply and bring your attention to the present moment. (Offline reflection)"
    }
}
