package de.fklappan.app.workoutlog.common

val Any.LOG_TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }