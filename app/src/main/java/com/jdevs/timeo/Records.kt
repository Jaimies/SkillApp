package com.jdevs.timeo

import android.app.PendingIntent.getActivity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream

open class Data {
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

    private fun listActivities(context: Context?) : ArrayList<Activity> {
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

    fun addActivitiesToElement(context: Context?, parentId: Int, view: View) {
        val activities = listActivities(context)
        val layout = view.findViewById<RelativeLayout>(parentId)

        val shownActivities = ArrayList<TextView>()

        for (item in activities.indices) {
            val textView = TextView(context)
            textView.text = activities[item].name

            val id = View.generateViewId()
            textView.id = id

            val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 15)

            if (shownActivities.count() > 0) {
                val prevId = shownActivities.last().id
                params.addRule(RelativeLayout.BELOW, prevId)

            }

            textView.layoutParams = params

            layout.addView(textView)
            shownActivities.add(textView)
        }
    }

    fun createElement(context: Context?, title: String, finalParent: RelativeLayout) {
        val parent = LinearLayout(context)

        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        parent.layoutParams = params
        parent.orientation = LinearLayout.HORIZONTAL

        parent.background = ResourcesCompat.getDrawable(context!!.resources, R.drawable.border_left, null)

        val textView = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        layoutParams.setMargins(15, 0, 15, 0)
        textView.layoutParams = layoutParams
        textView.text = title

        parent.addView(textView)

        finalParent.addView(parent)
    }
}

class Activity(var name: String, var icon: String)