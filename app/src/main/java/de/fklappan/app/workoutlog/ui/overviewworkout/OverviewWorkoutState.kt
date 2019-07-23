package de.fklappan.app.workoutlog.ui.overviewworkout

data class OverviewWorkoutState (
    val workoutList : MutableList<WorkoutGuiModel>,
    val error: Throwable?,
    val loading: Boolean
)