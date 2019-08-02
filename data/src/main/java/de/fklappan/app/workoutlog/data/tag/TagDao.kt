package de.fklappan.app.workoutlog.data.tag

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TagDao{

    @Query("SELECT * FROM tag")
    fun getTags(): List<TagDataModel>

    @Query("SELECT * FROM tag WHERE workout_id = :workoutId")
    fun getTagsForWorkout(workoutId: Int): List<TagDataModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertTag(tagDataModel: TagDataModel)
}