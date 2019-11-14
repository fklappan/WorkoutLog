package de.fklappan.app.workoutlog.ui.addresult

import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutDetailsGuiModel


sealed class AddResultState {
    object Loading : AddResultState()
    data class Error(val message: String) : AddResultState()
    data class WorkoutDetails(val workoutDetails: WorkoutDetailsGuiModel) : AddResultState()
    object Save : AddResultState()
}