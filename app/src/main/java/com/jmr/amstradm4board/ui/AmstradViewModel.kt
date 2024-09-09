package com.jmr.amstradm4board.ui

import android.util.Log
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

    init {
        updateAndDownloadFile()
//        downloadAndParseFile("games")
//        downloadAndParseFile("games/aaa%20JM")
    }

    fun updateAndDownloadFile() {
        viewModelScope.launch {
            try {
                // Simula la solicitud del navegador
                val response = repository.performHttpRequest()

                if (response?.isSuccessful == true) {
                    val gameFiles = repository.getDataList()
                    _dataFileList.postValue(gameFiles)
                }
            } catch (e: Exception) {
                // Manejar el error
                Log.v("MY_LOG", "Exception: ${e.message}")
            }
        }
    }

    fun downloadAndParseFile(path: String) {
        viewModelScope.launch {
            try {
                val dataFiles = repository.updateList(path)
                _dataFileList.postValue(dataFiles)
            } catch (e: Exception) {
                // Maneja el error
            }
        }
    }

}