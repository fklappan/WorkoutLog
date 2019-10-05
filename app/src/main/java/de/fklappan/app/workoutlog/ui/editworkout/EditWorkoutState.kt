package de.fklappan.app.workoutlog.ui.editworkout

import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel

sealed class EditWorkoutState {
    object Loading : EditWorkoutState()
    data class Error(val message: String) : EditWorkoutState()
    data class Workout(val workout: WorkoutGuiModel) : EditWorkoutState()
    object Save : EditWorkoutState()
}