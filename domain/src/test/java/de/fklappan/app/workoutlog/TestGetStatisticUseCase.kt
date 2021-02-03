package de.fklappan.app.workoutlog

import com.sun.org.apache.bcel.internal.generic.CALOAD
import de.fklappan.app.workoutlog.domain.StatisticCurrentDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import de.fklappan.app.workoutlog.domain.usecases.GetStatisticUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.observers.TestObserver
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

class TestGetStatisticUseCase {

    private val repository = mockk<WorkoutLogRepository>()

    // creates 3 consecutive training days, starting from given calendar
    private fun createConsecutiveWorkoutDays(calendar: Calendar) : List<WorkoutResultDomainModel>{
        val testData = ArrayList<WorkoutResultDomainModel>()

        val today = WorkoutResultDomainModel(1, "today", calendar.time, 1, true, "note", "feeling")
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val yesterday = WorkoutResultDomainModel(2, "yesterday", calendar.time, 1, true, "note", "feeling")
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val beforeYesterday = WorkoutResultDomainModel(3, "day before yesterday", calendar.time, 1, true, "note", "feeling")

        testData.add(today)
        testData.add(yesterday)
        testData.add(beforeYesterday)
        return testData
    }

    // creates a workout two days ago
    private fun createRestDays() : List<WorkoutResultDomainModel>{
        val testData = ArrayList<WorkoutResultDomainModel>()

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -2)

        val beforeYesterday = WorkoutResultDomainModel(3, "day before yesterday", calendar.time, 1, true, "note", "feeling")
        testData.add(beforeYesterday)
        return testData
    }

    @Test
    fun shouldHaveConsecutiveTrainingDaysFromToday() {
        // creates consecutive training days from today on
        every { repository.getResultsForPeriod(any(), any()) } returns createConsecutiveWorkoutDays(Calendar.getInstance())
        val uut = GetStatisticUseCase(repository)

        val bla : TestObserver<StatisticCurrentDomainModel> = uut.execute(Calendar.getInstance()).test()
        bla.awaitTerminalEvent()

        bla.assertNoErrors()
        bla.assertValue { statistics -> statistics.streak == 3 }
        bla.assertValue { statistics -> statistics.isWorkoutStreak }
        // as TODAY is the origin it is possible if today is the first of the month, we only have one workout day this month
        bla.assertValue { statistics -> statistics.workoutCountMonth >= 1 }
        bla.assertValue { statistics -> statistics.workoutCountYear == 3 }

        verify { repository.getResultsForPeriod(any(), any()) }
    }

    @Test
    fun shouldHaveConsecutiveTrainingDaysFromYesterday() {
        // creates consecutive training days from yesterday on
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_MONTH, -1)
        every { repository.getResultsForPeriod(any(), any()) } returns createConsecutiveWorkoutDays(yesterday)
        val uut = GetStatisticUseCase(repository)

        val bla : TestObserver<StatisticCurrentDomainModel> = uut.execute(Calendar.getInstance()).test()
        bla.awaitTerminalEvent()

        bla.assertNoErrors()
        bla.assertValue { statistics -> statistics.streak == 3 }
        bla.assertValue { statistics -> statistics.isWorkoutStreak }
        // as TODAY is the origin it is possible if today is the first of the month, we only have one workout day this month
        bla.assertValue { statistics -> statistics.workoutCountMonth >= 1 }
        bla.assertValue { statistics -> statistics.workoutCountYear == 3 }

        verify { repository.getResultsForPeriod(any(), any()) }
    }

    @Test
    fun shouldHaveRestDay() {
        // prepare test
        every { repository.getResultsForPeriod(any(), any()) } returns createRestDays()
        val uut = GetStatisticUseCase(repository)

        val bla : TestObserver<StatisticCurrentDomainModel> = uut.execute(Calendar.getInstance()).test()
        bla.awaitTerminalEvent()

        bla.assertNoErrors()
        bla.assertValue { statistics -> statistics.streak == 1 }
        bla.assertValue { statistics -> !statistics.isWorkoutStreak }
        bla.assertValue { statistics -> statistics.workoutCountMonth == 1 }
        bla.assertValue { statistics -> statistics.workoutCountYear == 1 }

        verify { repository.getResultsForPeriod(any(), any()) }
    }

    // deactivated for now because the test is garbage :D
    // the use case under test gets always the same values from the mocked repository. this does not
    // reflect the real world scenario, where the use case would get all results of the year and all
    // results of the month
/*    @Test
    fun `consecutive days into last month`() {
        // create first day of month
        val firstDayOfMonth = Calendar.getInstance()
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
        println(firstDayOfMonth.toPrettyString())
        val dayOfLastMonth = firstDayOfMonth.clone() as Calendar
        dayOfLastMonth.add(Calendar.DAY_OF_MONTH, -1)
        println(dayOfLastMonth.toPrettyString())

        every { repository.getResultsForPeriod(any(), any()) } returns createConsecutiveWorkoutDays(firstDayOfMonth.clone() as Calendar)
        val uut = GetStatisticUseCase(repository)

        val bla : TestObserver<StatisticCurrentDomainModel> = uut.execute(firstDayOfMonth).test()
        bla.awaitTerminalEvent()
        bla.assertNoErrors()
        bla.assertValue { statistics -> statistics.streak == 3 }
    }*/

    @Test
    fun `no workout in current but in last month`() {

        val today = Calendar.getInstance()
        today.set(Calendar.MONTH, 1)
        today.set(Calendar.DAY_OF_MONTH, 2)
        today.set(Calendar.YEAR, 2020)

        val lastMonthDay = today.clone() as Calendar
        lastMonthDay.set(Calendar.MONTH, 0)
        lastMonthDay.set(Calendar.DAY_OF_MONTH, 15)

        // create first day of month
        val lastMonth = Calendar.getInstance()
        lastMonth.set(Calendar.DAY_OF_MONTH, 1)
        lastMonth.add(Calendar.DAY_OF_MONTH, -1)

        val workout = WorkoutResultDomainModel(1, "today", lastMonthDay.time, 1, true, "note", "feeling")
        every { repository.getResultsForPeriod(any(), any()) } returns Collections.singletonList(workout)
        val uut = GetStatisticUseCase(repository)

        val bla : TestObserver<StatisticCurrentDomainModel> = uut.execute(today).test()
        bla.awaitTerminalEvent()
        bla.assertNoErrors()
        bla.assertValue { statistics -> !statistics.isWorkoutStreak }
    }

    @Test
    fun `rest day edge case - never done a workout`() {

        val emptyWorkoutList = ArrayList<WorkoutResultDomainModel>()
        every { repository.getResultsForPeriod(any(), any()) } returns emptyWorkoutList

        val uut = GetStatisticUseCase(repository)

        val bla : TestObserver<StatisticCurrentDomainModel> = uut.execute(Calendar.getInstance()).test()
        bla.awaitTerminalEvent()
        bla.assertNoErrors()
        bla.assertValue { statistics -> statistics.streak == 365 }
        bla.assertValue { statistics -> !statistics.isWorkoutStreak }
    }
}
