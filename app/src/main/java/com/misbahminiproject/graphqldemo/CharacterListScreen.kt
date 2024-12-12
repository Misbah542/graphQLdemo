package com.misbahminiproject.graphqldemo

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.apollographql.apollo3.api.Optional
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen() {
    val coroutineScope = rememberCoroutineScope()
    var characters by remember { mutableStateOf<List<GetCharactersQuery.Result>>(emptyList()) }
    var nextPage by remember { mutableStateOf<Int?>(1) }
    val isLoading by remember { mutableStateOf(false) }
    val errorMessage by remember { mutableStateOf<String?>(null) }
    var isViewAllEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        loadCharacters(1) { results, next ->
            characters = results
            nextPage = next
            isViewAllEnabled = results.size >= 20
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rick and Morty Characters") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(characters) { character ->
                    CharacterCard(character)
                }
                if (isViewAllEnabled && nextPage != null && !isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(onClick = {
                                coroutineScope.launch {
                                    loadCharacters(nextPage) { results, next ->
                                        characters = characters + results
                                        nextPage = next
                                        isViewAllEnabled = results.isNotEmpty()
                                    }
                                }
                            }) {
                                Text(text = "View Next")
                            }
                        }
                    }
                }
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                if (errorMessage != null) {
                    item {
                        Text(
                            text = "Error: $errorMessage",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}


suspend fun loadCharacters(
    page: Int?,
    onResult: (results: List<GetCharactersQuery.Result>, next: Int?) -> Unit
) {
    if (page == null) return

    try {
        val response = RickAndMortyApiClient.apolloClient
            .query(GetCharactersQuery(Optional.present(page)))
            .execute()

        val results = response.data?.characters?.results?.filterNotNull().orEmpty()
        val nextPage = response.data?.characters?.info?.next

        onResult(results, nextPage)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun CharacterCard(character: GetCharactersQuery.Result) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(model = character.image),
                contentDescription = character.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = character.name ?: "Unknown", fontSize = 20.sp)
                Text(
                    text = character.status ?: "Unknown",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
