package de.fklappan.app.workoutlog.domain

data class StatisticCurrentDomainModel(
    var streak: Int,
    var isWorkoutStreak: Boolean,
    var workoutCountMonth: Int,
    var workoutCountYear: Int
)