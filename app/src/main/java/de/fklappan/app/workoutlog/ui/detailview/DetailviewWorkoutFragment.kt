package de.fklappan.app.workoutlog.ui.detailview

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.ViewModelFactory
import de.fklappan.app.workoutlog.ui.overviewworkout.OverviewWorkoutViewModel
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import kotlinx.android.synthetic.main.card_workout_detail.*
import kotlinx.android.synthetic.main.detailview_workout.*
import kotlinx.android.synthetic.main.overview.floatingActionButton
import javax.inject.Inject

// TODO 29.07.2019 Flo Add possible argument keys as const val EXTRA_blabla
// TODO 29.07.2019 Flo remove the state stuff and refactor viewmodel
// TODO 29.07.2019 Flo use ctrl + OLI

class DetailviewWorkoutFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelDetail: DetailviewWorkoutViewModel
    private lateinit var workoutResultAdapter: WorkoutResultAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.detailview_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getInjector().inject(this)
        super.onViewCreated(view, savedInstanceState)

        initFragment()
        initFab()
        initViewModels()
        observeViewModels()
        fetchData()
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_workout_results)
        workoutResultAdapter = WorkoutResultAdapter { clickedResult ->
            Log.d(LOG_TAG, "clicked result id" + clickedResult.workout)
        }

        recyclerViewResults.adapter = workoutResultAdapter
        imageButtonFavorite.setOnClickListener{ viewModelDetail.favoriteClicked() }
    }

    private fun initFab() {
        floatingActionButton.setOnClickListener { view ->
            // TODO 05.07.2019 Flo maybe simply passing arguments is not the correct thing to do. But it should be ok for now
            Navigation.findNavController(view!!).navigate(R.id.action_detailviewWorkoutFragment_to_addResultFragment, arguments)
        }
    }

    private fun initViewModels() {
        viewModelDetail = ViewModelProviders.of(this, viewModelFactory).get(DetailviewWorkoutViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelDetail.state.observe(this, Observer { state -> renderState(state) } )
        viewModelDetail.updateStream.observe(this, Observer { workout -> showWorkoutDetails(workout)})
    }

    private fun fetchData() {
        viewModelDetail.loadWorkout(arguments!!.getInt("workoutId"))
    }

    private fun renderState(state: DetailviewWorkoutState) {
        if (state.error != null) {

        }
        if (state.workout != null){
            showResult(state.workout)
        }
//        showLoading(workoutState.loading)
//        if (workoutState.error != null) {
//            showError(workoutState.error)
//        } else {
//            showResult(workoutState.workoutList)
//        }
    }

    private fun showWorkoutDetails(workoutGuiModel: WorkoutGuiModel) {
        textViewWorkoutDetails.text = workoutGuiModel.text
        if (workoutGuiModel.favorite) {
            imageButtonFavorite.imageTintList = ColorStateList.valueOf(context!!.getColor(R.color.colorAccent))
        } else {
            imageButtonFavorite.imageTintList = ColorStateList.valueOf(context!!.getColor(R.color.gray))
        }
    }

    private fun showResult(result: WorkoutDetailsGuiModel) {
        Log.d(LOG_TAG, "Workout details loaded: " + result.workout.text)
        Log.d(LOG_TAG, "Results loaded: " + result.resultList.size)
        showWorkoutDetails(result.workout)

        val items = ArrayList<WorkoutResultGuiModel>()
        items.addAll(result.resultList)

        recyclerViewResults.visibility = View.VISIBLE
        workoutResultAdapter.items = items
    }
}