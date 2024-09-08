package com.jmr.amstradm4board.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmr.amstradm4board.Xfer
import com.jmr.amstradm4board.XferApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class XferViewModel @Inject constructor(
    private val xferApi: XferApi
) : ViewModel() {

    private val _files = mutableStateOf<List<String>>(emptyList())
    val files: State<List<String>> = _files

    fun listFiles(folder: String) {
        viewModelScope.launch {
            try {
                val response = Xfer("192.168.1.39").listFiles(folder)
                if (!response.isNullOrEmpty()) {
                    _files.value = response
                }
//                if (response.isSuccessful) {
//                    _files.value = response.body() ?: emptyList()
//                }
            } catch (e: HttpException) {
                // Maneja la excepción HTTP
            }
        }
    }

    fun resetCPC() {
        viewModelScope.launch {
            try {
                xferApi.resetCPC()
            } catch (e: HttpException) {
                // Maneja el error
            }
        }
    }
}