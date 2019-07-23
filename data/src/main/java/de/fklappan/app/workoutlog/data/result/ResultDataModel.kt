package de.fklappan.app.workoutlog.data.result

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.fklappan.app.workoutlog.data.workout.WorkoutDataModel
import java.util.*

@Entity(tableName = "result", foreignKeys =[
    ForeignKey(entity = WorkoutDataModel::class, parentColumns = ["workout_id"], childColumns = ["workout_id"], onDelete = ForeignKey.CASCADE)])
data class ResultDataModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="result_id", index = true) var resultId: Int,
    @ColumnInfo(name = "score") var score: String,
    @ColumnInfo(name = "date") var date: Date,
    @ColumnInfo(name = "workout_id") var workout: Int,
    @ColumnInfo(name = "ispr") var pr: Boolean,
    @ColumnInfo(name = "note") var note: String,
    @ColumnInfo(name = "feeling") var feeling: String
)