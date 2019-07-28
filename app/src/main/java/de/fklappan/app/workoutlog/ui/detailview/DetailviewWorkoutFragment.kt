package de.fklappan.app.workoutlog.ui.detailview

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
import kotlinx.android.synthetic.main.card_workout_detail.*
import kotlinx.android.synthetic.main.detailview_workout.*
import kotlinx.android.synthetic.main.overview.floatingActionButton
import javax.inject.Inject

class DetailviewWorkoutFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelDetail: DetailviewWorkoutViewModel
    private lateinit var workoutResultAdapter: WorkoutResultAdapter

    var workoutId: Int = 0

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

    private fun showResult(result: WorkoutDetailsGuiModel) {
        Log.d(LOG_TAG, "Workout details loaded: " + result.workout.text)
        Log.d(LOG_TAG, "Results loaded: " + result.resultList.size)
        workoutId = result.workout.workoutId

        textViewWorkoutDetails.text = result.workout.text

        val items = ArrayList<WorkoutResultGuiModel>()
        items.addAll(result.resultList)

        recyclerViewResults.visibility = View.VISIBLE
        workoutResultAdapter.items = items
    }
}