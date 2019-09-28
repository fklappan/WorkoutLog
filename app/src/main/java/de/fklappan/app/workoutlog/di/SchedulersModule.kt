package de.fklappan.app.workoutlog.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

const val SCHEDULER_MAINTHREAD = "SCHEDULER_MAINTHREAD"
const val SCHEDULER_IO = "SCHEDULER_IO"

/**
 * Module for providing RxScheduler instances
 */
@Module
class SchedulersModule {

    @Provides
    @Named(SCHEDULER_IO)
    fun provideSchedulerIo() : Scheduler = Schedulers.io()

    @Provides
    @Named(SCHEDULER_MAINTHREAD)
    fun provideSchedulerMainThread() : Scheduler = AndroidSchedulers.mainThread()

}