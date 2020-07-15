package de.fklappan.app.workoutlog.ui.detailviewworkout

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
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

    @Override
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit, menu)
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_edit) {
            editWorkout()
//            viewModelDetail.editWorkoutClicked()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_workout_results)
        workoutResultAdapter = WorkoutResultAdapter { clickedResult ->
            Log.d(LOG_TAG, "clicked result id" + clickedResult.workout)
        }
        recyclerViewResults.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerViewResults.adapter = workoutResultAdapter
        buttonFavorite.setOnClickListener { viewModelDetail.onFavoriteClicked() }
        setHasOptionsMenu(true)
        imageButtonExpand.setOnClickListener{onExpandClicked()}
    }

    private fun onExpandClicked() {
        imageButtonExpand.isSelected = !imageButtonExpand.isSelected
        // TODO: 15.07.20 animate
        if (imageButtonExpand.isSelected) {
            imageButtonExpand.setImageDrawable(resources.getDrawable(R.drawable.expand_more, null))
            textViewWorkoutDetails.visibility = View.GONE
            viewDividerWorkoutDetail.visibility = View.GONE
            buttonFavorite.visibility = View.GONE
        } else {
            imageButtonExpand.setImageDrawable(resources.getDrawable(R.drawable.expand_less, null))
            textViewWorkoutDetails.visibility = View.VISIBLE
            viewDividerWorkoutDetail.visibility = View.VISIBLE
            buttonFavorite.visibility = View.VISIBLE
        }

    }

    private fun initFab() {
        floatingActionButton.setOnClickListener { view ->
            // TODO 05.07.2019 Flo maybe simply passing arguments is not the correct thing to do. But it should be ok for now
            Navigation.findNavController(view!!)
                .navigate(R.id.action_detailviewWorkoutFragment_to_addResultFragment, arguments)
        }
    }

    private fun initViewModels() {
        viewModelDetail = ViewModelProviders.of(this, viewModelFactory).get(DetailviewWorkoutViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelDetail.state.observe(viewLifecycleOwner, Observer { state -> updateState(state)})
    }

    private fun fetchData() {
        viewModelDetail.loadWorkout(arguments!!.getInt("workoutId"))
    }

    private fun updateState(state: DetailviewWorkoutState) {
        when(state) {
            is DetailviewWorkoutState.Loading -> showLoading()
            is DetailviewWorkoutState.Error -> showError(state.message)
            is DetailviewWorkoutState.WorkoutDetails -> showWorkoutDetails(state.workoutDetails)
            is DetailviewWorkoutState.WorkoutUpdate -> showWorkoutUpdate(state.workout)
        }
    }

    private fun showWorkoutUpdate(workoutGuiModel: WorkoutGuiModel) {
        Log.d(LOG_TAG, "Workout updated")
        textViewWorkoutDetails.text = workoutGuiModel.text
        if (workoutGuiModel.favorite) {
            buttonFavorite.strokeColor = ColorStateList.valueOf(context!!.getColor(R.color.colorAccent))
            buttonFavorite.backgroundTintList = ColorStateList.valueOf(context!!.getColor(R.color.colorAccent))
            buttonFavorite.setTextColor(ColorStateList.valueOf(context!!.getColor(R.color.white)))
        } else {
            buttonFavorite.strokeColor = ColorStateList.valueOf(context!!.getColor(R.color.gray))
            buttonFavorite.backgroundTintList = ColorStateList.valueOf(context!!.getColor(R.color.transparent))
            buttonFavorite.setTextColor(ColorStateList.valueOf(context!!.getColor(R.color.black)))
        }
    }

    private fun editWorkout() {
        Navigation.findNavController(view!!)
            .navigate(R.id.action_detailviewWorkoutFragment_to_editWorkoutFragment, arguments)
    }

    private fun showWorkoutDetails(result: WorkoutDetailsGuiModel) {
        Log.d(LOG_TAG, "Workout details loaded: " + result.workout.text)
        Log.d(LOG_TAG, "Results loaded: " + result.resultList.size)
        showWorkoutUpdate(result.workout)

        val items = ArrayList<WorkoutResultGuiModel>()
        items.addAll(result.resultList)

        recyclerViewResults.visibility = View.VISIBLE
        workoutResultAdapter.items = items
    }

    private fun showLoading() {
        Log.d(LOG_TAG, "Loading workout details")
    }

    private fun showError(message: String) {
        Log.e(LOG_TAG, "Error fetching workout details: $message")
    }
}