package com.jdevs.timeo.adapters

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.R
import com.jdevs.timeo.data.TimeoRecord
import com.jdevs.timeo.utilities.TAG
import com.jdevs.timeo.utilities.TimeUtitlity
import kotlinx.android.synthetic.main.partial_records_list_item.view.*

class RecordsListAdapter(

    private val dataset: ArrayList<TimeoRecord>,
    private val mRecordsCollection: CollectionReference,
    private val mItemIds: ArrayList<String>,
    private val userId: String,
    private val context: Context?

) : RecyclerView.Adapter<RecordsListAdapter.ViewHolder>() {

    inner class ViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout),
        View.OnLongClickListener,
        OnFailureListener,
        DialogInterface.OnClickListener {

        private var view: View? = null

        init {

            layout.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View): Boolean {

            view = v

            showDialog()

            return true
        }

        private fun showDialog() {

            val dialog = AlertDialog.Builder(context!!)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle("Are you sure?")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Yes", this)
                .setNegativeButton("No", null)

            dialog.show()
        }

        override fun onFailure(firebaseException: Exception) {

            Log.w(
                TAG,
                "Failed to delete data from Firestore",
                firebaseException
            )
        }

        override fun onClick(dialog: DialogInterface?, which: Int) {

            val documentId = mItemIds[adapterPosition]

            mRecordsCollection
                .document(documentId)
                .delete()
                .addOnFailureListener(this)

            val activity = FirebaseFirestore.getInstance()
                .document("/users/$userId/activities/${dataset[adapterPosition].activityId}")

            val workingTime = dataset[adapterPosition].workingTime.toLong()

            activity.update("totalTime", FieldValue.increment(-workingTime))

            Snackbar.make(view!!, "Record deleted", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_records_list_item, parent, false) as ConstraintLayout

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.layout.apply {

            val backgroundColorId =
                if (position.rem(2) == 0) R.color.colorListEven else R.color.colorListOdd

            setBackgroundColor(ContextCompat.getColor(context, backgroundColorId))

            activityNameTextView.apply {

                text = dataset[position].title
            }

            workTimeTextView.apply {

                val time = TimeUtitlity.minsToTime(dataset[position].workingTime)

                var timeString = ""

                if (time.first != 0) {

                    timeString += "${time.first}h "
                }

                if (time.second != 0) {

                    timeString += "${time.second}m"
                }

                text = timeString
            }
        }
    }

    override fun getItemCount() = dataset.size
}
