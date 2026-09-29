package com.example.movil_1

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController

// Colores basados en el diseño de la app
private val BackgroundBlue = Color(0xFF040A15)
private val CardBg = Color(0xFF0B192C)
private val CardBorder = Color(0xFF1B365D)
private val CyanBorder = Color(0xFF00E5FF)
private val DialogBg = Color(0xFF0A1220)

@Composable
fun PerfilScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("rick_morty_user_prefs", Context.MODE_PRIVATE) }

    // Estados persistentes del perfil
    var userName by remember {
        mutableStateOf(prefs.getString("user_name", "Morty Smith") ?: "Morty Smith")
    }
    var userRank by remember {
        mutableStateOf(prefs.getString("user_rank", "Nivel 3 • Fan del multiverso") ?: "Nivel 3 • Fan del multiverso")
    }
    var userAvatar by remember {
        mutableStateOf(prefs.getString("user_avatar", "morty") ?: "morty")
    }

    // Estados de configuración persistentes
    var darkModeEnabled by remember {
        mutableStateOf(prefs.getBoolean("dark_mode", true))
    }
    var notificationsEnabled by remember {
        mutableStateOf(prefs.getBoolean("notifications", true))
    }
    var wifiOnlyEnabled by remember {
        mutableStateOf(prefs.getBoolean("wifi_only", true))
    }
    var hdQualityEnabled by remember {
        mutableStateOf(prefs.getBoolean("hd_quality", true))
    }

    // Estados para controlar qué modal está abierto
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showEpisodiosVistosDialog by remember { mutableStateOf(false) }
    var showLogrosDialog by remember { mutableStateOf(false) }
    var showActividadDialog by remember { mutableStateOf(false) }
    var showDescargasDialog by remember { mutableStateOf(false) }
    var showConfiguracionDialog by remember { mutableStateOf(false) }
    var showAyudaDialog by remember { mutableStateOf(false) }
    var showCerrarSesionDialog by remember { mutableStateOf(false) }

    // Obtener el drawable del avatar actual
    val currentAvatarRes = when (userAvatar) {
        "rick" -> R.drawable.rick
        "sumer" -> R.drawable.sumer
        "beth" -> R.drawable.beth
        "avatar1" -> R.drawable.avatar1
        else -> R.drawable.morty
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlue)
    ) {
        // Fondo del espacio con transparencia
        Image(
            painter = painterResource(id = R.drawable.bg_portal_space),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3f
        )

        // Contenido scrolleable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Barra superior con botón volver y título
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(NeonGreen)
                        .clickable {
                            if (!navController.popBackStack()) {
                                navController.navigate("main")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
                }

                Text(
                    text = "Mi Perfil",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Botón editar perfil en la esquina superior
                IconButton(onClick = { showEditProfileDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Perfil",
                        tint = NeonGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Header Perfil (Avatar + Info + Botón editar)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CardBg.copy(alpha = 0.8f))
                    .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                // Avatar con insignia de edición al tocarlo
                Box(
                    modifier = Modifier.clickable { showEditProfileDialog = true },
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Image(
                        painter = painterResource(id = currentAvatarRes),
                        contentDescription = "Avatar de Usuario",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(85.dp)
                            .clip(CircleShape)
                            .border(3.dp, CyanBorder, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(NeonGreen)
                            .border(1.5.dp, BackgroundBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Cambiar avatar",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información del usuario y barra XP
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userName,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userRank,
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Barra de progreso XP
                    LinearProgressIndicator(
                        progress = { 0.6f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NeonGreen,
                        trackColor = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "120 / 200 XP",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Nivel 3",
                            color = NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjetas de Estadísticas INTERACTIVAS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InteractiveStatBox(
                    modifier = Modifier.weight(1f),
                    number = "12",
                    label = "Episodios vistos",
                    onClick = { showEpisodiosVistosDialog = true }
                )
                InteractiveStatBox(
                    modifier = Modifier.weight(1f),
                    number = "4",
                    label = "Favoritos",
                    onClick = { navController.navigate("favorites") }
                )
                InteractiveStatBox(
                    modifier = Modifier.weight(1f),
                    number = "3",
                    label = "Logros",
                    onClick = { showLogrosDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menú de opciones INTERACTIVO
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBg),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    MenuOptionItem(
                        icon = Icons.Default.DateRange,
                        title = "Mi actividad",
                        subtitle = "Historial de reproducciones",
                        onClick = { showActividadDialog = true }
                    )
                    MenuOptionItem(
                        icon = Icons.Default.Download,
                        title = "Descargas",
                        subtitle = "4 episodios sin conexión",
                        onClick = { showDescargasDialog = true }
                    )
                    MenuOptionItem(
                        icon = Icons.Default.Settings,
                        title = "Configuración",
                        subtitle = "Modo oscuro, notificaciones y calidad",
                        onClick = { showConfiguracionDialog = true }
                    )
                    MenuOptionItem(
                        icon = Icons.Default.Info,
                        title = "Ayuda y soporte",
                        subtitle = "Preguntas frecuentes y versión",
                        onClick = { showAyudaDialog = true }
                    )
                    MenuOptionItem(
                        icon = Icons.Default.ExitToApp,
                        title = "Cerrar sesión",
                        subtitle = "Restablecer datos de sesión",
                        showDivider = false,
                        titleColor = Color(0xFFFF5252),
                        onClick = { showCerrarSesionDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // ==========================================
    // DIÁLOGOS Y MODALES INTERACTIVOS
    // ==========================================

    // 1. DIÁLOGO EDITAR PERFIL Y AVATAR
    if (showEditProfileDialog) {
        var tempName by remember { mutableStateOf(userName) }
        var tempAvatar by remember { mutableStateOf(userAvatar) }

        val avatarOptions = listOf(
            Triple("morty", "Morty", R.drawable.morty),
            Triple("rick", "Rick", R.drawable.rick),
            Triple("sumer", "Summer", R.drawable.sumer),
            Triple("beth", "Beth", R.drawable.beth),
            Triple("avatar1", "Viajero", R.drawable.avatar1)
        )

        Dialog(onDismissRequest = { showEditProfileDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DialogBg),
                border = BorderStroke(1.5.dp, NeonGreen),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Editar Perfil",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Elige tu avatar:",
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Selector de Avatar horizontal
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(avatarOptions) { (key, label, res) ->
                            val isSelected = tempAvatar == key
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { tempAvatar = key }
                            ) {
                                Image(
                                    painter = painterResource(id = res),
                                    contentDescription = label,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) NeonGreen else Color.Gray,
                                            shape = CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = label,
                                    color = if (isSelected) NeonGreen else Color.Gray,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Nombre de usuario", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showEditProfileDialog = false },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar", color = Color.White)
                        }

                        Button(
                            onClick = {
                                val cleanName = tempName.trim().ifEmpty { "Morty Smith" }
                                userName = cleanName
                                userAvatar = tempAvatar
                                prefs.edit()
                                    .putString("user_name", cleanName)
                                    .putString("user_avatar", tempAvatar)
                                    .apply()
                                Toast.makeText(context, "¡Perfil actualizado con éxito!", Toast.LENGTH_SHORT).show()
                                showEditProfileDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Guardar", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // 2. DIÁLOGO EPISODIOS VISTOS
    if (showEpisodiosVistosDialog) {
        val vistos = listOf(
            "T1 - E1 • Piloto original",
            "T1 - E3 • El portal a la droga",
            "T2 - E4 • Total Rickall (Familia Rick)",
            "T3 - E1 • La redención de Rick",
            "T3 - E3 • Pickle Rick",
            "T3 - E5 • La historia del señor Nimbus",
            "T4 - E8 • El episodio del tanque de ácido",
            "T5 - E10 • Rickmuray Jack"
        )
        Dialog(onDismissRequest = { showEpisodiosVistosDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DialogBg),
                border = BorderStroke(1.5.dp, NeonGreen),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📺 Episodios vistos (12)", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { showEpisodiosVistosDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier
                            .heightIn(max = 280.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        vistos.forEach { ep ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CardBg)
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(ep, color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showEpisodiosVistosDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Aceptar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 3. DIÁLOGO LOGROS
    if (showLogrosDialog) {
        val logros = listOf(
            Triple("🏆 Viajero Multiversal", "Has viajado a más de 5 dimensiones distintas", true),
            Triple("🧪 Científico Loco", "Viste 10 episodios en una sola sesión", true),
            Triple("🛸 Amigo de Squanchy", "Agregaste 5 episodios a favoritos", true),
            Triple("🌌 Maestro de la Ciudadela", "Completa la temporada 5 (En progreso: 70%)", false)
        )
        Dialog(onDismissRequest = { showLogrosDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DialogBg),
                border = BorderStroke(1.5.dp, NeonGreen),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🎖️ Logros (3/4)", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { showLogrosDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier.heightIn(max = 280.dp).verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        logros.forEach { (titulo, desc, completado) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (completado) CardBg else CardBg.copy(alpha = 0.4f))
                                    .border(1.dp, if (completado) NeonGreen.copy(alpha = 0.5f) else Color.DarkGray, RoundedCornerShape(10.dp))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(titulo, color = if (completado) NeonGreen else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(desc, color = Color.LightGray, fontSize = 12.sp)
                                }
                                Icon(
                                    imageVector = if (completado) Icons.Default.Check else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (completado) NeonGreen else Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showLogrosDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Genial", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 4. DIÁLOGO MI ACTIVIDAD
    if (showActividadDialog) {
        val actividad = listOf(
            "Reprodujiste: 'Ricks en el espacio' • Hoy 18:24",
            "Buscaste: 'Rick Sanchez C-137' • Ayer 21:05",
            "Añadiste a favoritos: 'El portal a la droga' • Ayer 20:50",
            "Subiste a Nivel 3 de multiverso • Hace 2 días"
        )
        Dialog(onDismissRequest = { showActividadDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DialogBg),
                border = BorderStroke(1.5.dp, NeonGreen),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📊 Mi Actividad Reciente", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { showActividadDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        actividad.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CardBg)
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.History, contentDescription = null, tint = CyanBorder, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(item, color = Color.LightGray, fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showActividadDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 5. DIÁLOGO DESCARGAS
    if (showDescargasDialog) {
        Dialog(onDismissRequest = { showDescargasDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DialogBg),
                border = BorderStroke(1.5.dp, NeonGreen),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("📥 Episodios Descargados", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Almacenamiento: 1.2 GB usados de 32 GB", color = NeonGreen, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    listOf(
                        "T1 - E3 • El portal a la droga (320 MB)",
                        "T3 - E5 • Señor Nimbus (290 MB)",
                        "T4 - E8 • Ricks en el espacio (310 MB)",
                        "T2 - E4 • Total Rickall (280 MB)"
                    ).forEach { dl ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CardBg)
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(dl, color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showDescargasDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Entendido", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 6. DIÁLOGO CONFIGURACIÓN CON SWITCHES REALES
    if (showConfiguracionDialog) {
        Dialog(onDismissRequest = { showConfiguracionDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DialogBg),
                border = BorderStroke(1.5.dp, NeonGreen),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚙️ Configuración", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { showConfiguracionDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    SettingSwitchRow(
                        title = "Modo Oscuro",
                        subtitle = "Tema interdimensional",
                        checked = darkModeEnabled,
                        onCheckedChange = {
                            darkModeEnabled = it
                            prefs.edit().putBoolean("dark_mode", it).apply()
                        }
                    )

                    SettingSwitchRow(
                        title = "Notificaciones",
                        subtitle = "Avisar sobre nuevos episodios",
                        checked = notificationsEnabled,
                        onCheckedChange = {
                            notificationsEnabled = it
                            prefs.edit().putBoolean("notifications", it).apply()
                        }
                    )

                    SettingSwitchRow(
                        title = "Solo Wi-Fi",
                        subtitle = "Ahorrar datos móviles",
                        checked = wifiOnlyEnabled,
                        onCheckedChange = {
                            wifiOnlyEnabled = it
                            prefs.edit().putBoolean("wifi_only", it).apply()
                        }
                    )

                    SettingSwitchRow(
                        title = "Calidad HD (1080p)",
                        subtitle = "Máxima fidelidad gráfica",
                        checked = hdQualityEnabled,
                        onCheckedChange = {
                            hdQualityEnabled = it
                            prefs.edit().putBoolean("hd_quality", it).apply()
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showConfiguracionDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar Preferencias", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 7. DIÁLOGO AYUDA Y SOPORTE
    if (showAyudaDialog) {
        Dialog(onDismissRequest = { showAyudaDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DialogBg),
                border = BorderStroke(1.5.dp, NeonGreen),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🛸 Ayuda & Soporte", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Rick and Morty Explorer v1.0.1\nDesarrollada para explorar episodios, personajes y dimensiones del multiverso.",
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "• API: rickandmortyapi.com\n• Soporte: soporte@citadelofrics.com\n• Hecho con Jetpack Compose y Kotlin",
                        color = NeonGreen,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { showAyudaDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Aceptar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // 8. DIÁLOGO CERRAR SESIÓN
    if (showCerrarSesionDialog) {
        Dialog(onDismissRequest = { showCerrarSesionDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DialogBg),
                border = BorderStroke(1.5.dp, Color(0xFFFF5252)),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("¿Cerrar sesión?", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "¿Deseas restablecer tu perfil de viajero interdimensional a los valores por defecto?",
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showCerrarSesionDialog = false },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar", color = Color.White)
                        }

                        Button(
                            onClick = {
                                userName = "Morty Smith"
                                userRank = "Nivel 3 • Fan del multiverso"
                                userAvatar = "morty"
                                prefs.edit().clear().apply()
                                Toast.makeText(context, "Sesión cerrada. Perfil restablecido.", Toast.LENGTH_SHORT).show()
                                showCerrarSesionDialog = false
                                navController.navigate("main")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cerrar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// Componente para las 3 cajitas interactivas (Episodios, Favoritos, Logros)
@Composable
fun InteractiveStatBox(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CardBg)
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = number, color = NeonGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, color = Color.LightGray, fontSize = 11.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Tocar para ver", color = Color.Gray, fontSize = 9.sp)
        }
    }
}

// Componente para cada fila del menú
@Composable
fun MenuOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    showDivider: Boolean = true,
    titleColor: Color = Color.White,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = titleColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = titleColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                if (subtitle != null) {
                    Text(text = subtitle, color = Color.Gray, fontSize = 12.sp)
                }
            }
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Ir", tint = Color.Gray, modifier = Modifier.size(20.dp))
        }

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

// Fila de interruptor para la configuración
@Composable
fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Color.Gray, fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = NeonGreen,
                uncheckedThumbColor = Color.LightGray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}