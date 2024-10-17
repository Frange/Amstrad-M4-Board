package com.jmr.amstradm4board.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmr.amstradm4board.data.repository.AmstradRepository
import com.jmr.amstradm4board.data.repository.AmstradSharedPreference
import com.jmr.amstradm4board.domain.model.DataFile
import com.jmr.amstradm4board.ui.Utils.logs
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.initPath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AmstradViewModel @Inject constructor(
    private val repository: AmstradRepository,
    private val sharedPreference: AmstradSharedPreference
) : ViewModel() {

    sealed class ListState {
        object Loading : ListState()
        data class Loaded(val items: List<DataFile>) : ListState()
        object Error : ListState()
    }

    private val _dataFileList = MutableStateFlow<List<DataFile>>(emptyList())
    val dataFileList: StateFlow<List<DataFile>> get() = _dataFileList

    var lastPath by mutableStateOf(initPath)
        private set

    internal var isRefreshing by mutableStateOf(false)
    internal var showDskDialog by mutableStateOf(false)
    internal var selectedDskName by mutableStateOf("")
    var dskFiles by mutableStateOf<List<DataFile>>(emptyList())
    var ipAddress by mutableStateOf(getIp())
        private set

    var listState by mutableStateOf<ListState>(ListState.Loading)
        private set

    fun toggleRefreshing(refreshing: Boolean) {
        isRefreshing = refreshing
    }

    fun toggleDskDialog(show: Boolean) {
        showDskDialog = show
    }

    fun setSelectedDskName(name: String) {
        selectedDskName = name
    }

    fun navigate(path: String) {
        listState = ListState.Loading
        viewModelScope.launch {
            try {
                val items = repository.navigate(ipAddress, path)
                _dataFileList.value = items // Actualiza el flujo
                listState = ListState.Loaded(items)
            } catch (e: Exception) {
                listState = ListState.Error
            }
        }
    }

    fun openDSK(path: String, onFilesLoaded: (List<DataFile>) -> Unit) {
        viewModelScope.launch {
            try {
                val dskFiles = repository.navigate(ipAddress, path)
                onFilesLoaded(dskFiles)
            } catch (e: Exception) {
                logs("Exception: ${e.message}")
            }
        }
    }

    // Resto de funciones sin cambios...

    fun refreshFileList() {
        navigate(lastPath)
    }

    fun setNewIp(newIp: String) {
        if (isValidIpAddress(newIp)) {
            sharedPreference.saveIpAddress(newIp)
            ipAddress = newIp
        }
    }

    fun runGame(path: String) {
        viewModelScope.launch {
            try {
                repository.runGame(ipAddress, path)
            } catch (e: Exception) {
                logs("Exception: ${e.message}")
            }
        }
    }

    fun goBack() {
        navigate(cleanPath(lastPath))
    }

    private fun getIp(): String {
        return sharedPreference.getLastIpAddress()
    }

    private fun cleanPath(input: String): String {
        return input.substringBeforeLast("/", "")
    }

    fun resetM4() {
        viewModelScope.launch {
            try {
                repository.resetM4(ipAddress)
            } catch (e: Exception) {
                logs("Exception: Reset M4 - ${e.message}")
            }
        }
    }

    fun resetCPC() {
        viewModelScope.launch {
            try {
                repository.resetCPC(ipAddress)
            } catch (e: Exception) {
                logs("Exception: Reset CPC - ${e.message}")
            }
        }
    }

    private fun isValidIpAddress(ip: String): Boolean {
        val ipRegex = Regex(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$"
        )
        return ip.matches(ipRegex)
    }
}
