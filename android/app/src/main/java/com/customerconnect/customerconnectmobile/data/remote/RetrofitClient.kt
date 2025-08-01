package com.yourpackage.customerconnectmobile.data.remote // Adjust package name

import android.content.Context // Import Context
import androidx.privacysandbox.tools.core.generator.build
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // For logging
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://customerconnect-0jz7.onrender.com/api/"

    // Function to create an instance of ApiService
    fun create(context: Context): com.google.firebase.appdistribution.gradle.ApiService { // Pass Context for CookieJar
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

        // Logging Interceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Use Level.BODY for development, Level.NONE for release
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(loggingInterceptor) // Add logging interceptor
            .connectTimeout(30, TimeUnit.SECONDS) // Optional: Set timeouts
            .readTimeout(30, TimeUnit.SECONDS)    // Optional: Set timeouts
            .writeTimeout(30, TimeUnit.SECONDS)   // Optional: Set timeouts
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}