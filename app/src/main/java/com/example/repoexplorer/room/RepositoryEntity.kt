package com.example.repoexplorer.room


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.repoexplorer.model.Owner


@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String?,
    val htmlUrl: String,
    @Embedded val owner: Owner,
    val stargazersCount: Int,
    val language: String?
)
