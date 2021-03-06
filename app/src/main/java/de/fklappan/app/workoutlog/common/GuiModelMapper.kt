package de.fklappan.app.workoutlog.common

import de.fklappan.app.workoutlog.domain.*
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutDetailsGuiModel
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import de.fklappan.app.workoutlog.ui.overviewresult.OverviewResultGuiModel
import de.fklappan.app.workoutlog.ui.overviewstatistic.StatisticCurrentGuiModel
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import javax.inject.Inject

class GuiModelMapper @Inject constructor() {

    fun mapDomainToGui(domainModel: WorkoutDomainModel): WorkoutGuiModel =
        WorkoutGuiModel(domainModel.workoutId, domainModel.text, domainModel.favorite)

    fun mapDomainToGui(domainModel: WorkoutResultDomainModel): WorkoutResultGuiModel =
        WorkoutResultGuiModel(
            domainModel.resultId,
            domainModel.score,
            domainModel.date,
            domainModel.workout,
            domainModel.pr,
            domainModel.note,
            domainModel.feeling
        )

    fun mapDomainToGui(domainModel: WorkoutDetailsDomainModel): WorkoutDetailsGuiModel {
        val guiResultList = ArrayList<WorkoutResultGuiModel>()
        for (resultDomainModel in domainModel.resultList) {
            guiResultList.add(mapDomainToGui(resultDomainModel))
        }
        return WorkoutDetailsGuiModel(mapDomainToGui(domainModel.workout), guiResultList)
    }

    fun mapDomainToGui(domainModel: StatisticCurrentDomainModel): StatisticCurrentGuiModel =
        StatisticCurrentGuiModel(
            domainModel.streak,
            domainModel.isWorkoutStreak,
            domainModel.workoutCountMonth,
            domainModel.workoutCountYear
        )


    fun mapGuiToDomain(guiModel: WorkoutGuiModel): WorkoutDomainModel =
        WorkoutDomainModel(guiModel.workoutId, guiModel.text, guiModel.favorite)

    fun mapGuiToDomain(guiModel: WorkoutResultGuiModel): WorkoutResultDomainModel =
        WorkoutResultDomainModel(
            guiModel.resultId,
            guiModel.score,
            guiModel.date,
            guiModel.workout,
            guiModel.pr,
            guiModel.note,
            guiModel.feeling
        )

    fun mapDomainToGui(domainModel: OverviewResultDomainModel) : OverviewResultGuiModel =
        OverviewResultGuiModel(
            mapDomainToGui(domainModel.workout),
            mapDomainToGui(domainModel.result)
        )
}