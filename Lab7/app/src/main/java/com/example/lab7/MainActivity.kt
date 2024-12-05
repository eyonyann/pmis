package com.example.lab7

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab7.ui.theme.Lab7Theme
import com.example.lab7.ui_components.DrawerMenu
import com.example.lab7.ui_components.InfoScreen
import com.example.lab7.ui_components.MainListItem
import com.example.lab7.ui_components.MainScreen
import com.example.lab7.ui_components.MainTopBar
import com.example.lab7.utils.DrawerEvents
import com.example.lab7.utils.IdArrayList
import com.example.lab7.utils.ItemSaver
import com.example.lab7.utils.ListItem
import com.example.lab7.utils.Routes
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var item = rememberSaveable(stateSaver = ItemSaver) {
                mutableStateOf(ListItem("", "",""))}

            Lab7Theme {
                NavHost(
                    navController = navController,
                    startDestination = Routes.MAIN_SCREEN.route
                ) {
                    composable(Routes.MAIN_SCREEN.route) {
                        MainScreen(context = this@MainActivity) { listItem ->
                            item.value=ListItem(listItem.title,listItem.imageName,listItem.htmlName)
                            navController.navigate(Routes.INFO_SCREEN.route)
                        }
                    }
                    composable(Routes.INFO_SCREEN.route) {
                        // вызов Infoscreen
                        InfoScreen(item = item.value!!)
                    }
                }

            }
        }
    }
}


