package de.fklappan.app.workoutlog.domain

interface Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String)
    fun w(tag: String, message: String)
}