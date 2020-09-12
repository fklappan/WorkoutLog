package de.fklappan.app.workoutlog.domain

import java.util.*

public interface WorkoutLogRepository {

    fun getWorkouts(): List<WorkoutDomainModel>

    fun getWorkoutById(workoutId: Int) : WorkoutDomainModel

    fun getResultById(resultId: Int) : WorkoutResultDomainModel

    fun getResultsForWorkout(workoutId: Int) : List<WorkoutResultDomainModel>

    fun addWorkout(workoutDomainModel: WorkoutDomainModel)

    fun addResult(workoutId: Int, result: WorkoutResultDomainModel)

    fun updateWorkout(workoutDomainModel: WorkoutDomainModel)

    fun updateResult(resultDomainModel: WorkoutResultDomainModel)

    fun getResultsForPeriod(start: Date, end: Date) : List<WorkoutResultDomainModel>

    fun deleteWorkout(workoutId: Int)

    fun deleteResult(resultId: Int)
}