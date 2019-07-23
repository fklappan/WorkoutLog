package de.fklappan.app.workoutlog.data

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import de.fklappan.app.workoutlog.data.result.ResultDataModel
import de.fklappan.app.workoutlog.data.workout.WorkoutDataModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
open class DatabaseTest {

    private lateinit var database: AppDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java).build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun addWorkout() {
        val workout = WorkoutDataModel(0, "Aphrodite - 50/40/30/20/10: Burpees, Situps, Air Squats")
        database.workoutDao().insertWorkout(workout)

        val workoutCompare = database.workoutDao().getWorkoutList()
        assert(workoutCompare.size == 1)
    }


    @Test
    fun addResult() {
        val workout = WorkoutDataModel(0, "Aphrodite - 50/40/30/20/10: Burpees, Situps, Air Squats")
        database.workoutDao().insertWorkout(workout)

        val workoutCompare = database.workoutDao().getWorkoutList()
        for (wo in workoutCompare) {
            val result = ResultDataModel(0, "28:03", Date(), wo.workoutId, true, "note", "good feeling")
            database.resultDao().insertResult(result)

            val resultCompare = database.resultDao().getResultsForWorkout(wo.workoutId)
            assert(resultCompare.size == 1)
        }

    }
}