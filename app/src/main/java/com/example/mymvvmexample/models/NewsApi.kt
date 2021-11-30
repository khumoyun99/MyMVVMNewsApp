package com.example.mymvvmexample.models

data class NewsApi(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)