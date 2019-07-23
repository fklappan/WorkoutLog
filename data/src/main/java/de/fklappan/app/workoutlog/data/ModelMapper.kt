package de.fklappan.app.workoutlog.data

import de.fklappan.app.workoutlog.data.result.ResultDataModel
import de.fklappan.app.workoutlog.data.workout.WorkoutDataModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import java.util.*

/**
 * Helper class to map between data and domain models
 */
class ModelMapper {

    fun mapDataToDomain(dataModel: WorkoutDataModel) = WorkoutDomainModel(dataModel.workoutId, dataModel.text, dataModel.favorite)

    fun mapDataToDomain(dataModel: ResultDataModel) =
        WorkoutResultDomainModel(dataModel.resultId, dataModel.score, dataModel.date, dataModel.workout, dataModel.pr, dataModel.note, dataModel.feeling)

    fun mapDomainToData(domainModel: WorkoutDomainModel) = WorkoutDataModel(domainModel.workoutId, domainModel.text, domainModel.favorite)

    fun mapDomainToData(domainModel: WorkoutResultDomainModel) =
        ResultDataModel(domainModel.resultId, domainModel.score, domainModel.date, domainModel.workout, domainModel.pr, domainModel.note, domainModel.feeling)
}