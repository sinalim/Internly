package com.example.internly.data

import com.example.internly.BuildConfig

object AiConfig {
    val API_KEY: String = BuildConfig.GEMINI_API_KEY

    const val MODEL = "gemini-3.8-flash"
}