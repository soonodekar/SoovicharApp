package com.soonodekar.myapp1.settings

import android.app.Activity
import android.content.Context

object Settings {
    private const val SETTINGS_PREFERENCE = "setting_preferences"

    fun setNotificationsNumber(context: Context, number : Int, numberType :String) {
        val prefs = context.getSharedPreferences(SETTINGS_PREFERENCE, Activity.MODE_PRIVATE)
        prefs.edit()
            .putInt(numberType, number)
            .apply()
    }

    fun getNotificationsNumber(context: Context, numberType: String) : Int  {
        val prefs = context.getSharedPreferences(SETTINGS_PREFERENCE, Activity.MODE_PRIVATE)
        return prefs.getInt(numberType, -1)
    }
}