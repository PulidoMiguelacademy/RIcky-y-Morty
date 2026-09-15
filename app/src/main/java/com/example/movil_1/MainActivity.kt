package com.example.movil_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF0F111A)
            ) {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Observamos la ruta actual para saber si debemos mostrar la barra inferior
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // El Scaffold ahora envuelve toda la navegación
    Scaffold(
        containerColor = Color(0xFF0F111A),
        bottomBar = {
            // Solo mostramos el menú de navegación si NO estamos en la pantalla splash
            if (currentRoute != "splash") {
                CustomBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Esta configuración evita que se abran muchas pantallas iguales al darle click varias veces
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        // Aquí adentro viven todas tus pantallas
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding) // Evita que el menú tape el contenido
        ) {
            // 1. Splash Screen
            composable("splash") {
                LaunchedEffect(key1 = true) {
                    kotlinx.coroutines.delay(2500.milliseconds)
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
                SplashScreen()
            }

            // 2. Inicio
            composable("main") {
                MainScreen(navController = navController)
            }

            // 3. Explorar
            composable("explore") {
                // Se añadieron las llamadas con navController y paddingValues que configuraste en ExploreScreen
                ExploreScreen(navController = navController, paddingValues = innerPadding)
            }

            // 4. Favoritos
            composable("favorites") {
                FavoritesScreen(navController = navController, paddingValues = innerPadding)
            }

            // 5. Perfil
            composable("perfil") {
                PerfilScreen(navController = navController)
            }
        }
    }
}

@Composable
fun CustomBottomNavigation(currentRoute: String?, onNavigate: (String) -> Unit) {
    val neonGreenColor = Color(0xFFC3F052)
    // Lista de pantallas: (Nombre, Icono, Ruta en el NavHost)
    val items = listOf(
        Triple("Inicio", Icons.Default.Home, "main"),
        Triple("Explorar", Icons.Default.Search, "explore"),
        Triple("Favoritos", Icons.Default.Favorite, "favorites"),
        Triple("Perfil", Icons.Default.Person, "perfil")
    )

    NavigationBar(
        containerColor = Color(0xFF0A0F14),
        contentColor = Color.Gray
    ) {
        items.forEach { (label, icon, route) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 10.sp) },
                selected = currentRoute == route, // Se pinta de verde si la ruta coincide
                onClick = { onNavigate(route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = neonGreenColor,
                    selectedTextColor = neonGreenColor,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun SplashScreen() {
    val portalGreenColor = Color(0xFF97CE4C)
    val textLightColor = Color(0xFFE0E0E0)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_portal_space),
            contentDescription = "Fondo del espacio",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.4f
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Rick and Morty Logo",
                modifier = Modifier.size(150.dp).padding(bottom = 24.dp),
                contentScale = ContentScale.Fit
            )
            Text(text = "CARGANDO...", color = portalGreenColor, fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(color = portalGreenColor, trackColor = Color.White.copy(alpha = 0.1f), strokeWidth = 4.dp, modifier = Modifier.size(45.dp))
        }

        Text(
            text = "v1.0.1",
            color = textLightColor.copy(alpha = 0.6f),
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp)
        )
    }
}