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
    var selectedEpisode by remember { mutableStateOf<Pair<String, Int>?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TopHeader(onAvatarClick = { navController.navigate("perfil") })
        Spacer(modifier = Modifier.height(24.dp))
        GreetingSection()
        Spacer(modifier = Modifier.height(16.dp))
        HeroBanner(onVerAhoraClick = { navController.navigate("explore") })
        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader(
            title = "Tendencias",
            actionText = "Ver todo",
            onActionClick = { navController.navigate("explore") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TrendingList(onEpisodeClick = { episode -> selectedEpisode = episode })
        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader(
            title = "Colecciones",
            actionText = "Ver todo",
            onActionClick = { navController.navigate("favorites") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TrendingList(onEpisodeClick = { episode -> selectedEpisode = episode })
        Spacer(modifier = Modifier.height(24.dp))
    }

    // Modal de Detalle al tocar cualquier tarjeta de la lista
    selectedEpisode?.let { episode ->
        MainEpisodeDetailDialog(
            episode = episode,
            onDismiss = { selectedEpisode = null },
            onExplore = {
                selectedEpisode = null
                navController.navigate("explore")
            }
        )
    }
}

@Composable
fun TopHeader(onAvatarClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Rick and Morty", color = NeonGreen, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 1. IMAGEN DEL AVATAR (Círculo superior derecho con clic hacia Perfil)
            Image(
                painter = painterResource(id = R.drawable.avatar1),
                contentDescription = "Ir al Perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, NeonGreen, CircleShape)
                    .clickable { onAvatarClick() }
            )

            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                tint = Color.White
            )
        }
    }
}

@Composable
fun GreetingSection() {
    Column {
        Text(text = "Hola, Morty", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(text = "¿Listo para otra aventura?", color = Color.LightGray, fontSize = 16.sp)
    }
}

@Composable
fun HeroBanner(onVerAhoraClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, NeonGreen, RoundedCornerShape(16.dp))
            .clickable { onVerAhoraClick() }
    ) {
        // 2. IMAGEN DEL BANNER (El cuadro grande central)
        Image(
            painter = painterResource(id = R.drawable.perimeramision),
            contentDescription = "Nuevos episodios",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Este Box mantiene el gradiente oscuro para que el texto siga siendo legible
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(Color.Black.copy(alpha = 0.9f), Color.Transparent), startX = 0f, endX = 800f))
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .fillMaxWidth(0.6f),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Nuevos episodios\ndisponibles", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onVerAhoraClick,
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Ver ahora", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String?,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        if (actionText != null) {
            Text(
                text = actionText,
                color = NeonGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onActionClick?.invoke() }
                    .padding(vertical = 4.dp, horizontal = 6.dp)
            )
        }
    }
}

@Composable
fun TrendingList(onEpisodeClick: (Pair<String, Int>) -> Unit = {}) {
    val episodios = listOf(
        Pair("El show de Rick", R.drawable.segundamision),
        Pair("Aventura espacial", R.drawable.terceramision),
        Pair("Planeta Squanch", R.drawable.cuartamision),
        Pair("Ciudadela", R.drawable.quintamis),
        Pair("Morty malvado", R.drawable.sextamiso)
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(episodios.size) { index ->
            val episodio = episodios[index]

            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(140.dp)
                    .height(180.dp)
                    .border(1.dp, Color(0xFF2A3441), RoundedCornerShape(16.dp))
                    .clickable { onEpisodeClick(episodio) }
            ) {
                Column {
                    Image(
                        painter = painterResource(id = episodio.second),
                        contentDescription = episodio.first,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )

                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            episodio.first,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⭐ 4.8", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Tocar para ver", color = Color.Gray, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainEpisodeDetailDialog(
    episode: Pair<String, Int>,
    onDismiss: () -> Unit,
    onExplore: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            border = BorderStroke(1.5.dp, NeonGreen),
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
                Image(
                    painter = painterResource(id = episode.second),
                    contentDescription = episode.first,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = episode.first,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "⭐ Calificación: 4.8 / 5.0 • Temporada Destacada",
                    color = NeonGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Acompaña a Rick y Morty en esta travesía intergaláctica llena de peligros, criaturas extrañas y portales multiversales.",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onExplore,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Explorar en el catálogo", color = Color.Black, fontWeight = FontWeight.Bold)
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