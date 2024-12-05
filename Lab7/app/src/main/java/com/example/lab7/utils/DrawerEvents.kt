package com.example.lab7.utils

sealed class DrawerEvents {
    data class OnItemClick(val title: String,val index:Int): DrawerEvents()
}
