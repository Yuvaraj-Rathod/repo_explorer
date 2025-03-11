package com.example.repoexplorer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.repoexplorer.navigation.AppNavigation
import com.example.repoexplorer.ui.theme.RepoExplorer
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    var isSignedIn by mutableStateOf(false)
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize FirebaseAuth.
        firebaseAuth = FirebaseAuth.getInstance()

        // Configure Google Sign In options.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from Firebase config
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check if the user is already signed in.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null && firebaseAuth.currentUser != null) {
            Log.d("MainActivity", "Already signed in: ${account.email}")
            isSignedIn = true
        } else {
            Log.d("MainActivity", "No signed in account found.")
        }

        // Register sign in launcher.
        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d("MainActivity", "Google sign in success: ${account?.email}")
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        isSignedIn = authTask.isSuccessful
                        Log.d("MainActivity", "Firebase auth success: $isSignedIn")
                    }
                } catch (e: ApiException) {
                    Log.e("MainActivity", "Sign in failed: ${e.statusCode}")
                    isSignedIn = false
                }
            } else {
                Log.e("MainActivity", "Sign in canceled or failed.")
            }
        }

        setContent {
            val navController = rememberNavController()
            // Navigate to "home" when sign in succeeds.
            LaunchedEffect(isSignedIn) {
                if (isSignedIn) {
                    navController.navigate("home") {
                        popUpTo("sign_in") { inclusive = true }
                    }
                }
            }
            RepoExplorer {
                AppNavigation(
                    navController = navController,
                    onSignInClick = { signIn() },
                    onSignOutClick = {
                        // Sign out from Firebase and update state.
                        firebaseAuth.signOut()
                        isSignedIn = false
                        // Navigate back to sign-in.
                        navController.navigate("sign_in") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }
    }

    fun signIn() {
        // Optionally sign out first to force the account chooser.
        googleSignInClient.signOut().addOnCompleteListener {
            signInLauncher.launch(googleSignInClient.signInIntent)
        }
    }
}
