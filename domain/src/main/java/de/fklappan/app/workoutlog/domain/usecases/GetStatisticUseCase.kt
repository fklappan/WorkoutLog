package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.StatisticCurrentDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.max

// TODO 08.09.2019 Flo move SimpleDateFormat to Date extension
class GetStatisticUseCase(val repository: WorkoutLogRepository) : UseCase<Calendar, StatisticCurrentDomainModel> {

    override fun execute(startDay : Calendar): Single<StatisticCurrentDomainModel> {

        val curYearStart = getYearBegin(startDay.clone() as Calendar)
        val curYearEnd = getYearEnd(startDay.clone() as Calendar)
        val resultYearList = repository.getResultsForPeriod(curYearStart.time, curYearEnd.time)

        val curMonthStart = getMonthBegin(startDay.clone() as Calendar)
        val curMonthEnd = getMonthEnd(startDay.clone() as Calendar)
        val resultMonthList = repository.getResultsForPeriod(curMonthStart.time, curMonthEnd.time)

        val monthCount = filterUniqueDailyWorkout(resultMonthList)
        val yearCount = filterUniqueDailyWorkout(resultYearList)

        // check for streak or rest days
        val consecutiveDaysFromToday = getConsecutiveTrainingDays(resultMonthList, startDay.clone() as Calendar)
        val yesterday = startDay.clone() as Calendar
        yesterday.add(Calendar.DAY_OF_MONTH, -1)

        val consecutiveDaysFromYesterday = getConsecutiveTrainingDays(resultMonthList, yesterday.clone() as Calendar)
        val consecutiveDays = max(consecutiveDaysFromToday, consecutiveDaysFromYesterday)

        if (consecutiveDays == 0) {
            // rest days are always calculated from yesterday, because it is still possible to make
            // a workout today
            val consecutiveRestDays = getConsecutiveRestDays(resultMonthList, yesterday)
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
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
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
        // move calendar from fromDay backwards using the Date as identifier (converted to string)
        // till there is a gap between days
        val set = createDateAsStringSet(workoutMonthList)

        var currentDayString = SimpleDateFormat("ddMMyyyy").format(fromDay.time)
        var trainingDays = 0

        while(set.contains(currentDayString)) {
            trainingDays++

            fromDay.add(Calendar.DAY_OF_MONTH, -1)
            currentDayString = SimpleDateFormat("ddMMyyyy").format(fromDay.time)
        }

        return trainingDays
    }

    private fun createDateAsStringSet(workoutMonthList: List<WorkoutResultDomainModel>): HashSet<String> {
        val set = HashSet<String>()
        for (result in workoutMonthList) {
            val dateString = SimpleDateFormat("ddMMyyyy").format(result.date)
            set.add(dateString)
        }
        return set
    }

    private fun getConsecutiveRestDays(workoutMonthList: List<WorkoutResultDomainModel>, fromDay: Calendar): Int {
        // move calendar from fromDay backwards using the Date as identifier (converted to string)
        // till there is a workout entry in the set (or 365 days are passed)
        val set = createDateAsStringSet(workoutMonthList)

        var currentDayString = SimpleDateFormat("ddMMyyyy").format(fromDay.time)
        var restDays = 0

        while(!set.contains(currentDayString)) {
            restDays++

            fromDay.add(Calendar.DAY_OF_MONTH, -1)
            currentDayString = SimpleDateFormat("ddMMyyyy").format(fromDay.time)
            if (restDays >= 365) {
                break
            }
        }

        return restDays
    }
}