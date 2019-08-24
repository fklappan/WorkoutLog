package de.fklappan.app.workoutlog.ui.overviewstatistic

data class StatisticCurrentGuiModel(
    // streak is whether workout streak or rest day "streak"
    var streak: Int,
    var isWorkoutStreak: Boolean,
    var workoutCountMonth: Int,
    var workoutCountYear: Int
)