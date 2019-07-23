package de.fklappan.app.workoutlog.data.result

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ResultDao{

    @Query("SELECT * FROM result WHERE workout_id = :workoutId")
    fun getResultsForWorkout(workoutId: Int): List<ResultDataModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertResult(resultDataModel: ResultDataModel)
}