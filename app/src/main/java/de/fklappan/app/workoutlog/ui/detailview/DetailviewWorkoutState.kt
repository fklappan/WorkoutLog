package de.fklappan.app.workoutlog.ui.detailview

data class DetailviewWorkoutState (
    val workout : WorkoutDetailsGuiModel?,
    val error: Throwable?,
    val loading: Boolean
)