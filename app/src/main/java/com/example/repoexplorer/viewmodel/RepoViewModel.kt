package com.example.repoexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repoexplorer.repository.RepoRepository
import com.example.repoexplorer.room.RepositoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val repository: RepoRepository
) : ViewModel() {
    private val _repositories = MutableStateFlow<List<RepositoryEntity>>(emptyList())
    val repositories: StateFlow<List<RepositoryEntity>> = _repositories

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Clears the error state.
    fun clearError() {
        _error.value = null
    }

    // Load repositories from the network and cache them.
    fun loadUserRepositories(username: String) {
        viewModelScope.launch {
            // Clear any existing error before starting a new request.
            _error.value = null
            try {
                val data = repository.fetchUserRepositories(username)
                _repositories.value = data
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // Load cached repositories.
    fun loadCachedRepositories() {
        viewModelScope.launch {
            _repositories.value = repository.getCachedRepositories()
        }
    }

    // Search repositories via the network.
    fun searchRepositories(query: String) {
        viewModelScope.launch {
            // Clear any previous error.
            _error.value = null
            try {
                val data = repository.searchRepositories(query)
                _repositories.value = data
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
