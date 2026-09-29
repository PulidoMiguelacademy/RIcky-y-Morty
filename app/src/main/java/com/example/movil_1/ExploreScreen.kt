package com.example.movil_1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movil_1.data.model.CharacterDto
import com.example.movil_1.data.model.EpisodeDto
import com.example.movil_1.ui.ExploreViewModel

// Colores de la vista
private val CardBorderBlue = Color(0xFF1E3A5F)
private val SearchBgColor = Color(0xFF0F1A2A)
private val DarkDialogBg = Color(0xFF0D141C)
private val CyanColor = Color(0xFF00E5FF)
private val AliveColor = Color(0xFF4CAF50)
private val DeadColor = Color(0xFFF44336)
private val UnknownColor = Color(0xFF9E9E9E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    navController: NavController,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    viewModel: ExploreViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedEpisodeForDetail by remember { mutableStateOf<Pair<String, String>?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Título de la pantalla
        Text(
            text = "Explorar",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Barra de Búsqueda dinámica conectada a la API
        TextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            placeholder = {
                Text(
                    text = "Buscar episodios, personajes...",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
            },
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = Color.Gray)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorderBlue, RoundedCornerShape(24.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SearchBgColor,
                unfocusedContainerColor = SearchBgColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Chips (Filtros: Todos, Episodios, Personajes, Series)
        val categories = listOf("Todos", "Episodios", "Personajes", "Series")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { category ->
                val isSelected = uiState.selectedCategory == category
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) NeonGreen else Color.Transparent)
                        .border(
                            1.dp,
                            if (isSelected) Color.Transparent else CardBorderBlue,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { viewModel.selectCategory(category) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.Black else Color.LightGray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // ------------------ SECCIÓN PERSONAJES (API) ------------------
        if (uiState.selectedCategory == "Todos" || uiState.selectedCategory == "Personajes") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Personajes (${uiState.characters.size})",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (uiState.characters.isNotEmpty()) "Toca para ver" else "Cargando...",
                    color = NeonGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            if (uiState.characters.isNotEmpty()) {
                                if (uiState.selectedCategory != "Personajes") {
                                    viewModel.selectCategory("Personajes")
                                } else {
                                    viewModel.selectCharacter(uiState.characters.firstOrNull())
                                }
                            }
                        }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.isLoadingCharacters) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonGreen, modifier = Modifier.size(36.dp))
                }
            } else if (uiState.characters.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage ?: "No se encontraron personajes.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(uiState.characters, key = { it.id }) { character ->
                        CharacterItemView(
                            character = character,
                            onClick = { viewModel.selectCharacter(character) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ------------------ SECCIÓN EPISODIOS (API / POPULARES) ------------------
        if (uiState.selectedCategory == "Todos" || uiState.selectedCategory == "Episodios" || uiState.selectedCategory == "Series") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Episodios populares",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ver todo",
                    color = NeonGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            viewModel.selectCategory("Episodios")
                        }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.isLoadingEpisodes && uiState.episodes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonGreen, modifier = Modifier.size(36.dp))
                }
            } else if (uiState.episodes.isNotEmpty()) {
                // Episodios reales traídos desde la API
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    uiState.episodes.take(8).forEachIndexed { index, episode ->
                        ApiEpisodeItem(
                            episode = episode,
                            imageRes = when (index % 4) {
                                0 -> R.drawable.portal
                                1 -> R.drawable.segundamision
                                2 -> R.drawable.quintamis
                                else -> R.drawable.familia
                            },
                            onClick = {
                                selectedEpisodeForDetail = Pair(episode.name, "${episode.episode} • Emitido el: ${episode.airDate} • ${episode.characters.size} personajes")
                            }
                        )
                    }
                }
            } else {
                // Fallback con imágenes locales que el usuario ya tenía configuradas
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    EpisodeItem(
                        title = "El portal a la droga",
                        season = "T1 - E3",
                        rating = "4.9",
                        imageRes = R.drawable.portal,
                        onClick = {
                            selectedEpisodeForDetail = Pair("El portal a la droga", "T1 - E3 • Calificación 4.9 • Un viaje caótico al multiverso con Rick y Morty.")
                        }
                    )
                    EpisodeItem(
                        title = "La historia del señor Nimbus",
                        season = "T3 - E5",
                        rating = "4.7",
                        imageRes = R.drawable.segundamision,
                        onClick = {
                            selectedEpisodeForDetail = Pair("La historia del señor Nimbus", "T3 - E5 • Calificación 4.7 • El rey del océano llega a negociar la paz.")
                        }
                    )
                    EpisodeItem(
                        title = "Ricks en el espacio",
                        season = "T4 - E8",
                        rating = "4.8",
                        imageRes = R.drawable.quintamis,
                        onClick = {
                            selectedEpisodeForDetail = Pair("Ricks en el espacio", "T4 - E8 • Calificación 4.8 • Conflictos cósmicos y clones familiares.")
                        }
                    )
                    EpisodeItem(
                        title = "La familia Rick",
                        season = "T2 - E4",
                        rating = "4.6",
                        imageRes = R.drawable.familia,
                        onClick = {
                            selectedEpisodeForDetail = Pair("La familia Rick", "T2 - E4 • Calificación 4.6 • Invasión de parásitos alienígenas de recuerdos felices.")
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Modal de Detalle de Personaje cuando el usuario toca un avatar
    uiState.selectedCharacter?.let { character ->
        CharacterDetailDialog(
            character = character,
            onDismiss = { viewModel.selectCharacter(null) }
        )
    }

    // Modal de Detalle de Episodio al tocar cualquier episodio
    selectedEpisodeForDetail?.let { (title, desc) ->
        EpisodeDetailDialog(
            title = title,
            description = desc,
            onDismiss = { selectedEpisodeForDetail = null }
        )
    }
}

@Composable
fun CharacterItemView(
    character: CharacterDto,
    onClick: () -> Unit
) {
    val borderColor = when (character.status.lowercase()) {
        "alive" -> NeonGreen
        "dead" -> DeadColor
        else -> CyanColor
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.image)
                    .crossfade(true)
                    .build(),
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.avatar1),
                error = painterResource(id = R.drawable.morty),
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .border(2.dp, borderColor, CircleShape)
            )

            // Indicador de estado (Punto de vida)
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(borderColor)
                    .border(2.dp, DarkDialogBg, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = character.name,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ApiEpisodeItem(
    episode: EpisodeDto,
    imageRes: Int,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SearchBgColor)
            .border(1.dp, CardBorderBlue, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = episode.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 100.dp, height = 70.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = episode.name,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "${episode.episode} • ${episode.airDate}",
                color = Color.LightGray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⭐ 4.9 • ${episode.characters.size} personajes",
                    color = NeonGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tocar para ver",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun EpisodeItem(
    title: String,
    season: String,
    rating: String,
    imageRes: Int,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SearchBgColor)
            .border(1.dp, CardBorderBlue, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 100.dp, height = 70.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = season, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "⭐ $rating", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = "Tocar para ver", color = Color.Gray, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun EpisodeDetailDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkDialogBg),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.LightGray)
                    }
                }

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeonGreen.copy(alpha = 0.15f))
                        .border(1.dp, NeonGreen, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "⭐ 4.9 • Disponible en streaming",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = description,
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reproducir Episodio", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CharacterDetailDialog(
    character: CharacterDto,
    onDismiss: () -> Unit
) {
    val statusColor = when (character.status.lowercase()) {
        "alive" -> AliveColor
        "dead" -> DeadColor
        else -> UnknownColor
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkDialogBg),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.LightGray)
                    }
                }

                // Avatar grande con Coil
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.avatar1),
                    error = painterResource(id = R.drawable.morty),
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .border(3.dp, statusColor, CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = character.name,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Status chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusColor.copy(alpha = 0.2f))
                        .border(1.dp, statusColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${character.status} • ${character.species}",
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = CardBorderBlue)
                Spacer(modifier = Modifier.height(16.dp))

                // Detalles
                DetailRow(label = "Género", value = character.gender.ifBlank { "Desconocido" })
                DetailRow(label = "Origen", value = character.origin?.name ?: "Desconocido")
                DetailRow(label = "Ubicación actual", value = character.location?.name ?: "Desconocido")
                DetailRow(label = "Apariciones", value = "${character.episode.size} episodios")

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 13.sp)
        Text(
            text = value,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}