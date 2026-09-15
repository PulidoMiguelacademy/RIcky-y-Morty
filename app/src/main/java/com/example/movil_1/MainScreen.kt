package com.example.movil_1

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

val DarkBackground = Color(0xFF0D141C)
val NeonGreen = Color(0xFFC3F052)
val CardBackground = Color(0xFF1A222D)

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TopHeader()
        Spacer(modifier = Modifier.height(24.dp))
        GreetingSection()
        Spacer(modifier = Modifier.height(16.dp))
        HeroBanner()
        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader(title = "Tendencias", actionText = "Ver todo")
        Spacer(modifier = Modifier.height(12.dp))
        TrendingList()
        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader(title = "Colecciones", actionText = null)
        TrendingList()
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun TopHeader() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text("Rick and Morty", color = NeonGreen, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Row(verticalAlignment = Alignment.CenterVertically) {

            // 1. IMAGEN DEL AVATAR (Círculo superior derecho)
            Image(
                painter = painterResource(id = R.drawable.avatar1),
                contentDescription = "Perfil",
                contentScale = ContentScale.Crop, // Esto hace que la imagen llene el círculo sin deformarse
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = Color.White)
        }
    }
}

@Composable
fun GreetingSection() {
    Column { Text(text = "Hola, Morty", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold); Text(text = "¿Listo para otra aventura?", color = Color.LightGray, fontSize = 16.sp) }
}

@Composable
fun HeroBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, NeonGreen, RoundedCornerShape(16.dp))
    ) {
        // 2. IMAGEN DEL BANNER (El cuadro grande central)
        Image(
            painter = painterResource(id = R.drawable.perimeramision),
            contentDescription = "Nuevos episodios",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Este Box mantiene el gradiente oscuro para que el texto siga siendo legible
        Box(modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color.Black.copy(alpha = 0.9f), Color.Transparent), startX = 0f, endX = 800f)))
        Column(modifier = Modifier.fillMaxHeight().padding(16.dp).fillMaxWidth(0.6f), verticalArrangement = Arrangement.Center) {
            Text("Nuevos episodios\ndisponibles", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = NeonGreen), shape = RoundedCornerShape(24.dp)) { Text("Ver ahora", color = Color.Black, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
fun SectionHeader(title: String, actionText: String?) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        if (actionText != null) Text(text = actionText, color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun TrendingList() {
    // 3. IMÁGENES DE LAS TARJETAS (Listas horizontales)
    // Creé una pequeña lista para que puedas poner nombres e imágenes diferentes a cada tarjeta.
    val episodios = listOf(
        Pair("El show de Rick", R.drawable.segundamision), // <-- Cambia la imagen aquí
        Pair("Aventura espacial", R.drawable.terceramision), // <-- Cambia la imagen aquí
        Pair("Planeta Squanch", R.drawable.cuartamision), // <-- Cambia la imagen aquí
        Pair("Ciudadela", R.drawable.quintamis), // <-- Cambia la imagen aquí
        Pair("Morty malvado", R.drawable.sextamiso) // <-- Cambia la imagen aquí
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(episodios.size) { index ->
            val episodio = episodios[index]

            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.width(140.dp).height(180.dp).border(1.dp, Color(0xFF2A3441), RoundedCornerShape(16.dp))
            ) {
                Column {
                    // Imagen de la tarjeta reemplazando el Box gris
                    Image(
                        painter = painterResource(id = episodio.second), // Toma la imagen de la lista de arriba
                        contentDescription = episodio.first,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )

                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(episodio.first, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                        Spacer(modifier = Modifier.weight(1f))
                        Text("⭐ 4.8", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}