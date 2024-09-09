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

    private var lastPath = "/"

    init {
        navigate("games/aaa%20JM")
    }

    fun navigate(path: String) {
        viewModelScope.launch {
            try {
                val response = repository.navigate(path)

                if (response?.isSuccessful == true) {
                    lastPath = path
                    val gameFiles = repository.getDataList(path)
                    _dataFileList.postValue(gameFiles)
                }
            } catch (e: Exception) {
                Log.v("MY_LOG", "Exception: ${e.message}")
            }
        }
    }

    fun runGame(path: String) {
        viewModelScope.launch {
            try {
                repository.runGame(path)
            } catch (e: Exception) {
                Log.v("MY_LOG", "Exception: ${e.message}")
            }
        }
    }

    private fun cleanPath(input: String): String {
        return input.substringBeforeLast("/", "")
    }

    fun goBack() {
        navigate(cleanPath(lastPath))
    }

}