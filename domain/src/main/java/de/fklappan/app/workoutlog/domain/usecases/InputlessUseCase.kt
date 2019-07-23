package de.fklappan.app.workoutlog.domain.usecases

import io.reactivex.Completable
import io.reactivex.Single

interface InputlessUseCase <RETURN> {
    fun execute() : Single<RETURN>
}