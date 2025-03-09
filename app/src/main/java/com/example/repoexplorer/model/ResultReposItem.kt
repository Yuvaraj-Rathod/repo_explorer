package com.example.repoexplorer.model

data class ResultReposItem(
    val id: Int,
    val name: String,
    val description: String?,   // May be null if no description is provided
    val html_url: String,
    val owner: Owner,
    val stargazers_count: Int,
    val language: String?       // Optional field indicating the repo's primary language
)