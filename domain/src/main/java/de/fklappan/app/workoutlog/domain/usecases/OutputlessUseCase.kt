package de.fklappan.app.workoutlog.domain.usecases

import io.reactivex.Completable

interface OutputlessUseCase <PARAMETER> {
    fun execute(param: PARAMETER) : Completable
}