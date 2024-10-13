package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab1.ui.theme.Lab1Theme
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab1Theme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    var text by rememberSaveable { mutableStateOf("") }

    val myName = "Вдовенко Никита Дмитриевич"

    Column(
        modifier = Modifier
            .fillMaxSize()
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lab1Theme {
        MyApp()
    }
}
