package de.fklappan.app.workoutlog.common

import android.util.Log
import de.fklappan.app.workoutlog.domain.Logger

class AndroidLoggerImpl : Logger {
    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

}