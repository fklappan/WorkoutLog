package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.*
import io.reactivex.Single
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.max

class GetStatisticUseCase(val repository: WorkoutLogRepository) : UseCase<Calendar, StatisticCurrentDomainModel> {

    override fun execute(startDay : Calendar): Single<StatisticCurrentDomainModel> {

        val curYearStart = startDay.getYearBegin()
        val curYearEnd = startDay.getYearEnd()
        val resultYearList = repository.getResultsForPeriod(curYearStart.time, curYearEnd.time)

        val curMonthStart = startDay.getMonthBegin()
        val curMonthEnd = startDay.getMonthEnd()
        val resultMonthList = repository.getResultsForPeriod(curMonthStart.time, curMonthEnd.time)

        val monthCount = filterUniqueDailyWorkout(resultMonthList)
        val yearCount = filterUniqueDailyWorkout(resultYearList)

        // check for streak or rest days
        val consecutiveDaysFromToday = getConsecutiveTrainingDays(resultYearList, startDay.clone() as Calendar)
        val yesterday = startDay.clone() as Calendar
        yesterday.add(Calendar.DAY_OF_MONTH, -1)

        val consecutiveDaysFromYesterday = getConsecutiveTrainingDays(resultYearList, yesterday.clone() as Calendar)
        val consecutiveDays = max(consecutiveDaysFromToday, consecutiveDaysFromYesterday)

        if (consecutiveDays == 0) {
            // rest days are always calculated from yesterday, because it is still possible to make
            // a workout today
            val consecutiveRestDays = getConsecutiveRestDays(resultYearList, yesterday)
            val statisticModel = StatisticCurrentDomainModel(consecutiveRestDays, false, monthCount, yearCount)
            return Single.just(statisticModel)
        }

        val statisticModel = StatisticCurrentDomainModel(consecutiveDays, true, monthCount, yearCount)
        return Single.just(statisticModel)
    }

    private fun filterUniqueDailyWorkout(resultDomainList: List<WorkoutResultDomainModel>): Int {
        val set = HashSet<String>()

        for (result in resultDomainList) {
            val dateString = result.date.toEqualizableString()
            set.add(dateString)
        }
        return set.size
    }

    private fun getConsecutiveTrainingDays(workoutMonthList: List<WorkoutResultDomainModel>, fromDay: Calendar): Int {
        // move calendar from fromDay backwards using the Date as identifier (converted to string)
        // till there is a gap between days
        val set = createDateAsStringSet(workoutMonthList)

        var currentDayString = fromDay.time.toEqualizableString()
        var trainingDays = 0

        while(set.contains(currentDayString)) {
            trainingDays++

            fromDay.add(Calendar.DAY_OF_MONTH, -1)
            currentDayString = fromDay.time.toEqualizableString()
        }

        return trainingDays
    }

    private fun createDateAsStringSet(workoutMonthList: List<WorkoutResultDomainModel>): HashSet<String> {
        val set = HashSet<String>()
        for (result in workoutMonthList) {
            val dateString = result.date.toEqualizableString()
            set.add(dateString)
        }
        return set
    }

    private fun getConsecutiveRestDays(workoutYearList: List<WorkoutResultDomainModel>, fromDay: Calendar): Int {
        // move calendar from fromDay backwards using the Date as identifier (converted to string)
        // till there is a workout entry in the set (or 365 days are passed)
        val set = createDateAsStringSet(workoutYearList)

        var currentDayString = fromDay.time.toEqualizableString()
        var restDays = 0

        while(!set.contains(currentDayString)) {
            restDays++

            fromDay.add(Calendar.DAY_OF_MONTH, -1)
            currentDayString = fromDay.time.toEqualizableString()
            if (restDays >= 365) {
                break
            }
        }

        return restDays
    }
}