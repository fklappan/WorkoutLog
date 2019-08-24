package de.fklappan.app.workoutlog.data.result

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface ResultDao{

    @Query("SELECT * FROM result WHERE workout_id = :workoutId")
    fun getResultsForWorkout(workoutId: Int): List<ResultDataModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertResult(resultDataModel: ResultDataModel)

    @Query("SELECT * FROM result WHERE date BETWEEN :start AND :end")
    fun getResultsForPeriod(start: Date, end: Date) : List<ResultDataModel>
}