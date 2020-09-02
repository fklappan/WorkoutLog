package de.fklappan.app.workoutlog.ui.detailviewresult

import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel

sealed class DetailviewResultState {
    object Loading : DetailviewResultState()
    data class Error(val message: String) : DetailviewResultState()
    data class ResultDetails(val resultDetails: WorkoutResultGuiModel) : DetailviewResultState()
    data class ResultUpdate(val result: WorkoutResultGuiModel) : DetailviewResultState()
}