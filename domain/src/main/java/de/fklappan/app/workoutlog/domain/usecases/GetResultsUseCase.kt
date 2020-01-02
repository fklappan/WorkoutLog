package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.*
import io.reactivex.Single
import java.util.*

class GetResultsUseCase(val repository: WorkoutLogRepository) : UseCase<Calendar, List<OverviewResultDomainModel>> {

    override fun execute(startDay : Calendar): Single<List<OverviewResultDomainModel>> {

        // limited to current year
        val retList : LinkedList<OverviewResultDomainModel> = LinkedList()
        val curYearStart = startDay.getYearBegin()

        val resultYearList = repository.getResultsForPeriod(curYearStart.time, startDay.time).sortedByDescending { it.date.time }

        for (result in resultYearList) {
            val resultDomainModel = OverviewResultDomainModel(
                repository.getWorkoutById(result.workout),
                result)

            retList.add(resultDomainModel)
        }
        return Single.just(retList)
    }
}