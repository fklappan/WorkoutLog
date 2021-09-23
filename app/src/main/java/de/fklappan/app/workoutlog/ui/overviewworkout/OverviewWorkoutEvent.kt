package de.fklappan.app.workoutlog.ui.overviewworkout

/**
 * One time events to be handled by the view
 */
sealed class OverviewWorkoutEvent {
    object None : OverviewWorkoutEvent()
    data class Error(val message: String) : OverviewWorkoutEvent()
    data class ErrorLocalized(val resourceId: Int) : OverviewWorkoutEvent()
    data class WorkoutUpdate(val workout: WorkoutGuiModel) : OverviewWorkoutEvent()
    data class WorkoutNavigate(val workout: WorkoutGuiModel) : OverviewWorkoutEvent()
}