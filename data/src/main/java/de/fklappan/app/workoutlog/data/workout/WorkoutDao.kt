package de.fklappan.app.workoutlog.data.workout

import androidx.room.*

@Dao
interface WorkoutDao{

    @Query("SELECT * FROM workout")
    fun getWorkoutList(): List<WorkoutDataModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertWorkout(workoutDataModel: WorkoutDataModel)

    @Query("SELECT * FROM workout WHERE workout_id = :workoutId")
    fun getWorkoutById(workoutId: Int): WorkoutDataModel

    @Update
    fun updateWorkout(workoutDataModel: WorkoutDataModel)

    @Delete
    fun deleteWorkout(workoutDataModel: WorkoutDataModel)
}