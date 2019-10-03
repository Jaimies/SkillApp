package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddActivityFragment : FragmentWithActionBarNavigation() {
    private lateinit var title : EditText
    private lateinit var icon  : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_add_activity, container, false)
        title = view.findViewById(R.id.title)
        icon  = view.findViewById(R.id.icon)

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_add_activity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.doneAddingActivity -> {
                if(validateInput()) {
                    val title = title.text.toString()
                    val icon = icon.text.toString()

                    Data().addActivity(context, title, icon)

                    findNavController().navigate(R.id.homeFragment)
                }
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

        return true

    }

    private fun validateInput() : Boolean {
        var valid = true

        if (title.text.toString() == "") {
            title.error = "Title cannot be empty"
            valid =  false
        }

        if (icon.text.toString() == "") {
            icon.error = "Icon cannot be empty"
            valid = false
        }

        return valid
    }
}
