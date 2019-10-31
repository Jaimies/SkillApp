package com.jdevs.timeo.models

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.jdevs.timeo.R
import com.jdevs.timeo.TAG
import com.jdevs.timeo.data.TimeoRecord
import kotlinx.android.synthetic.main.partial_records_list_item.view.*

class RecordsListAdapter(

    private val dataset: Array<TimeoRecord>,
    private val mRecordsCollection : CollectionReference,
    private val mItemIds : ArrayList<String>,
    private val context : Context?

) : RecyclerView.Adapter<RecordsListAdapter.ViewHolder>() {

    inner class ViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout),
        View.OnLongClickListener {

        init {

            layout.setOnLongClickListener(this)

        }

        override fun onLongClick(v: View): Boolean {

            if(context == null) {

                return true

            }

            val dialog = AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle("Are you sure?")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Yes") { _: DialogInterface, _: Int ->

                    val documentId = mItemIds[adapterPosition]

                    mRecordsCollection
                        .document(documentId)
                        .delete()
                        .addOnFailureListener { firebaseFirestoreException ->

                            Log.w(TAG, "Failed to delete data from Firestore", firebaseFirestoreException)

                        }


                    Snackbar.make(v, "Item deleted", Snackbar.LENGTH_LONG).show()

                }
                .setNegativeButton("No", null)


            dialog.show()


            return true

        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_records_list_item, parent, false) as LinearLayout


        return ViewHolder(layout)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.layout.apply {

            val backgroundColorId = if(position.rem(2) == 0) R.color.colorListEven else R.color.colorListOdd

            setBackgroundColor(ContextCompat.getColor(context,backgroundColorId))


            activityNameTextView.apply {

                text = dataset[position].title

            }

            workTimeTextView.apply {

                val timeMinutes = dataset[position].workingTime

                val hours = timeMinutes.div(60)
                val minutes = timeMinutes.rem(60)

                var timeString = ""

                if(hours != 0) {

                    timeString += "${hours}h "

                }

                if(minutes != 0) {

                    timeString += "${minutes}m"

                }

                text = timeString

            }
        }

    }

    override fun getItemCount() = dataset.size
}