package com.jmr.amstradm4board.data.repository

import android.app.Application
import android.content.Context
import com.jmr.amstradm4board.ui.render.config.MainScreenConfig.Companion.defaultIp


class AmstradSharedPreference(
    val context: Application
) {

    fun getLastIpAddress(): String {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("last_ip", defaultIp) ?: defaultIp
    }

    fun saveIpAddress(ipAddress: String) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("last_ip", ipAddress)
            commit()
        }
    }

}