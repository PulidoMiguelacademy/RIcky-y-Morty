package com.example.movil_1 // Asegúrate de que sea tu paquete

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores de la vista (NeonGreen ya está definido en MainScreen.kt)
private val CardBorderBlue = Color(0xFF1E3A5F)
private val SearchBgColor = Color(0xFF0F1A2A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues) // Respeta el espacio de la barra de navegación
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

        // Barra de Búsqueda
        var searchQuery by remember { mutableStateOf("") }
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar episodios, personajes...", color = Color.Gray, fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White) },
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
            shape = RoundedCornerShape(24.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Chips (Filtros: Todos, Episodios, etc.)
        val categories = listOf("Todos", "Episodios", "Personajes", "Series")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories.size) { index ->
                val isSelected = index == 0 // Seleccionamos "Todos" por defecto para el diseño
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) NeonGreen else Color.Transparent)
                        .border(
                            1.dp,
                            if (isSelected) Color.Transparent else CardBorderBlue,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = categories[index],
                        color = if (isSelected) Color.Black else Color.LightGray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Sección Episodios Populares
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Episodios populares", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Ver todo", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Lista vertical de episodios
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EpisodeItem("El portal a la droga", "T1 - E3", "4.9")
            EpisodeItem("La historia del señor Nimbus", "T3 - E5", "4.7")
            EpisodeItem("Ricks en el espacio", "T4 - E8", "4.8")
            EpisodeItem("La familia Rick", "T2 - E4", "4.6")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Sección Personajes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Personajes", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Ver todo", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Lista horizontal de personajes
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(4) { index ->
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.Gray) // Aquí irán tus R.drawable de personajes
                        .border(2.dp, if(index % 2 == 0) NeonGreen else Color(0xFF9C27B0), CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun EpisodeItem(title: String, season: String, rating: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SearchBgColor)
            .border(1.dp, CardBorderBlue, RoundedCornerShape(16.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del episodio
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 70.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.DarkGray) // Placeholder de la imagen
        )
        Spacer(modifier = Modifier.width(12.dp))

        // Información del episodio
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = season, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "⭐ $rating", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}