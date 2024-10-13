package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab2.ui.theme.Lab2Theme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedScreen by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedScreen) { selectedScreen = it }
        }
    ) { paddingValues ->
        when (selectedScreen) {
            0 -> HomeScreen(paddingValues)
            1 -> ListScreen(paddingValues)
        }
    }
}

@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    var text by rememberSaveable { mutableStateOf("") }
    val myName = "Вдовенко Никита Дмитриевич"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (text.isEmpty()) "Информация отсутствует" else text,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge
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

@Composable
fun ListScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Lists Page", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun BottomNavigationBar(selectedScreen: Int, onScreenSelected: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedScreen == 0,
            onClick = { onScreenSelected(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Lists") },
            label = { Text("Lists") },
            selected = selectedScreen == 1,
            onClick = { onScreenSelected(1) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lab2Theme {
        MainScreen()
    }
}
