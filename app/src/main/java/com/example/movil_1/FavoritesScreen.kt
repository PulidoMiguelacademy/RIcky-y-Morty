package com.example.movil_1

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
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

// Definición de colores basados en tu diseño (NeonGreen ya está definido globalmente en MainScreen.kt)
private val BackgroundColor = Color(0xFF06101D) // Fondo azul muy oscuro
private val CardBgColor = Color(0xFF0F1E33)     // Azul un poco más claro para las tarjetas
private val CardBorderBlue = Color(0xFF1B385B)  // Borde de las tarjetas
private val StarYellow = Color(0xFFFFC107)      // Amarillo para la estrella

@Composable
fun FavoritesScreen(
    navController: NavController,
    paddingValues: PaddingValues = PaddingValues(0.dp) // Para compatibilidad con el Scaffold/BottomNav
) {
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0 = Episodios, 1 = Series

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor) // Fondo de toda la pantalla
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Encabezado (Corazón + "Favoritos")
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favoritos",
                tint = NeonGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Favoritos",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Selector (Episodios / Series)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botón Episodios
            Button(
                onClick = { selectedSubTab = 0 },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSubTab == 0) NeonGreen else CardBgColor
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Episodios",
                    color = if (selectedSubTab == 0) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            // Botón Series
            Button(
                onClick = { selectedSubTab = 1 },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSubTab == 1) NeonGreen else CardBgColor
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Series",
                    color = if (selectedSubTab == 1) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 1. IMÁGENES DE LAS TARJETAS DE EPISODIOS
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            FavoriteItemCard("La historia del señor Nimbus", "T3 - E5", "4.7", R.drawable.historia)
            FavoriteItemCard("El portal a la droga", "T1 - E3", "4.9", R.drawable.portal)
            FavoriteItemCard("Ricks en el espacio", "T4 - E8", "4.8", R.drawable.quintamis)
            FavoriteItemCard("La familia Rick", "T2 - E4", "4.6", R.drawable.familia)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. ILUSTRACIÓN DEL ATARDECER (Banner Inferior)
        // SOLUCIÓN: Quitamos el Box y el .height(160.dp). Ahora la imagen dicta su propia altura.
        Image(
            painter = painterResource(id = R.drawable.cuartamision),
            contentDescription = "Atardecer Rick y Morty",
            contentScale = ContentScale.FillWidth, // Llenará el ancho de la pantalla y ajustará su altura sin dejar huecos
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(32.dp)) // Espacio al final para que no quede pegado a la barra de navegación
    }
}

@Composable
fun FavoriteItemCard(title: String, subtitle: String, rating: String, imageRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp) // Altura fija para que todas las tarjetas se vean iguales
            .clip(RoundedCornerShape(16.dp))
            .background(CardBgColor)
            .border(1.dp, CardBorderBlue, RoundedCornerShape(16.dp))
            .padding(end = 8.dp), // Padding solo a la derecha
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del episodio
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .width(90.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Textos del centro
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = subtitle, color = Color.LightGray, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = StarYellow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = rating, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Botón de opciones (tres puntos)
        IconButton(onClick = { /* Acción de opciones */ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Opciones",
                tint = Color.White
            )
        }
    }
}