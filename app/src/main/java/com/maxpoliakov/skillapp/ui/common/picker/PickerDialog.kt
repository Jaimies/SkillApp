package com.maxpoliakov.skillapp.ui.common.picker

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import cn.carbswang.android.numberpickerview.library.NumberPickerView
import com.google.android.material.shape.MaterialShapeDrawable
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.util.error.logToCrashlytics
import com.maxpoliakov.skillapp.util.ui.setup

abstract class PickerDialog : DialogFragment() {
    private val positiveButtonListeners: MutableSet<View.OnClickListener> = LinkedHashSet()
    private val negativeButtonListeners: MutableSet<View.OnClickListener> = LinkedHashSet()
    private val cancelListeners: MutableSet<DialogInterface.OnCancelListener> = LinkedHashSet()
    private val dismissListeners: MutableSet<DialogInterface.OnDismissListener> = LinkedHashSet()

    private var titleResId = 0
    private var titleText: String? = null

    lateinit var firstPicker: NumberPickerView
    lateinit var secondPicker: NumberPickerView

    private var overrideThemeResId = 0

    abstract fun getFirstPickerValues(): Array<String>
    abstract fun getSecondPickerValues(): Array<String>

    override fun onCreateDialog(bundle: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), themeResId)
        val context = dialog.context
        val surfaceColor = resolveOrThrow(
            context, R.attr.colorSurface, DurationPicker::class.java.canonicalName
        )
        val background = MaterialShapeDrawable(
            context,
            null,
            R.attr.materialTimePickerStyle,
            R.style.Widget_MaterialComponents_TimePicker
        )
        val a = context.obtainStyledAttributes(
            null,
            R.styleable.MaterialTimePicker,
            R.attr.materialTimePickerStyle,
            R.style.Widget_MaterialComponents_TimePicker
        )
        a.recycle()
        background.initializeElevationOverlay(context)
        background.fillColor = ColorStateList.valueOf(surfaceColor)
        val window = dialog.window
        window!!.setBackgroundDrawable(background)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        // On some Android APIs the dialog won't wrap content by default. Explicitly update here.
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return dialog
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        restoreState(bundle ?: arguments)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)

        bundle.putInt(TITLE_RES_EXTRA, titleResId)
        bundle.putString(TITLE_TEXT_EXTRA, titleText)
        bundle.putInt(OVERRIDE_THEME_RES_ID, overrideThemeResId)
        bundle.putInt(FIRST_PICKER_VALUE, firstPicker.value)
        bundle.putInt(SECOND_PICKER_VALUE, secondPicker.value)
    }

    fun restoreState(bundle: Bundle?) {
        if (bundle == null) return
        titleResId = bundle.getInt(TITLE_RES_EXTRA, 0)
        titleText = bundle.getString(TITLE_TEXT_EXTRA)
        overrideThemeResId = bundle.getInt(OVERRIDE_THEME_RES_ID, 0)

        lifecycleScope.launchWhenStarted {
            restoreStateOfPickers(bundle)
        }
    }

    protected open fun restoreStateOfPickers(bundle: Bundle) {
        try {
            firstPicker.value = bundle.getInt(FIRST_PICKER_VALUE, 0)
            secondPicker.value = bundle.getInt(SECOND_PICKER_VALUE, 0)
        } catch (e: Throwable) {
            e.logToCrashlytics()
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        val root = layoutInflater.inflate(R.layout.picker_dialog, viewGroup) as ViewGroup

        firstPicker = root.findViewById(R.id.hours_picker)
        firstPicker.setup(getFirstPickerValues())

        secondPicker = root.findViewById(R.id.minutes_picker)
        secondPicker.setup(getSecondPickerValues())

        val headerTitle = root.findViewById<TextView>(R.id.header_title)
        if (!TextUtils.isEmpty(titleText)) {
            headerTitle.text = titleText
        }
        if (titleResId != 0) {
            headerTitle.setText(titleResId)
        }
        val okButton = root.findViewById<Button>(R.id.material_timepicker_ok_button)
        okButton.setOnClickListener { v ->
            for (listener in positiveButtonListeners) {
                listener.onClick(v)
            }
            dismiss()
        }
        val cancelButton = root.findViewById<Button>(R.id.material_timepicker_cancel_button)
        cancelButton.setOnClickListener { v ->
            for (listener in negativeButtonListeners) {
                listener.onClick(v)
            }
            dismiss()
        }
        return root
    }

    override fun onCancel(dialogInterface: DialogInterface) {
        for (listener in cancelListeners) {
            listener.onCancel(dialogInterface)
        }
        super.onCancel(dialogInterface)
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        for (listener in dismissListeners) {
            listener.onDismiss(dialogInterface)
        }
        super.onDismiss(dialogInterface)
    }

    /** The supplied listener is called when the user confirms a valid selection.  */
    fun addOnPositiveButtonClickListener(listener: View.OnClickListener): Boolean {
        return positiveButtonListeners.add(listener)
    }

    /**
     * Removes a listener previously added via [ ][DurationPicker.addOnPositiveButtonClickListener].
     */
    fun removeOnPositiveButtonClickListener(listener: View.OnClickListener): Boolean {
        return positiveButtonListeners.remove(listener)
    }

    /**
     * Removes all listeners added via [ ][DurationPicker.addOnPositiveButtonClickListener].
     */
    fun clearOnPositiveButtonClickListeners() {
        positiveButtonListeners.clear()
    }

    /** The supplied listener is called when the user clicks the cancel button.  */
    fun addOnNegativeButtonClickListener(listener: View.OnClickListener): Boolean {
        return negativeButtonListeners.add(listener)
    }

    /**
     * Removes a listener previously added via [ ][DurationPicker.addOnNegativeButtonClickListener].
     */
    fun removeOnNegativeButtonClickListener(listener: View.OnClickListener): Boolean {
        return negativeButtonListeners.remove(listener)
    }

    /**
     * Removes all listeners added via [ ][DurationPicker.addOnNegativeButtonClickListener].
     */
    fun clearOnNegativeButtonClickListeners() {
        negativeButtonListeners.clear()
    }

    /**
     * The supplied listener is called when the user cancels the picker via back button or a touch
     * outside the view.
     *
     *
     * It is not called when the user clicks the cancel button. To add a listener for use when the
     * user clicks the cancel button, use [ ][DurationPicker.addOnNegativeButtonClickListener].
     */
    fun addOnCancelListener(listener: DialogInterface.OnCancelListener): Boolean {
        return cancelListeners.add(listener)
    }

    /**
     * Removes a listener previously added via [ ][DurationPicker.addOnCancelListener].
     */
    fun removeOnCancelListener(listener: DialogInterface.OnCancelListener): Boolean {
        return cancelListeners.remove(listener)
    }

    /**
     * Removes all listeners added via [ ][DurationPicker.addOnCancelListener].
     */
    fun clearOnCancelListeners() {
        cancelListeners.clear()
    }

    /**
     * The supplied listener is called whenever the DialogFragment is dismissed, no matter how it is
     * dismissed.
     */
    fun addOnDismissListener(listener: DialogInterface.OnDismissListener): Boolean {
        return dismissListeners.add(listener)
    }

    /**
     * Removes a listener previously added via [ ][DurationPicker.addOnDismissListener].
     */
    fun removeOnDismissListener(listener: DialogInterface.OnDismissListener): Boolean {
        return dismissListeners.remove(listener)
    }

    /**
     * Removes all listeners added via [ ][DurationPicker.addOnDismissListener].
     */
    fun clearOnDismissListeners() {
        dismissListeners.clear()
    }

    private val themeResId: Int
        get() {
            if (overrideThemeResId != 0) {
                return overrideThemeResId
            }
            val value = resolve(requireContext(), R.attr.materialTimePickerTheme)
            return value?.data ?: 0
        }

    /** Used to create [DurationPicker] instances.  */
    abstract class Builder {
        open var titleTextResId = 0
        var titleText: CharSequence? = null
        var overrideThemeResId = 0
        var firstPickerValue = 0
        var secondPickerValue = 0

        abstract fun createDialog(): PickerDialog

        fun setFirstPickerValue(value: Int): Builder {
            firstPickerValue = value
            return this
        }

        fun setSecondPickerValue(value: Int): Builder {
            secondPickerValue = value
            return this
        }

        /**
         * Sets the text used to guide the user at the top of the picker.
         */
        fun setTitleText(@StringRes titleTextResId: Int): Builder {
            this.titleTextResId = titleTextResId
            return this
        }

        /**
         * Sets the text used to guide the user at the top of the picker.
         */
        fun setTitleText(charSequence: CharSequence?): Builder {
            titleText = charSequence
            return this
        }

        /** Sets the theme for the time picker.  */
        fun setTheme(@StyleRes themeResId: Int): Builder {
            overrideThemeResId = themeResId
            return this
        }

        open fun build(): PickerDialog {
            return createDialog().apply {
                arguments = createArguments()
            }
        }

        private fun createArguments(): Bundle {
            val args = Bundle().apply {
                putInt(FIRST_PICKER_VALUE, firstPickerValue)
                putInt(SECOND_PICKER_VALUE, secondPickerValue)
                putInt(TITLE_RES_EXTRA, titleTextResId)
                putInt(OVERRIDE_THEME_RES_ID, overrideThemeResId)
                if (titleText != null)
                    putString(TITLE_TEXT_EXTRA, titleText.toString())
            }
            return args
        }
    }

    companion object {
        const val TITLE_RES_EXTRA = "TIME_PICKER_TITLE_RES"
        const val TITLE_TEXT_EXTRA = "TIME_PICKER_TITLE_TEXT"
        const val OVERRIDE_THEME_RES_ID = "TIME_PICKER_OVERRIDE_THEME_RES_ID"
        const val FIRST_PICKER_VALUE = "FIRST_PICKER_VALUE"
        const val SECOND_PICKER_VALUE = "SECOND_PICKER_VALUE"

        fun resolveOrThrow(
            context: Context,
            @AttrRes attributeResId: Int,
            errorMessageComponent: String
        ): Int {
            val typedValue = resolve(context, attributeResId)
            if (typedValue == null) {
                val errorMessage = ("%1\$s requires a value for the %2\$s attribute to be set in your app theme. "
                        + "You can either set the attribute in your theme or "
                        + "update your theme to inherit from Theme.MaterialComponents (or a descendant).")
                throw java.lang.IllegalArgumentException(
                    String.format(
                        errorMessage,
                        errorMessageComponent,
                        context.resources.getResourceName(attributeResId)
                    )
                )
            }
            return typedValue.data
        }

        fun resolve(context: Context, @AttrRes attributeResId: Int): TypedValue? {
            val typedValue = TypedValue()
            return if (context.theme.resolveAttribute(attributeResId, typedValue, true)) {
                typedValue
            } else null
        }
    }
}
