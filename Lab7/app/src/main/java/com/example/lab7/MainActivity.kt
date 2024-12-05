package com.example.lab7

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab7.ui.theme.Lab7Theme
import com.example.lab7.ui_components.InfoScreen
import com.example.lab7.ui_components.MainScreen
import com.example.lab7.utils.ItemSaver
import com.example.lab7.utils.ListItem
import com.example.lab7.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var item= rememberSaveable(stateSaver = ItemSaver) {
                mutableStateOf( ListItem(id = 0, title = "", imageName = "",
                    htmlName = "", isfav = false, category = ""))
            }

            Lab7Theme {
                NavHost(
                    navController = navController,
                    startDestination = Routes.MAIN_SCREEN.route
                ) {
                    composable(Routes.MAIN_SCREEN.route) {
                        MainScreen() {
                            listItem ->item.value =ListItem(listItem.id,
                                                            listItem.title,
                                                            listItem.imageName,
                                                            listItem.htmlName,
                                                            listItem.isfav,
                                                            listItem.category)
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


