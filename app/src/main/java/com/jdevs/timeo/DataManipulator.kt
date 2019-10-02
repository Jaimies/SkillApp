package com.jdevs.timeo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream

open class DataManipulator {
    private val gson  = Gson()
    private val filename = "record.json"

    fun addActivity(context : Context?, name: String, icon: String) {
        val activity = Activity(name, icon)
        val activities = listActivities(context)
        activities.add(0, activity)


        val jsonData = gson.toJson(activities)

        val fileOutputStream: FileOutputStream?
        try {
            fileOutputStream = context?.openFileOutput(filename, Context.MODE_PRIVATE)
            fileOutputStream?.bufferedWriter().use {out->
                out?.write(jsonData)
            }
        } catch (e: Throwable){
            e.printStackTrace()
        }
    }

    fun listActivities(context: Context?) : ArrayList<Activity> {
        var text = "[]"

        try {
            val file = File(context?.filesDir, filename)
            if(file.readText() !== "") {
                text = file.readText()
            }
        } catch (e : Throwable) {
            e.printStackTrace()
        }

        val type = object : TypeToken<ArrayList<Activity>>() {}.type

        try {
            return gson.fromJson(text, type)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return ArrayList()
    }

    fun readFile(context: Context?) : String {
        val file = File(context?.filesDir, filename)
        if(file.readText() !== "") {
            return file.readText()
        }

        return ""
    }
}

class Activity(var name: String, var icon: String)

open class DataManipulatorFragment : FragmentWithActionBarNavigation()  {
    companion object Records: DataManipulator()
}