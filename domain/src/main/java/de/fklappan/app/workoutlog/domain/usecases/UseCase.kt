package de.fklappan.app.workoutlog.domain.usecases

import io.reactivex.Single

interface UseCase <PARAMETER, RETURN> {
    fun execute(param: PARAMETER): Single<RETURN>
}