package com.example.repoexplorer.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.repoexplorer.room.RepositoryEntity
import com.example.repoexplorer.viewmodel.RepositoryViewModel
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.repoexplorer.ui.theme.SlateBlue
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.unit.sp
import com.example.repoexplorer.ui.theme.CharcoalBlue
import com.example.repoexplorer.ui.theme.MidnightBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onSignOut: () -> Unit,
    onFabClick: () -> Unit, // Callback for FAB click
    viewModel: RepositoryViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var searchAttempted by remember { mutableStateOf(false) }
    var showSearchField by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val repositories by viewModel.repositories.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCachedRepositories()
    }

    Scaffold(
        topBar = {
            CustomAppBar(
                title = "Repo Explorer",
                onUserSearch = { showSearchField = true },
                onSignOut = { showLogoutDialog = true },
                onSearchClick = { onSearchClick() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 15.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Archive, // Use any icon of your choice.
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (showSearchField) {
                TextField(
                    value = username,
                    onValueChange = { newText ->
                        username = newText
                        viewModel.clearError()
                    },
                    placeholder = { Text("Enter Username" , fontSize = 15.sp) },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (username.isNotBlank()) {
                                searchAttempted = true
                                viewModel.loadUserRepositories(username)
                                showSearchField = false
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.PersonSearch,
                                contentDescription = "Search Username"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Logout confirmation dialog.
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Sign Out") },
                    text = { Text("Are you sure you want to sign out?") },
                    confirmButton = {
                        Button(onClick = {
                            showLogoutDialog = false
                            onSignOut()
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showLogoutDialog = false }) {
                            Text("No")
                        }
                    }
                )
            }

            if (!error.isNullOrEmpty()) {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (repositories.isEmpty() && searchAttempted) {
                Text(
                    text = "Username incorrect or not found",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(repositories) { repo ->
                        RepositoryItem(repo)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomAppBar(
    title: String,
    onUserSearch: () -> Unit,
    onSignOut: () -> Unit,
    onSearchClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth().padding(top = 25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Leading Elevated Icon (Menu)
            ElevatedIcon(
                imageVector = Icons.Default.PersonSearch,
                contentDescription = "Menu",
                onClick = onUserSearch
            )
            // Title Text centered
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontSize = 22.sp
            )

            // Trailing Elevated Icon (Search)
            ElevatedIcon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                onClick = onSearchClick
            )

            // Trailing Elevated Icon (Favorite)
            ElevatedIcon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Settings",
                onClick = onSignOut
            )

        }
    }
}
@Composable
fun ElevatedIcon(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(42.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false
            )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            color = MidnightBlue
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}



