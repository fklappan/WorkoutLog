package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.*
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

class GetStatisticUseCase(val repository: WorkoutLogRepository) : InputlessUseCase<StatisticCurrentDomainModel> {

    override fun execute(): Single<StatisticCurrentDomainModel> {

        val curYearStart = getYearBegin(Calendar.getInstance())
        val curYearEnd = getYearEnd(Calendar.getInstance())

        val resultYearList = repository.getResultsForPeriod(curYearStart.time, curYearEnd.time)

        val curMonthStart = getMonthBegin(Calendar.getInstance())
        val curMonthEnd = getMonthEnd(Calendar.getInstance())
        val resultMonthList = repository.getResultsForPeriod(curMonthStart.time, curMonthEnd.time)

        val monthCount = filterUniqueDailyWorkout(resultMonthList)
        val yearCount = filterUniqueDailyWorkout(resultYearList)

        // check for streak or rest days
        val consecutiveDays = getConsecutiveTrainingDays(resultMonthList, Calendar.getInstance())
        var consecutiveRestDays = 0
        if (consecutiveDays == 0) {
            consecutiveRestDays = getConsecutiveRestDays(resultMonthList, Calendar.getInstance())
            val statisticModel = StatisticCurrentDomainModel(consecutiveRestDays, false, monthCount, yearCount)
            return Single.just(statisticModel)
        }

        val statisticModel = StatisticCurrentDomainModel(consecutiveDays, true, monthCount, yearCount)
        return Single.just(statisticModel)
    }

    private fun getMonthBegin(date: Calendar) : Calendar {
        with(date) {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        return date
    }

    private fun getMonthEnd(date: Calendar) : Calendar {
        with(date) {
            set(Calendar.DAY_OF_MONTH, 31)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
        return date
    }

    private fun getYearBegin(date: Calendar) : Calendar {
        date.set(Calendar.MONTH, Calendar.JANUARY)
        getMonthBegin(date)
        return date
    }

    private fun getYearEnd(date: Calendar) : Calendar {
        date.set(Calendar.MONTH, Calendar.DECEMBER)
        getMonthEnd(date)
        return date
    }

    private fun filterUniqueDailyWorkout(resultDomainList: List<WorkoutResultDomainModel>): Int {
        val set = HashSet<String>()

        for (result in resultDomainList) {
            val dateString = SimpleDateFormat("ddMMyyyy").format(result.date)
            set.add(dateString)
        }
        return set.size
    }

    private fun getConsecutiveTrainingDays(workoutMonthList: List<WorkoutResultDomainModel>, fromDay: Calendar): Int {

        val set = HashSet<String>()
        for (result in workoutMonthList) {
            val dateString = SimpleDateFormat("ddMMyyyy").format(result.date)
            set.add(dateString)
        }

        val searchMonth = fromDay.get(Calendar.MONTH)
        var currentMonth = searchMonth

        var trainingDays = 0
        val startDay = SimpleDateFormat("ddMMyyyy").format(fromDay.time)

        while(currentMonth == searchMonth) {
            val dateString = SimpleDateFormat("ddMMyyyy").format(fromDay.time)

            if (set.contains(dateString)) {
                trainingDays++
            } else {
                // dont stop today, look at least at yesterday (it is still possible to make a workout today ;-))
                if (dateString != startDay) {
                    return trainingDays
                }
            }

            fromDay.add(Calendar.DAY_OF_MONTH, -1)
            currentMonth = fromDay.get(Calendar.MONTH)
        }
        return trainingDays
    }

    private fun getConsecutiveRestDays(workoutMonthList: List<WorkoutResultDomainModel>, fromDay: Calendar): Int {

        // TODO 23.08.2019 Flo extract a method
        val set = HashSet<String>()
        for (result in workoutMonthList) {
            val dateString = SimpleDateFormat("ddMMyyyy").format(result.date)
            set.add(dateString)
        }

        val searchMonth = fromDay.get(Calendar.MONTH)
        var currentMonth = searchMonth

        var restDays = 0
        val startDay = SimpleDateFormat("ddMMyyyy").format(fromDay.time)

        while (currentMonth == searchMonth) {
            val dateString = SimpleDateFormat("ddMMyyyy").format(fromDay.time)

            if (set.contains(dateString)) {
                return restDays
            } else {
                // dont count today, because it is possible to still make a workout
                if (dateString != startDay) {
                    restDays++
                }
            }

            fromDay.add(Calendar.DAY_OF_MONTH, -1)
            currentMonth = fromDay.get(Calendar.MONTH)
        }
        return restDays
    }
}