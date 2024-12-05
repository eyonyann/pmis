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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab7.ui.theme.Lab7Theme
import com.example.lab7.ui_components.DrawerMenu
import com.example.lab7.ui_components.MainListItem
import com.example.lab7.ui_components.MainTopBar
import com.example.lab7.utils.DrawerEvents
import com.example.lab7.utils.IdArrayList
import com.example.lab7.utils.ListItem
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab7Theme {
                val topBarTitle = remember { mutableStateOf("Зеленый чай") }
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val mainList= remember { mutableStateOf(getListItemsByIndex(0,this))
                }
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            DrawerMenu(){ event ->
                                when(event) {
                                    is DrawerEvents.OnItemClick -> {
                                        topBarTitle.value=event.title
                                        mainList.value= getListItemsByIndex(
                                            event.index,this@MainActivity
                                        )
                                    }
                                }
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        }
                    },
                    content = {
                        Scaffold(
                            topBar = {
                                MainTopBar(title = topBarTitle.value, drawerState
                                = drawerState)
                            }
                        ) {innerPadding ->
                            LazyColumn(modifier=
                            Modifier.padding(innerPadding).fillMaxSize()){
                                items(mainList.value) {item ->
                                    MainListItem(item = item)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}


private fun getListItemsByIndex(index: Int, context: Context): List<ListItem>{
    val list = ArrayList<ListItem>()
    val arrayList = context.resources.getStringArray(IdArrayList.listId[index])
    arrayList.forEach { item ->
        val itemArray = item.split("|")
        list.add(
            ListItem(
                itemArray[0],
                itemArray[1],
                itemArray[2]
            )
        )
    }
    return list
}
