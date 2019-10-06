package com.jdevs.timeo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

open class Data(private val context: Context?) {

    private val gson  = Gson()
    private val activitiesFilename = "activities.time"
    private val recordsFilename = "records.time"

    private var activities = ArrayList<Activity>()
    private var records    = ArrayList<Record>()




//    Listing elements
    private fun list(type : Type, filename : String): ArrayList<Any> {
        var text = "[]"

        try {
            val file = File(context?.filesDir, filename)
            if(file.readText() !== "") {
                text = file.readText()
            }
        } catch (e : Throwable) {
            e.printStackTrace()
        }

        try {
            return gson.fromJson(text, type)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return ArrayList()
    }


    private fun listActivities() {
        val type = object : TypeToken<ArrayList<Activity>>() {}.type
        activities = (list(type, activitiesFilename)) as ArrayList<Activity>
    }


    private fun listRecords() {
        val type = object : TypeToken<ArrayList<Record>>() {}.type
        records = (list(type, recordsFilename)) as ArrayList<Record>
    }




    //    Methods, creating records / activities
    fun createActivity(name: String, icon: String) {
        val currentTime = Calendar.getInstance().time

        listActivities()
        val newId = findBiggestId(activities) + 1
        val activity   = Activity(newId, name, icon, currentTime)
        activities.add(0, activity)

        val jsonData   = gson.toJson(activities)

        val fileOutputStream: FileOutputStream?
        try {
            fileOutputStream = context?.openFileOutput(activitiesFilename, Context.MODE_PRIVATE)
            fileOutputStream?.bufferedWriter().use {out->
                out?.write(jsonData)
            }
        } catch (e: Throwable){
            e.printStackTrace()
        }
    }


    fun createRecord (activityId: Int, minutes: Int) {
        val currentDate = Calendar.getInstance().time

        listRecords()
        val newId = findBiggestId(activities) + 1
        val record     = Record(newId, activityId, currentDate, minutes)
        records.add(0, record)

        val jsonData   = gson.toJson(activities)

        val fileOutputStream: FileOutputStream?
        try {
            fileOutputStream = context?.openFileOutput(recordsFilename, Context.MODE_PRIVATE)
            fileOutputStream?.bufferedWriter().use {out->
                out?.write(jsonData)
            }
        } catch (e: Throwable){
            e.printStackTrace()
        }
    }




//    Methods about adding elements to view
    fun addRecordsToView(parentId: Int, view: View) {
        listRecords()
        listActivities()
        val layout = view.findViewById<LinearLayout>(parentId)
        val parent = view.findViewById<ConstraintLayout>(R.id.activity_container)


        if (records.count() == 0) {
            createNoActivitiesElement(parent)
            return
        }

        for (index in records.indices) {
            val relatedActivity = findActivityById(activities, records[index].activityId)


            val isOdd = index.rem(2) == 0

            val minutes = convertMinsToTime(records[index].minutes)
            createRecordElement(relatedActivity!!.name, minutes, layout, isOdd)
        }
    }


    fun addActivitiesToView(parentId: Int, view: View, navController: NavController) {
        listActivities()

        val layout = view.findViewById<LinearLayout>(parentId)
        val parent = view.findViewById<ConstraintLayout>(R.id.activity_container)

        if (activities.count() == 0) {
            createNoActivitiesElement(parent)
            return
        }

        for (activity in activities) {
            createActivityElement(activity.name, layout, navController)
        }
    }





//    Methods about Record elements
    private fun createRecordElement(title: String, time: String, finalParent: LinearLayout, isOdd: Boolean = false) {
        val parent = createRecordParent(isOdd)

        val textView = createRecordTextView(title)
        val timeLabel = createRecordTimeLabel(time)

        parent.apply {
            addView(textView)
            addView(timeLabel)
        }

        finalParent.addView(parent)
    }


    private fun createRecordParent(isOdd : Boolean = false) : LinearLayout{
        val parent                 = LinearLayout(context)

        val parentParams           =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                height             = context!!.resources.getDimensionPixelSize(R.dimen.record_height)
            }

        val bgColorId = if(isOdd) R.color.colorListOdd else R.color.colorListEven
        val bgColor   = ContextCompat.getColor(context!!, bgColorId)

        parent.apply {
            layoutParams           = parentParams
            orientation            = LinearLayout.HORIZONTAL
            gravity                = Gravity.CENTER_VERTICAL

            setBackgroundColor(bgColor)
            id = View.generateViewId()
        }

        return parent
    }


    private fun createRecordTextView(title : String) :  TextView{
        //        Create TextView
        val textView               = TextView(context)
        val textViewParams         = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        textViewParams.setMargins(120, 0, 15, 0)
        TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_MaterialComponents_Headline6)

        textView.apply {
            layoutParams           = textViewParams
            gravity                = Gravity.CENTER
            text                   = title
            typeface               = Typeface.DEFAULT_BOLD
        }

