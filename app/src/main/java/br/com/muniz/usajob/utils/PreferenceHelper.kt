package br.com.muniz.usajob.utils

import android.content.Context
import androidx.preference.PreferenceManager
import br.com.muniz.usajob.Constants

enum class TypeValue {
    STRING,
    INT,
    BOOLEAN
}

class PreferenceHelper(val context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getValue(key: String, typeValue: TypeValue): Any {
        return when (typeValue) {
            TypeValue.BOOLEAN -> sharedPreferences.getBoolean(
                key,
                Constants.DEFAULT_VALUE_BOOLEAN
            )
            TypeValue.STRING -> sharedPreferences.getString(
                key,
                Constants.DEFAULT_VALUE_STRING
            )!!
            TypeValue.INT -> sharedPreferences.getInt(
                key,
                Constants.DEFAULT_VALUE_INT
            )
        }
    }

    fun setValue(key: String, value: Any, typeValue: TypeValue) {
        val editor = sharedPreferences.edit()
        when (typeValue) {
            TypeValue.STRING -> editor.putString(key, value as String).apply()
            TypeValue.INT -> editor.putInt(key, value as Int).apply()
            TypeValue.BOOLEAN -> editor.putBoolean(key, value as Boolean).apply()
        }
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear().apply()
    }
}
