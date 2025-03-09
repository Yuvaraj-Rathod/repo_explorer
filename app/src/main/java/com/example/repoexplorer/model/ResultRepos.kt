package com.example.repoexplorer.model

data class SearchResponse(
    val total_count: Int,
    val items: List<ResultReposItem>
)