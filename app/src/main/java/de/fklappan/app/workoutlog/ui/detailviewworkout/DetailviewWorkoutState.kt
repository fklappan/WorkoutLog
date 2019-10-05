package de.fklappan.app.workoutlog.ui.detailviewworkout

import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel

sealed class DetailviewWorkoutState {
    object Loading : DetailviewWorkoutState()
    data class Error(val message: String) : DetailviewWorkoutState()
    data class WorkoutDetails(val workoutDetails: WorkoutDetailsGuiModel) : DetailviewWorkoutState()
    data class WorkoutUpdate(val workout: WorkoutGuiModel) : DetailviewWorkoutState()
}