        return textView
    }


    private fun createRecordTimeLabel(time: String) : LinearLayout {
        //        Create TextView
        val textView               = TextView(context)
        val params                 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        params.setMargins(60, 0, 45, 0)

        textView.apply {
            layoutParams           = params
            gravity                = Gravity.END or Gravity.CENTER_VERTICAL
            text                   = time
            setTextColor(ContextCompat.getColor(context, R.color.colorBlue))
        }

        val linearLayout = LinearLayout(context)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)


        linearLayout.apply {
            this.layoutParams = layoutParams
            addView(textView)
        }



        return linearLayout
    }





//    Methods about activity elements
    private fun createActivityElement(title: String, finalParent: LinearLayout, navController: NavController) {
        val parent = createActivityParent()

//        Create TextView
        val textView               = createActivityTextView(title)
        val imageButtonLayout      = createActivityImageView(navController)

        parent.apply {
            addView(textView)
            addView(imageButtonLayout)
        }

        finalParent.addView(parent)
    }


    private fun createActivityParent() : LinearLayout {
        val parent                 = LinearLayout(context)

        val parentParams           =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                height             = context!!.resources.getDimensionPixelSize(R.dimen.activity_height)
                setMargins(0, 0, 0, 25)
            }

        parent.apply {
            layoutParams           = parentParams
            orientation            = LinearLayout.HORIZONTAL
            setBackgroundResource(R.drawable.border_left)
            id = View.generateViewId()
        }

        return parent
    }


    private fun createActivityTextView(title: String) : TextView {
        val textView               = TextView(context)
        val textViewParams         = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)

        textViewParams.setMargins(35, 0, 15, 0)
        TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_MaterialComponents_Headline5)

        val textTitle = if(title.length < 28) title else title.substring(0, 28) + "..."

        textView.apply {
            layoutParams           = textViewParams
            gravity                = Gravity.CENTER
            text                   = textTitle
            typeface               = Typeface.DEFAULT_BOLD
        }

        return textView
    }


    private fun createActivityImageView(navController: NavController) : LinearLayout {
        val imageButton            = ImageButton(context)
        val imageButtonParams      =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity            = Gravity.END or Gravity.CENTER_VERTICAL
                setMargins(0, 0, 30, 0)
            }

        imageButton.apply {
            setBackgroundResource(R.drawable.background_corners_4dp)
            layoutParams           = imageButtonParams
            setOnClickListener {
                showRecordDialog()
            }
        }

        val imageButtonLayout       = LinearLayout(context)
        val imageButtonLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        imageButtonLayout.apply {
            layoutParams            = imageButtonLayoutParams
            gravity                 = Gravity.END
            addView(imageButton)
        }

        return imageButtonLayout
    }


    private fun createNoActivitiesElement(parent: ConstraintLayout) {
        val linearLayout = LinearLayout(context)

        linearLayout.apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER

            val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
            params.apply {
                topToTop = parent.id
                rightToRight= parent.id
                bottomToBottom= parent.id
                leftToLeft= parent.id
                width = ConstraintLayout.LayoutParams.MATCH_PARENT
                height = ConstraintLayout.LayoutParams.MATCH_PARENT
            }

            layoutParams = params
        }

        val textView = TextView(context)

        textView.apply {
            text = "No activities yet.."
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val button = MaterialButton(context)

        button.apply{
            text = "Tap to add a task"
            layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            setOnClickListener(Navigation.createNavigateOnClickListener(R.id.viewCreateActivityFragment))
        }

        linearLayout.apply {
            addView(textView)
            addView(button)
        }

        TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_MaterialComponents_Headline5)
        parent.addView(linearLayout)
    }




    private fun showRecordDialog() {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.fragment_record_activity_dialog)

        val screenDimensions = ScreenManager.getScreenDimensions(context)

        val dialogWidth  = (screenDimensions.first  * 0.85).roundToInt()
        val dialogHeight = (screenDimensions.second * 0.50).roundToInt()

        val window = dialog.window
        window!!.setLayout(dialogWidth, dialogHeight)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }




    //  General methods
    private fun convertMinsToTime(minutes: Int) : String {
        val hours = minutes.div(60)
        val mins  = minutes.rem(60)

        val hoursString = if(hours > 0) "${hours}h " else ""

        return "${hoursString}${mins}m"
    }


    private fun findActivityById(list : ArrayList<Activity>, id : Int) : Activity? {
        for (item in list) {
            if(item.id == id) {
                return item
            }
        }

        return null
    }


    private fun <T>findBiggestId(list : ArrayList<T>) : Int {
        var biggest = 0
        for (item in list) {
            if((item is Activity) && item.id > biggest) {
                biggest = item.id
                continue
            }

            if((item is Record) && item.id > biggest) {
                biggest = item.id
                continue
            }
        }

        return biggest
    }
}

class Activity(var id: Int, var name: String, var icon: String, var creationDate: Date)
class Record(var id : Int, var activityId: Int, var date: Date, var minutes: Int)
