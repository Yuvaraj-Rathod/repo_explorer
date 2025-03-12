package com.example.repoexplorer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repoexplorer.repository.FCMRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FCMViewModel @Inject constructor(
    private val fcmRepository: FCMRepository
) : ViewModel() {

    val token = fcmRepository.token

    init {
        viewModelScope.launch {
            fcmRepository.token.collectLatest { token ->
                token?.let {
                    Log.d("FCMViewModel", "User FCM Token: $it")
                }
            }
        }
    }

    fun subscribeToTopic(topic: String) {
        fcmRepository.subscribeToTopic(topic)
    }

    fun unsubscribeFromTopic(topic: String) {
        fcmRepository.unsubscribeFromTopic(topic)
    }
}
