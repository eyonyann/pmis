package com.example.lab7

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.lab7.db.MainDb
import com.example.lab7.utils.ListItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.text.Typography.dagger

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainDb: MainDb
):ViewModel() {
    val mainList = mutableStateOf(emptyList<ListItem>())
    private var job: Job? = null
    fun getAllItemsByCategory(cat: String) {
        job?.cancel()
        job = viewModelScope.launch {
            mainDb.dao.getAllItemsByCategory(cat).collect { list -> mainList.value =
                list }
        }
    }
    fun getFavorites(){
        job?.cancel()
        job = viewModelScope.launch {
            mainDb.dao.getFavorites().collect{ list -> mainList.value = list}
        }
    }
    fun insertItem(item: ListItem) = viewModelScope.launch {
        mainDb.dao.insertItem(item)
    }
    fun deleteItem(item: ListItem) = viewModelScope.launch {
        mainDb.dao.deleteItem(item)
    }
}
