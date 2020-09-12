package de.fklappan.app.workoutlog.ui.editresult

import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import java.util.*

sealed class EditResultState {
    object Loading : EditResultState()
    data class Error(val message: String) : EditResultState()
    data class Data(val result: WorkoutResultGuiModel) : EditResultState()
    object ResultSaved : EditResultState()
    data class DateSelected(val date: Date) : EditResultState()
    data class PrChanged(val isPr: Boolean) : EditResultState()
}