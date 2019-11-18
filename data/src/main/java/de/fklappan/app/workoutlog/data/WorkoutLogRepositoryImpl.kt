package de.fklappan.app.workoutlog.data

import de.fklappan.app.workoutlog.data.result.ResultDao
import de.fklappan.app.workoutlog.data.workout.WorkoutDao
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//class WorkoutLogRepositoryImpl(val workoutDao: WorkoutDao) : WorkoutLogRepository {
class WorkoutLogRepositoryImpl(private val workoutDao: WorkoutDao, private val resultDao: ResultDao) : WorkoutLogRepository {


    private val modelMapper = ModelMapper()

    override fun getWorkoutById(workoutId: Int): WorkoutDomainModel {
        return modelMapper.mapDataToDomain(workoutDao.getWorkoutById(workoutId))
    }

    override fun getResultsForWorkout(workoutId: Int): List<WorkoutResultDomainModel> {
        val domainModelList = ArrayList<WorkoutResultDomainModel>()

        val dataModelList = resultDao.getResultsForWorkout(workoutId)

        for (dataModel in dataModelList) {
            domainModelList.add(modelMapper.mapDataToDomain(dataModel))
        }
        return domainModelList
    }

    override fun getResultsForPeriod(start: Date, end: Date) : List<WorkoutResultDomainModel> {
        val domainModelList = ArrayList<WorkoutResultDomainModel>()

        val dataModelList = resultDao.getResultsForPeriod(start, end)
        for (dataModel in dataModelList) {
            domainModelList.add(modelMapper.mapDataToDomain(dataModel))
        }
        return domainModelList
    }


    override fun getWorkouts(): List<WorkoutDomainModel> {
        val domainModelList = ArrayList<WorkoutDomainModel>()
        val dataModelList = workoutDao.getWorkoutList()


        for (dataModel in dataModelList) {
            domainModelList.add(modelMapper.mapDataToDomain(dataModel))
        }
        return domainModelList
    }

    override fun addWorkout(workoutDomainModel: WorkoutDomainModel) {
        workoutDao.insertWorkout(modelMapper.mapDomainToData(workoutDomainModel))
    }

    override fun addResult(workoutId: Int, result: WorkoutResultDomainModel) {
        val dataModel = modelMapper.mapDomainToData(result)
        dataModel.workout = workoutId
        resultDao.insertResult(dataModel)
    }

    override fun updateWorkout(workoutDomainModel: WorkoutDomainModel) {
        val dataModel = modelMapper.mapDomainToData(workoutDomainModel)
        workoutDao.updateWorkout(dataModel)
    }
}