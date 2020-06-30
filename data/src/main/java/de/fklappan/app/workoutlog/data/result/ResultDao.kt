package de.fklappan.app.workoutlog.data.result

import androidx.room.*
import java.util.*

@Dao
interface ResultDao{

    @Query("SELECT * FROM result WHERE workout_id = :workoutId")
    fun getResultsForWorkout(workoutId: Int): List<ResultDataModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertResult(resultDataModel: ResultDataModel)

    @Query("SELECT * FROM result WHERE date BETWEEN :start AND :end")
    fun getResultsForPeriod(start: Date, end: Date) : List<ResultDataModel>

    @Query("SELECT * FROM result WHERE result_id = :resultId")
    fun getResultById(resultId: Int): ResultDataModel

    @Delete
    fun deleteResult(resultDataModel: ResultDataModel)
}