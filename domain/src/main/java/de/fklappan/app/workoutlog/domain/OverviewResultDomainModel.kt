package de.fklappan.app.workoutlog.domain

data class OverviewResultDomainModel(
    val workout: WorkoutDomainModel,
    var result: WorkoutResultDomainModel
)