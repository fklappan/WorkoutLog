package de.fklappan.app.workoutlog.domain

import java.util.*

data class WorkoutResultDomainModel(
    var resultId: Int,
    var score: String,
    var date: Date,
    var workout: Int,
    var pr: Boolean,
    var note: String,
    var feeling: String)
//    var workoutId: Int,
//    var text: String)