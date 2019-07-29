package de.fklappan.app.workoutlog.ui.detailview

import java.util.*

data class WorkoutResultGuiModel(
    var resultId: Int,
    var score: String,
    var date: Date,
    var workout: Int,
    var pr: Boolean,
    var note: String,
    var feeling: String
)