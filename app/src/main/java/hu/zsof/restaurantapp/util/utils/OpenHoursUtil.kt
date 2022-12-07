package hu.zsof.restaurantapp.util.utils

import android.content.Context
import android.graphics.Color
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import hu.zsof.restaurantapp.R
import java.util.*

object OpenHoursUtil {

    fun disableText(
        textViewOpen: TextView,
        textViewClose: TextView,
        context: Context,
        checkBox: CheckBox
    ) {
        textViewOpen.isEnabled = false
        textViewOpen.isClickable = false
        textViewOpen.setTextColor(Color.parseColor("#8D353535"))
        textViewOpen.text = context.getString(R.string.set_time)
        textViewClose.isEnabled = false
        textViewClose.isClickable = false
        textViewClose.setTextColor(Color.parseColor("#8D353535"))
        textViewClose.text = context.getString(R.string.set_time)
        checkBox.isChecked = true
    }

    fun enableText(textViewOpen: TextView, textViewClose: TextView) {
        textViewOpen.isEnabled = true
        textViewOpen.isClickable = true
        textViewOpen.setTextColor(Color.parseColor("#000000"))
        textViewClose.isEnabled = true
        textViewClose.isClickable = true
        textViewClose.setTextColor(Color.parseColor("#000000"))
    }

    fun dayOpenOrClosed(
        context: Context,
        isChecked: Boolean,
        textViewOpen: TextView,
        textViewClose: TextView,
        sameOpenHoursChecked: Boolean
    ) {
        if (isChecked) {
            if (!sameOpenHoursChecked) {
                textViewOpen.isEnabled = true
                textViewClose.isEnabled = true
                textViewOpen.isClickable = true
                textViewClose.isClickable = true
                textViewOpen.text = context.getString(R.string.set_time)
                textViewClose.text = context.getString(R.string.set_time)
                textViewOpen.setTextColor(Color.parseColor("#000000"))
                textViewClose.setTextColor(Color.parseColor("#000000"))
            } else {
                textViewOpen.isEnabled = false
                textViewClose.isEnabled = false
                textViewOpen.isClickable = false
                textViewClose.isClickable = false
                textViewOpen.text = context.getString(R.string.set_time)
                textViewClose.text = context.getString(R.string.set_time)
                textViewOpen.setTextColor(Color.parseColor("#8D353535"))
                textViewClose.setTextColor(Color.parseColor("#8D353535"))
            }
        } else {
            textViewOpen.isEnabled = false
            textViewClose.isEnabled = false
            textViewOpen.isClickable = false
            textViewClose.isClickable = false
            textViewOpen.text = context.getString(R.string.closed)
            textViewClose.text = context.getString(R.string.closed)
            textViewOpen.setTextColor(Color.parseColor("#8D353535"))
            textViewClose.setTextColor(Color.parseColor("#8D353535"))
        }
    }

    fun timePicker(textView: TextView, childFragmentManager: FragmentManager) {
        val calendar = Calendar.getInstance()
        val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
            .setTitleText("WAKE UP TIME")
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .build()

        materialTimePicker.show(childFragmentManager, "MainActivity")

        materialTimePicker.addOnPositiveButtonClickListener {
            val pickedHour = materialTimePicker.hour
            val pickedMinute = materialTimePicker.minute

            val formattedTime = when {
                pickedHour < 10 -> {
                    if (pickedMinute < 10) {
                        "0${materialTimePicker.hour}:0${materialTimePicker.minute}"
                    } else "0${materialTimePicker.hour}:${materialTimePicker.minute}"
                }
                else -> {
                    if (pickedMinute < 10) {
                        "${materialTimePicker.hour}:0${materialTimePicker.minute}"
                    } else "${materialTimePicker.hour}:${materialTimePicker.minute}"
                }
            }

            textView.text = formattedTime
        }
    }

    fun timePickerBasic(
        childFragmentManager: FragmentManager,
        basicTextView: TextView,
        mondayTextView: TextView,
        tuesdayTextView: TextView,
        wednesdayTextView: TextView,
        thursdayTextView: TextView,
        fridayTextView: TextView,
        saturdayTextView: TextView,
        sundayTextView: TextView
    ) {
        val calendar = Calendar.getInstance()
        val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
            .setTitleText("WAKE UP TIME")
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .build()

        materialTimePicker.show(childFragmentManager, "MainActivity")

        materialTimePicker.addOnPositiveButtonClickListener {
            val pickedHour = materialTimePicker.hour
            val pickedMinute = materialTimePicker.minute

            val formattedTime = when {
                pickedHour < 10 -> {
                    if (pickedMinute < 10) {
                        "0${materialTimePicker.hour}:0${materialTimePicker.minute}"
                    } else "0${materialTimePicker.hour}:${materialTimePicker.minute}"
                }
                else -> {
                    if (pickedMinute < 10) {
                        "${materialTimePicker.hour}:0${materialTimePicker.minute}"
                    } else "${materialTimePicker.hour}:${materialTimePicker.minute}"
                }
            }

            basicTextView.text = formattedTime
            mondayTextView.text = formattedTime
            tuesdayTextView.text = formattedTime
            wednesdayTextView.text = formattedTime
            thursdayTextView.text = formattedTime
            fridayTextView.text = formattedTime
            saturdayTextView.text = formattedTime
            sundayTextView.text = formattedTime
        }
    }
}
