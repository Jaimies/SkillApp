package com.maxpoliakov.skillapp.data

import android.content.SharedPreferences

class StubSharedPreferences(data: Map<String, Any>) : SharedPreferences {
    private val data = data.toMutableMap()

    override fun getAll() = data

    override fun getString(key: String?, defValue: String?) = getValue(key, defValue)
    override fun getStringSet(key: String?, defValues: MutableSet<String>?) = getValue(key, defValues)
    override fun getInt(key: String?, defValue: Int) = getValue(key, defValue)
    override fun getLong(key: String?, defValue: Long) = getValue(key, defValue)
    override fun getFloat(key: String?, defValue: Float) = getValue(key, defValue)
    override fun getBoolean(key: String?, defValue: Boolean) = getValue(key, defValue)

    private inline fun <reified T> getValue(key: String?, defValue: T): T {
        return data.getOrDefault(key, defValue) as T
    }

    override fun contains(key: String?) = data.contains(key)

    override fun edit(): SharedPreferences.Editor = Editor()

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}
    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {}

    inner class Editor : SharedPreferences.Editor {
        override fun putString(key: String?, value: String?) = putValue(key, value)

        override fun putStringSet(key: String?, values: MutableSet<String>?) = putValue(key, values)
        override fun putInt(key: String?, value: Int) = putValue(key, value)
        override fun putLong(key: String?, value: Long) = putValue(key, value)
        override fun putFloat(key: String?, value: Float) = putValue(key, value)
        override fun putBoolean(key: String?, value: Boolean) = putValue(key, value)
        override fun remove(key: String?) = data.remove(key).let { this }
        override fun clear() = data.clear().let { this }
        override fun commit() = true
        override fun apply() {}

        private inline fun <reified T> putValue(key: String?, value: T): SharedPreferences.Editor {
            if (value == null) data.remove(key)
            else data[key!!] = value as Any
            return this
        }
    }
}
