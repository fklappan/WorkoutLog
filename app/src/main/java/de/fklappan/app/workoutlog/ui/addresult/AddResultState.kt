package de.fklappan.app.workoutlog.ui.addresult

import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutDetailsGuiModel
import java.util.*


sealed class AddResultState {
    object Loading : AddResultState()
    data class Error(val message: String) : AddResultState()
    data class Data(val workoutDetails: WorkoutDetailsGuiModel, val date:Date, val isPr: Boolean) : AddResultState()
    object Save : AddResultState()
    data class DateSelected(val date: Date) : AddResultState()
    data class PrChanged(val isPr: Boolean) : AddResultState()
}