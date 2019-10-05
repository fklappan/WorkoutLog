package de.fklappan.app.workoutlog.ui.addworkout


sealed class AddWorkoutState {
    data class Error(val message: String) : AddWorkoutState()
    object Save : AddWorkoutState()
}