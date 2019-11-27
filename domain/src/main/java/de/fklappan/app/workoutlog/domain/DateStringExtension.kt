package de.fklappan.app.workoutlog.domain

import java.text.SimpleDateFormat
import java.util.*

fun Date.toPrettyString(): String = SimpleDateFormat("dd.MM.yyyy").format(this)
fun Date.toEqualizableString() : String = SimpleDateFormat("ddMMyyyy").format(this)
