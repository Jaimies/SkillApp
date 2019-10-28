package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_create_activity.view.*

class CreateActivityFragment : FragmentWithActionBarNavigation() {

    private lateinit var titleTextInputLayout: TextInputLayout
    private lateinit var titleEditText : EditText

    private lateinit var iconTextInputLayout: TextInputLayout
    private lateinit var iconEditText  : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_create_activity, container, false)

        titleEditText = view.titleEditText
        titleTextInputLayout = view.titleTextInputLayout

        iconEditText = view.iconEditText
        iconTextInputLayout = view.iconTextInputLayout


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
                    val title = titleEditText.text.toString()
                    val icon = iconEditText.text.toString()

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

        if (titleEditText.text.isEmpty()) {

            titleTextInputLayout.error = "Title cannot be empty"

            return false

        }

        if (iconEditText.text.isEmpty()) {

            iconTextInputLayout.error = "Icon cannot be empty"

            return false

        }

        return true
    }
}
