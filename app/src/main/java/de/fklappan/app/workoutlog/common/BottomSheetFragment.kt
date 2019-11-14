package de.fklappan.app.workoutlog.common

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class BottomSheetFragment(private val dateSetListener: (DatePicker, Int, Int, Int) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        return DatePickerDialog(requireContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }



}