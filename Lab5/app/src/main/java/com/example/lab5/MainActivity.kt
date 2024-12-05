package com.example.lab5

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.teaencyclopedia.ui.theme.TeaEncyclopediaTheme
import kotlinx.coroutines.launch
import java.util.Locale

data class TeaItem(val name: String, val imageName: String, val descriptionFileName: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeaEncyclopediaTheme {
                TeaApp(this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaApp(context: Context) {
    val categories = context.resources.getStringArray(R.array.categories)
    val teaItems = remember { mutableStateListOf<TeaItem>() }
    val currentCategory = remember { mutableStateOf(categories[0]) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()

    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(
                categories = categories,
                onCategorySelected = { category ->
                    currentCategory.value = category
                    teaItems.clear()
                    teaItems.addAll(getTeaItemsByCategory(category, context))
                    scope.launch { drawerState.close() }
                }
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentCategory.value, fontSize = 18.sp) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF8BC34A))
                )
            },
            content = { innerPadding ->
                NavHost(navController = navController, startDestination = "teaList") {
                    composable("teaList") {
                        TeaListScreen(teaItems, context, navController, innerPadding)  // Pass innerPadding here
                    }
                    composable("teaDescription/{teaName}") { backStackEntry ->
                        val teaName = backStackEntry.arguments?.getString("teaName") ?: ""
                        TeaDescriptionScreen(teaName, context)
                    }
                }
            }
        )
    }
}

@Composable
fun TeaListScreen(teaItems: List<TeaItem>, context: Context, navController: NavController, innerPadding: PaddingValues) {
    LazyColumn(
        contentPadding = innerPadding,
        modifier = Modifier.fillMaxSize()
    ) {
        items(teaItems) { item ->
            TeaCard(item, context, navController)
        }
    }
}



@Composable
fun DrawerMenu(categories: Array<String>, onCategorySelected: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Категории чая",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            color = Color(0xFF8BC34A)
        )
        LazyColumn {
            items(categories) { category ->
                Text(
                    text = category,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategorySelected(category) }
                        .padding(16.dp),
                    fontSize = 18.sp
                )
            }
        }
    }
}


@Composable
fun TeaListScreen(teaItems: List<TeaItem>, context: Context, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(teaItems) { item ->
            TeaCard(item, context, navController)
        }
    }
}

@Composable
fun TeaCard(item: TeaItem, context: Context, navController: NavController) {
    val imageResId = context.resources.getIdentifier(item.imageName, "drawable", context.packageName)
    val image = painterResource(id = imageResId)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(200.dp)
            .clickable { navController.navigate("teaDescription/${item.name}") },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Отображаем картинку
            Image(
                painter = image,
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Text(
                text = item.name,
                fontSize = 20.sp,
                color = Color(0xFF4CAF50),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TeaDescriptionScreen(teaName: String, context: Context) {
    val htmlFileName = teaName.lowercase(Locale.getDefault()).replace(" ", "_") // Предположим, что файл назван по имени чая в нижнем регистре
    val assetFilePath = "file:///android_asset/teadescriptions/$htmlFileName.html"

    AndroidView(
        factory = { context ->
            android.webkit.WebView(context).apply {
                loadUrl(assetFilePath)
            }
        },
        update = { webView ->
            webView.loadUrl(assetFilePath)
        }
    )
}




fun getTeaItemsByCategory(category: String, context: Context): List<TeaItem> {
    val data = mapOf(
        "Зеленый чай" to listOf(
            TeaItem("Сенча", "green_tea", "Описание зеленого чая Сенча"),
            TeaItem("Матча", "matcha", "Описание зеленого чая Матча"),
            TeaItem("Ганпаудер", "gunpowder", "Описание зеленого чая Ганпаудер"),
            TeaItem("Лунцзин", "longjing", "Описание зеленого чая Лунцзин")
        ),
        "Черный чай" to listOf(
            TeaItem("Ассам", "black_assam", "Описание черного чая Ассам"),
            TeaItem("Дарджилинг", "black_darjeeling", "Описание черного чая Дарджилинг"),
            TeaItem("Кимун", "keemun", "Описание черного чая Кимун"),
            TeaItem("Цейлонский", "ceylon", "Описание черного чая Цейлонский")
        ),
        "Белый чай" to listOf(
            TeaItem("Белая пион", "white_peony", "Описание белого чая Белая пион"),
            TeaItem("Серебряные иглы", "silver_needles", "Описание белого чая Серебряные иглы"),
            TeaItem("Шоу Мэй", "shou_mei", "Описание белого чая Шоу Мэй")
        ),
        "Травяной чай" to listOf(
            TeaItem("Мята", "mint", "Описание травяного чая Мята"),
            TeaItem("Ромашка", "chamomile", "Описание травяного чая Ромашка"),
            TeaItem("Имбирный чай", "ginger_tea", "Описание травяного чая Имбирный чай"),
            TeaItem("Каркаде", "hibiscus", "Описание травяного чая Каркаде")
        ),
        "Желтый чай" to listOf(
            TeaItem("Хо Шань Хуан Я", "huoshan_huangya", "Описание желтого чая Хо Шань Хуан Я"),
            TeaItem("Цзюнь Шань Инь Чжень", "junshan_yinzhen", "Описание желтого чая Цзюнь Шань Инь Чжень")
        ),
        "Оолонг" to listOf(
            TeaItem("Те Гуаньинь", "tieguanyin", "Описание оолонга Те Гуаньинь"),
            TeaItem("Дахунпао", "dahongpao", "Описание оолонга Дахунпао"),
            TeaItem("Найсян Оолонг", "milky_oolong", "Описание оолонга Найсян Оолонг")
        ),
        "Пуэр" to listOf(
            TeaItem("Шу Пуэр", "shu_puer", "Описание пуэра Шу Пуэр"),
            TeaItem("Шэн Пуэр", "sheng_puer", "Описание пуэра Шэн Пуэр")
        ),
        "Матча" to listOf(спс
            TeaItem("Классический Матча", "classic_matcha", "Описание матча Классический Матча"),
            TeaItem("Матча Латте", "matcha_latte", "Описание матча Матча Латте")
        ),
        "Фруктовый чай" to listOf(
            TeaItem("Цитрусовый", "citrus_tea", "Описание фруктового чая Цитрусовый"),
            TeaItem("Ягодный", "berry_tea", "Описание фруктового чая Ягодный"),
            TeaItem("Манговый", "mango_tea", "Описание фруктового чая Манговый")
        ),
        "Чай с молоком" to listOf(
            TeaItem("Баббл Ти", "bubble_tea", "Описание чая с молоком Баббл Ти"),
            TeaItem("Молочный улун", "milk_oolong", "Описание чая с молоком Молочный улун"),
            TeaItem("Индийский чай", "masala_tea", "Описание чая с молоком Индийский чай")
        )
    )


    return data[category] ?: emptyList()
}
