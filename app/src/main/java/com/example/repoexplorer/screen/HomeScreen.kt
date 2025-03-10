package com.example.repoexplorer.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.repoexplorer.ui.theme.SlateBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    viewModel: RepositoryViewModel = hiltViewModel()
) {
    // Observe repositories and errors from ViewModel
    val repositories by viewModel.repositories.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserRepositories("Yuvaraj-Rathod)")
    }

    Scaffold(
        topBar = {
            CustomAppBar(
                title = "Repo Explorer",
                onMenuClick = {
                    // Handle leading icon click (e.g., open navigation drawer)
                },
                onSearchClick = {
                    onSearchClick()
                },
                onFavoriteClick = {

                }
            )
        }
    ) { innerPadding ->
        // Content area remains the same.
        Box(modifier = Modifier.padding(innerPadding)) {
            if (!error.isNullOrEmpty()) {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
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
    onMenuClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth().padding(top = 25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 1.dp, vertical = 12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Leading Elevated Icon (Menu)
            ElevatedIcon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                onClick = onMenuClick
            )
            // Title Text centered
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            // Trailing Elevated Icon (Favorite)
            ElevatedIcon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                onClick = onFavoriteClick
            )
            // Trailing Elevated Icon (Search)
            ElevatedIcon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                onClick = onSearchClick
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
            color = SlateBlue
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



