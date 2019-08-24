package de.fklappan.app.workoutlog

import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutsUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.observers.TestObserver
import org.junit.Test

class TestGetWorkoutsUseCase {

    private val repository = mockk<WorkoutLogRepository>()

    @Test
    fun shouldReturnEmptyList() {
        // prepare test
        every { repository.getWorkouts() } returns ArrayList()
        val uut = GetWorkoutsUseCase(repository)

        val bla : TestObserver<List<WorkoutDomainModel>> = uut.execute().test()
        bla.awaitTerminalEvent()

        bla.assertNoErrors()
        bla.assertValue { list -> list.isEmpty() }
    }

    @Test
    fun shouldReturnElement() {
        // prepare testdata
        val testData = ArrayList<WorkoutDomainModel>()
        testData.add(WorkoutDomainModel(3, "Workout", true))
        every { repository.getWorkouts() } returns testData
        val uut = GetWorkoutsUseCase(repository)

        val bla : TestObserver<List<WorkoutDomainModel>> = uut.execute().test()
        bla.awaitTerminalEvent()

        bla.assertNoErrors()
        bla.assertValue { list -> list.size == 1 }
    }

}
