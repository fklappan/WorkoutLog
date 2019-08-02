package de.fklappan.app.workoutlog.data.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.fklappan.app.workoutlog.data.workout.WorkoutDataModel
import java.util.*

@Entity(tableName = "tag", foreignKeys =[
    ForeignKey(entity = WorkoutDataModel::class, parentColumns = ["workout_id"], childColumns = ["workout_id"], onDelete = ForeignKey.CASCADE)])
data class TagDataModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="tag_id", index = true) var tagId: Int,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "workout_id") var workout: Int
)