package com.example.lab4

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }
}

@Composable
fun Main() {
    val navController = rememberNavController()
    Column(Modifier.padding(8.dp)) {
        NavHost(navController, startDestination = NavRoutes.Home.route, modifier = Modifier.weight(1f)) {
            composable(NavRoutes.Home.route) { HomeScreen() }
            composable(NavRoutes.Lists.route) { ListScreen() }
            composable(NavRoutes.Images.route) { ImageScreen() }
            composable(NavRoutes.Camera.route) { CameraScreen(activity = LocalContext.current as ComponentActivity) }
        }
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun HomeScreen() {
    var text by rememberSaveable { mutableStateOf("") }
    val myName = "Вдовенко Никита Дмитриевич"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text.ifEmpty { "Информация отсутствует" },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { text = myName },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Показать ФИО")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { text = "" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Очистить")
        }
    }
}

data class Person(val name: String, val bank: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListScreen() {
    val people = listOf(
        Person("Вдовенко Никита", "Alfa-bank"),
        Person("Бадей Павел", "Alfa-bank"),
        Person("Целуйко Дарья", "Alfa-bank"),
        Person("Иванов Иван", "Приорбанк"),
        Person("Петров Петр", "Приорбанк"),
        Person("Сидоров Сидор", "Приорбанк"),
        Person("Смирнова Анна", "Беларусбанк"),
        Person("Кузнецов Олег", "Беларусбанк"),
        Person("Федорова Мария", "БПС-Сбербанк"),
        Person("Коваленко Сергей", "БПС-Сбербанк"),
        Person("Михайлова Оксана", "БПС-Сбербанк")
    )

    val groups = people.groupBy { it.bank }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        groups.forEach { (bank, clients) ->
            stickyHeader {
                Text(
                    text = bank,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(5.dp)
                        .fillMaxWidth()
                )
            }
            items(clients) { client ->
                Text(
                    text = client.name,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ImageScreen() {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.homka),
            contentDescription = "Rotating Image",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .graphicsLayer(rotationZ = animatedRotation)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                rotationAngle += 90f
            }
        ) {
            Text("Повернуть")
        }
    }
}

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("home")
    data object Lists : NavRoutes("lists")
    data object Images : NavRoutes("images")
    data object Camera : NavRoutes("camera")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        listOf(
            NavRoutes.Home to Icons.Default.Home,
            NavRoutes.Lists to Icons.AutoMirrored.Filled.List,
            NavRoutes.Images to Icons.Default.AccountCircle,
            NavRoutes.Camera to Icons.Default.Add
        ).forEach { (route, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(route.route) },
                selected = currentRoute == route.route,
                onClick = {
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun CameraScreen(activity: ComponentActivity) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
            } else {
                Toast.makeText(activity, "Изображение не выбрано", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(activity, "Разрешение на использование камеры предоставлено", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                activity,
                "Для создания снимка необходимо разрешение на использование камеры",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                galleryLauncher.launch("image/*")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Выбрать изображение из галереи")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Сделать снимок")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(selectedImageUri),
                contentDescription = "Выбранное изображение",
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.LightGray)
            )
        }
    }
}