package com.example.repoexplorer.repository


import com.example.repoexplorer.api.GitHubApiService
import com.example.repoexplorer.room.RepositoryEntity
import com.example.repoexplorer.room.RepositoryDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoRepository @Inject constructor(
    private val apiService: GitHubApiService,
    private val repositoryDao: RepositoryDao
) {
    // Fetch repositories from the network, cache them locally, and return the list.
    suspend fun fetchUserRepositories(username: String): List<RepositoryEntity> {
        return try {
            // Network call to get repositories for the user.
            val repos = apiService.getUserRepositories(username)
            // Map network model to local entity.
            val entityList = repos.map { repo ->
                RepositoryEntity(
                    id = repo.id,
                    name = repo.name,
                    description = repo.description,
                    htmlUrl = repo.html_url,
                    owner = repo.owner,
                    stargazersCount = repo.stargazers_count,
                    language = repo.language
                )
            }
            // Clear old data and cache new data.
            repositoryDao.clearRepositories()
            repositoryDao.insertRepositories(entityList)
            entityList
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) {
                throw Exception("Username incorrect or not found")
            } else {
                throw Exception("Error: ${e.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Unexpected error: ${e.localizedMessage}")
        }
    }


    // Retrieve cached repositories.
    suspend fun getCachedRepositories(): List<RepositoryEntity> =
        repositoryDao.getAllRepositories()

    // Search repositories via the GitHub API and map them to local entities.
    suspend fun searchRepositories(query: String): List<RepositoryEntity> {
        val response = apiService.searchRepositories(query)
        return response.items.map { repo ->
            RepositoryEntity(
                id = repo.id,
                name = repo.name,
                description = repo.description,
                htmlUrl = repo.html_url,
                owner = repo.owner,
                stargazersCount = repo.stargazers_count,
                language = repo.language
            )
        }
    }
}