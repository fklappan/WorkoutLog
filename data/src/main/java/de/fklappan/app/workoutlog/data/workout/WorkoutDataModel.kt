package de.fklappan.app.workoutlog.data.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout")
data class WorkoutDataModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id", index = true) var workoutId: Int,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "favorite") var favorite: Boolean
)