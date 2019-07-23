package de.fklappan.app.workoutlog.domain

data class WorkoutDetailsDomainModel(var workout: WorkoutDomainModel, var resultList: List<WorkoutResultDomainModel>)