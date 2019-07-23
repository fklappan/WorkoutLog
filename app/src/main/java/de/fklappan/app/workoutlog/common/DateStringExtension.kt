package de.fklappan.app.workoutlog.common

import java.text.SimpleDateFormat
import java.util.*

fun Date.toPrettyString() : String = SimpleDateFormat("dd.MM.yyyy").format(this)
