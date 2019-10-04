package com.jdevs.timeo

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.TextViewCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream

open class Data(private val context: Context?) {
    private val gson  = Gson()
    private val filename = "record.json"
    private var shownActivities = ArrayList<LinearLayout>()

    fun addActivity(name: String, icon: String) {
        val activity = Activity(name, icon)
        val activities = listActivities()
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

    private fun listActivities() : ArrayList<Activity> {
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

    fun addActivitiesToElement(parentId: Int, view: View) {
        val activities = listActivities()
        val layout = view.findViewById<LinearLayout>(parentId)

        for (activity in activities) {
            createElement(activity.name, layout)
        }
    }

    private fun createElement(title: String, finalParent: LinearLayout) {
        val parent            = LinearLayout(context)

        val parentParams      =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                height   = context!!.resources.getDimensionPixelSize(R.dimen.activity_height)
                setMargins(0, 0, 0, 25)
            }

        parent.apply {
            layoutParams   = parentParams
            orientation    = LinearLayout.HORIZONTAL
            setBackgroundResource(R.drawable.border_left)
            id = View.generateViewId()
        }

//        Create TextView
        val textView          = TextView(context)
        val textViewParams    = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        textViewParams.setMargins(60, 0, 15, 0)

        textView.apply {
            layoutParams = textViewParams
            gravity      = Gravity.CENTER
            text         = title
        }

        TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_MaterialComponents_Headline5)
        textView.typeface     = Typeface.DEFAULT_BOLD

//        Add image button
        val imageButton = ImageButton(context)
        val imageButtonParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.END or Gravity.CENTER_VERTICAL
                setMargins(0, 0, 30, 0)
            }

        imageButton.apply {
            setBackgroundResource(R.drawable.background_corners_4dp)
            layoutParams = imageButtonParams
            setOnClickListener {
                Toast.makeText(context, "Recording activity", Toast.LENGTH_LONG).show()
            }
        }

        val imageButtonLayout = LinearLayout(context)
        val imageButtonLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        imageButtonLayout.apply {
            layoutParams = imageButtonLayoutParams
            gravity = Gravity.END
            addView(imageButton)
        }


        parent.apply {
            addView(textView)
            addView(imageButtonLayout)
        }

        finalParent.addView(parent)

        shownActivities.add(parent)
    }
}

class Activity(var name: String, var icon: String)