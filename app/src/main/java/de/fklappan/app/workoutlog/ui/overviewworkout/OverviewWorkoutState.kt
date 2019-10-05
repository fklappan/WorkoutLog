package de.fklappan.app.workoutlog.ui.overviewworkout

sealed class OverviewWorkoutState {
    object Loading : OverviewWorkoutState()
    data class Error(val message: String) : OverviewWorkoutState()
    data class WorkoutList(val workouts: List<WorkoutGuiModel>) : OverviewWorkoutState()
    data class WorkoutUpdate(val workout: WorkoutGuiModel) : OverviewWorkoutState()
}