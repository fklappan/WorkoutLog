package de.fklappan.app.workoutlog.domain

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.getYearBegin() : Calendar  {
    val ret = this.clone() as Calendar
    with(ret) {
        set(Calendar.MONTH, Calendar.JANUARY)
    }
    val dateString = ret.time.toPrettyString()
    return ret.getMonthBegin()
}

fun Calendar.getMonthBegin() : Calendar {
    val ret = this.clone() as Calendar
    with(ret) {
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    val dateString = ret.time.toPrettyString()
    return ret
}

fun Calendar.getYearEnd() : Calendar  {
    val ret = this.clone() as Calendar
    with(ret) {
        set(Calendar.MONTH, Calendar.DECEMBER)
    }
    return getMonthEnd()
}

fun Calendar.setYesterday() {
    this.set(Calendar.DAY_OF_YEAR, this.get(Calendar.DAY_OF_YEAR) - 1)
}

fun Calendar.getMonthEnd() : Calendar {
    val ret = this.clone() as Calendar
    with(ret) {
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
    }
    return ret
}

fun Calendar.isSameDay(calendar: Calendar) : Boolean {
    return (this.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) &&
            (this.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) &&
            (this.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))
}

fun Calendar.toPrettyString(): String = SimpleDateFormat("dd.MM.yyyy").format(time)