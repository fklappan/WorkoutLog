package de.fklappan.app.workoutlog.domain

public interface WorkoutLogRepository {

    fun getWorkouts(): List<WorkoutDomainModel>

    fun getWorkoutById(workoutId: Int) : WorkoutDomainModel

    fun getResultsForWorkout(workoutId: Int) : List<WorkoutResultDomainModel>

    fun addWorkout(workoutDomainModel: WorkoutDomainModel)

    fun addResult(workoutId: Int, result: WorkoutResultDomainModel)

    fun updateWorkout(workoutDomainModel: WorkoutDomainModel)

    fun getTags(): List<TagDomainModel>

}