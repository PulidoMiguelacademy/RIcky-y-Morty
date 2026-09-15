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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Colores de la vista (NeonGreen ya está definido en MainScreen.kt)
private val CardBorderBlue = Color(0xFF1E3A5F)
private val SearchBgColor = Color(0xFF0F1A2A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    navController: NavController,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
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

        // 1. IMÁGENES DE EPISODIOS
        // Modificamos la llamada para pasarle la imagen de prueba a cada episodio
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EpisodeItem("El portal a la droga", "T1 - E3", "4.9", R.drawable.portal) // <-- Cambia la imagen aquí
            EpisodeItem("La historia del señor Nimbus", "T3 - E5", "4.7", R.drawable.segundamision) // <-- Cambia la imagen aquí
            EpisodeItem("Ricks en el espacio", "T4 - E8", "4.8", R.drawable.quintamis) // <-- Cambia la imagen aquí
            EpisodeItem("La familia Rick", "T2 - E4", "4.6", R.drawable.familia) // <-- Cambia la imagen aquí
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

        // 2. IMÁGENES DE PERSONAJES
        // Creamos una lista con las imágenes de los personajes para que sea más fácil cambiarlas
        val personajes = listOf(
            R.drawable.rick, // <-- Cambia la imagen aquí (Personaje 1)
            R.drawable.sumer, // <-- Cambia la imagen aquí (Personaje 2)
            R.drawable.morty, // <-- Cambia la imagen aquí (Personaje 3)
            R.drawable.beth  // <-- Cambia la imagen aquí (Personaje 4)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(personajes.size) { index ->
                Image(
                    painter = painterResource(id = personajes[index]),
                    contentDescription = "Personaje",
                    contentScale = ContentScale.Crop, // Ajusta la imagen al círculo sin deformarla
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(2.dp, if(index % 2 == 0) NeonGreen else Color(0xFF9C27B0), CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// Actualizamos esta función para que reciba la imagen (imageRes)
@Composable
fun EpisodeItem(title: String, season: String, rating: String, imageRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SearchBgColor)
            .border(1.dp, CardBorderBlue, RoundedCornerShape(16.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del episodio (Reemplazamos el Box por Image)
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 100.dp, height = 70.dp)
                .clip(RoundedCornerShape(12.dp))
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