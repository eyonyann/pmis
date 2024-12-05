package com.example.lab7.ui_components

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.lab7.utils.ListItem

@Composable
fun InfoScreen (item: ListItem){
    Card(
        modifier = Modifier.fillMaxSize().padding(5.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AssetImage(
                imageName = item.imageName,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            HtmlLoader(htmlName = item.htmlName)
        }
    }
}

@Composable
fun HtmlLoader(htmlName: String) {
    var backEnabled by remember { mutableStateOf(false) }
    var webView: WebView? = null
    val context = LocalContext.current
    val assetManger = context.assets
    val inputStream = assetManger.open("html/$htmlName")
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    val htmlString = String(buffer)
    AndroidView(modifier = Modifier.fillMaxSize().padding(5.dp),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String?,
                                               favicon: Bitmap?) {
                        backEnabled = view.canGoBack()
                    }
                }
                settings.javaScriptEnabled = true
                loadData(htmlString, "text/html", "utf-8")
                webView = this
            }
        }, update = {
            webView = it
        })
    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }
}



