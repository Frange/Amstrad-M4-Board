package com.jmr.amstradm4board.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmr.amstradm4board.data.repository.AmstradRepository
import com.jmr.amstradm4board.domain.model.DataFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AmstradViewModel @Inject constructor(
    private val repository: AmstradRepository
) : ViewModel() {

    private val _dataFileList = MutableLiveData<List<DataFile>>()
    val dataFileList: LiveData<List<DataFile>> get() = _dataFileList

    private var lastPath = "/"
    var ipAddress: String = "192.168.1.39"

    internal var isRefreshing by mutableStateOf(false)

    internal var showDskDialog by mutableStateOf(false)

    internal var selectedDskName by mutableStateOf("")

    var dskFiles by mutableStateOf<List<DataFile>>(emptyList())

//    var dskFiles by mutableStateOf<List<DataFile>>(emptyList())

    fun toggleRefreshing(refreshing: Boolean) {
        isRefreshing = refreshing
    }

    fun toggleDskDialog(show: Boolean) {
        showDskDialog = show
    }

    fun setSelectedDskName(name: String) {
        selectedDskName = name
    }

//    fun setDskFiles(files: List<DataFile>) {
//        dskFiles = files
//    }

    fun navigate(ip: String, path: String) {
        viewModelScope.launch {
            try {
                val gameFiles = repository.navigate(ip, path)

                lastPath = path
                _dataFileList.postValue(gameFiles)
            } catch (e: Exception) {
                Log.v("MY_LOG", "Exception: ${e.message}")
            }
        }
    }

    fun openDSK(ip: String, path: String, onFilesLoaded: (List<DataFile>) -> Unit) {
        viewModelScope.launch {
            try {
                val dskFiles = repository.navigate(ip, path)  // Método en el repositorio para obtener el contenido del .dsk
                onFilesLoaded(dskFiles)
            } catch (e: Exception) {
                Log.v("MY_LOG", "Exception: ${e.message}")
            }
        }
    }

    fun runGame(ip: String, path: String) {
        viewModelScope.launch {
            try {
                repository.runGame(ip, path)
            } catch (e: Exception) {
                Log.v("MY_LOG", "Exception: ${e.message}")
            }
        }
    }

    fun goBack(ip: String) {
        navigate(ip, cleanPath(lastPath))
    }

    private fun cleanPath(input: String): String {
        return input.substringBeforeLast("/", "")
    }

    fun resetM4() {

    }

    fun resetCPC() {

    }

    fun refreshFileList(ip: String) {
        navigate(ip, lastPath)
    }
}