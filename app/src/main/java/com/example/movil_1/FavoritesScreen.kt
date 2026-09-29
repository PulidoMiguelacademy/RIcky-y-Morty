package com.example.movil_1

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController

// Definición de colores basados en tu diseño (NeonGreen ya está definido globalmente en MainScreen.kt)
private val BackgroundColor = Color(0xFF06101D) // Fondo azul muy oscuro
private val CardBgColor = Color(0xFF0F1E33)     // Azul un poco más claro para las tarjetas
private val CardBorderBlue = Color(0xFF1B385B)  // Borde de las tarjetas
private val StarYellow = Color(0xFFFFC107)      // Amarillo para la estrella

@Composable
fun FavoritesScreen(
    navController: NavController,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0 = Episodios, 1 = Series

    // Lista mutable de favoritos para permitir eliminaciones
    var favoriteEpisodes by remember {
        mutableStateOf(
            listOf(
                Triple("La historia del señor Nimbus", "T3 - E5 • 4.7", R.drawable.historia),
                Triple("El portal a la droga", "T1 - E3 • 4.9", R.drawable.portal),
                Triple("Ricks en el espacio", "T4 - E8 • 4.8", R.drawable.quintamis),
                Triple("La familia Rick", "T2 - E4 • 4.6", R.drawable.familia)
            )
        )
    }

    val favoriteSeries = listOf(
        Triple("Temporada 1 • El inicio interdimensional", "11 Episodios • 4.9", R.drawable.perimeramision),
        Triple("Temporada 2 • Realidades alternas", "10 Episodios • 4.8", R.drawable.segundamision),
        Triple("Temporada 3 • La ciudadela de Ricks", "10 Episodios • 4.9", R.drawable.terceramision),
        Triple("Temporada 4 • Aventuras cósmicas", "10 Episodios • 4.7", R.drawable.cuartamision)
    )

    var selectedDetailItem by remember { mutableStateOf<Triple<String, String, Int>?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
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
                    text = "Episodios (${favoriteEpisodes.size})",
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
                    text = "Series (${favoriteSeries.size})",
                    color = if (selectedSubTab == 1) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Contenido según la pestaña seleccionada
        if (selectedSubTab == 0) {
            if (favoriteEpisodes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes episodios en favoritos.\n¡Explora y añade los que más te gusten!",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    favoriteEpisodes.forEach { (title, sub, img) ->
                        FavoriteItemCard(
                            title = title,
                            subtitle = sub.substringBefore(" • "),
                            rating = sub.substringAfter(" • "),
                            imageRes = img,
                            onClick = {
                                selectedDetailItem = Triple(title, sub, img)
                            },
                            onDelete = {
                                favoriteEpisodes = favoriteEpisodes.filterNot { it.first == title }
                                Toast.makeText(context, "Eliminado de favoritos: $title", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                favoriteSeries.forEach { (title, sub, img) ->
                    FavoriteItemCard(
                        title = title,
                        subtitle = sub.substringBefore(" • "),
                        rating = sub.substringAfter(" • "),
                        imageRes = img,
                        onClick = {
                            selectedDetailItem = Triple(title, sub, img)
                        },
                        onDelete = {
                            Toast.makeText(context, "Información de temporada guardada", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. ILUSTRACIÓN DEL ATARDECER (Banner Inferior)
        Image(
            painter = painterResource(id = R.drawable.cuartamision),
            contentDescription = "Atardecer Rick y Morty",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    navController.navigate("explore")
                }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }

    // Modal de Detalle al tocar un favorito
    selectedDetailItem?.let { (title, subtitle, img) ->
        Dialog(onDismissRequest = { selectedDetailItem = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBgColor),
                border = BorderStroke(1.5.dp, NeonGreen),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { selectedDetailItem = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.LightGray)
                        }
                    }

                    Image(
                        painter = painterResource(id = img),
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = subtitle,
                        color = NeonGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            selectedDetailItem = null
                            Toast.makeText(context, "Reproduciendo $title...", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver ahora", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteItemCard(
    title: String,
    subtitle: String,
    rating: String,
    imageRes: Int,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardBgColor)
            .border(1.dp, CardBorderBlue, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(end = 8.dp),
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
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = subtitle, color = Color.LightGray, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = StarYellow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = rating, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Tocar para ver", color = Color.Gray, fontSize = 10.sp)
        }

        // Botón de opciones (tres puntos con menú desplegable)
        Box {
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Opciones",
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.background(CardBgColor).border(1.dp, CardBorderBlue)
            ) {
                DropdownMenuItem(
                    text = { Text("Ver detalles", color = Color.White) },
                    onClick = {
                        showMenu = false
                        onClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar de favoritos", color = Color(0xFFFF5252)) },
                    onClick = {
                        showMenu = false
                        onDelete()
                    }
                )
            }
        }
    }
}