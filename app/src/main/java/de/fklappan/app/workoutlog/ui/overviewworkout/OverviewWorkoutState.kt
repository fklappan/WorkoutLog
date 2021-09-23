package de.fklappan.app.workoutlog.ui.overviewworkout

/**
 * States used by the view. Kind of "persistent" as they should be re-emitted after config change
 */
sealed class OverviewWorkoutState {
    object Loading : OverviewWorkoutState()
    data class WorkoutList(val workouts: List<WorkoutGuiModel>) : OverviewWorkoutState()
}