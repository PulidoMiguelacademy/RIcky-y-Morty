package com.example.movil_1

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

// Colores basados en tu diseño (NeonGreen ya está definido de forma global en MainScreen.kt)
private val BackgroundBlue = Color(0xFF040A15)
private val CardBg = Color(0xFF0B192C)
private val CardBorder = Color(0xFF1B365D)
private val CyanBorder = Color(0xFF00E5FF)

@Composable
fun PerfilScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlue)
    ) {
        // Fondo del espacio (Aprovechamos la imagen que ya tienes del splash)
        Image(
            painter = painterResource(id = R.drawable.bg_portal_space),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3f // Le bajamos la opacidad para que los textos resalten
        )

        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Botón de retroceso (verde)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(NeonGreen)
                    .clickable { navController.popBackStack() }, // Acción para volver atrás
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Header Perfil (Avatar + Info)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar circular con borde cian
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                        .border(3.dp, CyanBorder, CircleShape)
                ) {
                    // Descomenta esto cuando tengas la foto de Morty
                    /*Image(
                        painter = painterResource(id = R.drawable.tu_imagen_de_morty),
                        contentDescription = "Morty Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )*/
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información del usuario y barra XP
                Column {
                    Text(
                        text = "Morty Smith",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Nivel 3 • Fan del multiverso",
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Barra de progreso XP
                    LinearProgressIndicator(
                        progress = { 0.6f }, // Representa el 60% (120 de 200)
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NeonGreen,
                        trackColor = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "120 / 200 XP",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjetas de Estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatBox(modifier = Modifier.weight(1f), number = "12", label = "Episodios vistos")
                StatBox(modifier = Modifier.weight(1f), number = "5", label = "Favoritos")
                StatBox(modifier = Modifier.weight(1f), number = "3", label = "Logros")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menú de opciones inferior
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBg),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // He usado iconos estándar de Android para que compile a la primera
                    MenuOptionItem(icon = Icons.Default.DateRange, title = "Mi actividad")
                    MenuOptionItem(icon = Icons.Default.KeyboardArrowDown, title = "Descargas")
                    MenuOptionItem(icon = Icons.Default.Settings, title = "Configuración")
                    MenuOptionItem(icon = Icons.Default.Info, title = "Ayuda")
                    MenuOptionItem(icon = Icons.Default.ExitToApp, title = "Cerrar sesión", showDivider = false)
                }
            }

            // Espacio al fondo para que no se pise con la imagen de Rick y el menú inferior
            Spacer(modifier = Modifier.height(180.dp))
        }

        // Imagen inferior (Portal con Rick asomándose)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .align(Alignment.BottomCenter)
        ) {
            // Descomenta esto cuando tengas la imagen del portal inferior
            /*Image(
                painter = painterResource(id = R.drawable.rick_portal_inferior),
                contentDescription = "Rick en portal",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize()
            )*/
        }
    }
}

// Componente para las 3 cajitas de arriba (12 Episodios, 5 Favoritos, etc.)
@Composable
fun StatBox(modifier: Modifier = Modifier, number: String, label: String) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CardBg)
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = number, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, color = Color.LightGray, fontSize = 11.sp)
        }
    }
}

// Componente para cada fila del menú (Mi actividad, Descargas, etc.)
@Composable
fun MenuOptionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, showDivider: Boolean = true) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Acción del menú */ }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Ir", tint = Color.Gray, modifier = Modifier.size(20.dp))
        }

        // Línea separadora entre opciones
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(1.dp)
                    .background(CardBorder)
            )
        }
    }
}