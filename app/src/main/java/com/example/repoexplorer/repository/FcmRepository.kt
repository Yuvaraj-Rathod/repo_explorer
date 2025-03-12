package com.example.repoexplorer.repository

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FCMRepository @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging
) {
    private val _token = MutableStateFlow<String?>(null)
    val token: Flow<String?> = _token.asStateFlow()

    init {
        getToken()
    }

    private fun getToken() {
        firebaseMessaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCMRepository", "Fetching FCM token failed", task.exception)
                return@addOnCompleteListener
            }
            _token.value = task.result
            Log.d("FCMRepository", "FCM Token: ${task.result}")
        }
    }

    fun subscribeToTopic(topic: String) {
        firebaseMessaging.subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCMRepository", "Subscribed to topic: $topic")
                } else {
                    Log.e("FCMRepository", "Failed to subscribe to topic: $topic", task.exception)
                }
            }
    }

    fun unsubscribeFromTopic(topic: String) {
        firebaseMessaging.unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCMRepository", "Unsubscribed from topic: $topic")
                } else {
                    Log.e("FCMRepository", "Failed to unsubscribe from topic: $topic", task.exception)
                }
            }
    }
}
