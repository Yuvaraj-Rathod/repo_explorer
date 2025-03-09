package com.example.repoexplorer.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.repoexplorer.room.RepositoryEntity
import com.example.repoexplorer.viewmodel.RepositoryViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: RepositoryViewModel = hiltViewModel()) {
    val repositories by viewModel.repositories.collectAsState()
    val error by viewModel.error.collectAsState()
    var query by remember { mutableStateOf("")}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Repositories") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Enter search query") },
                trailingIcon = {
                    IconButton(onClick = { viewModel.searchRepositories(query) }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Repos")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (!error.isNullOrEmpty()) {
                Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(repositories) { repo ->
                    RepositoryItem(repo)
                }
            }
        }
    }
}

@Composable
fun RepositoryItem(repo: RepositoryEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            // Example: Load avatar with Coil (AsyncImage)
            AsyncImage(
                model = repo.owner.avatar_url,
                contentDescription = "Owner Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = repo.name, style = MaterialTheme.typography.titleMedium)
                repo.description?.let {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (!repo.language.isNullOrEmpty()) {
                        Text(text = "Lang: ${repo.language}", style = MaterialTheme.typography.labelSmall)
                    }
                    Text(text = "★ ${repo.stargazersCount}", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
