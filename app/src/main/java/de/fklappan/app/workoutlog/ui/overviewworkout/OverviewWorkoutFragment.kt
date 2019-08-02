package de.fklappan.app.workoutlog.ui.overviewworkout

import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.RenderProcessGoneDetail
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import kotlinx.android.synthetic.main.bottom_sheet_overview.*
import kotlinx.android.synthetic.main.overview.*
import javax.inject.Inject

class OverviewWorkoutFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelOverviewWorkout: OverviewWorkoutViewModel
    private lateinit var overviewWorkoutAdapter: OverviewWorkoutAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.overview, container, false)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overview, menu)

        val drawable = menu.findItem(R.id.action_filter).icon
        drawable?.let {
            DrawableCompat.setTint(it, context!!.getColor(R.color.white))
            menu.findItem(R.id.action_filter).icon = it
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_filter -> {
                toggleBottomSheet()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleBottomSheet() {
        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        with(sheetBehavior) {
            state = if (state == STATE_EXPANDED) {
                STATE_COLLAPSED
            } else {
                STATE_EXPANDED
            }
        }
    }

    private fun initFab() {
        floatingActionButton.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_overviewFragment_to_addWorkoutFragment)
        }
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_workout_overview)
        setHasOptionsMenu(true)
        BottomSheetBehavior.from(bottomSheet).state = STATE_COLLAPSED

        overviewWorkoutAdapter = OverviewWorkoutAdapter(this::workoutClicked, this::favoriteClicked)
        recyclerViewWorkouts.adapter = overviewWorkoutAdapter

        for (child in chipGroupTags.children) {
            child.setOnClickListener(this::chipClicked)
        }
    }

    private fun workoutClicked(workoutGuiModel: WorkoutGuiModel) {
        Log.d(LOG_TAG, "clicked workout id" + workoutGuiModel.workoutId)
        val bundle = bundleOf("workoutId" to workoutGuiModel.workoutId)
        Navigation.findNavController(view!!).navigate(R.id.action_overviewFragment_to_detailviewWorkoutFragment, bundle)
    }

    private fun favoriteClicked(workoutId: Int) {
        Log.d(LOG_TAG, "clicked workout $workoutId to toggle favorite")
        viewModelOverviewWorkout.favoriteClicked(workoutId)
    }

    private fun chipClicked(view: View) {
        view.isSelected = !view.isSelected
        applyFilter()
    }

    private fun applyFilter() {
        val filterList = ArrayList<Int>()
        for (child in chipGroupTags.children) {
            if (child.isSelected) {
                filterList.add(Integer.parseInt(child.tag as String))
            }
        }
        viewModelOverviewWorkout.filterWorkouts(filterList)
    }

    private fun initViewModels() {
        viewModelOverviewWorkout =
            ViewModelProviders.of(this, viewModelFactory).get(OverviewWorkoutViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelOverviewWorkout.workoutList.observe(this, Observer { workoutList -> showWorkoutList(workoutList) })
        viewModelOverviewWorkout.updateStream.observe(this, Observer { workout -> updateWorkout(workout) })
    }

    private fun fetchData() {
        viewModelOverviewWorkout.loadWorkouts()
    }

    private fun updateWorkout(workoutGuiModel: WorkoutGuiModel) {
        Log.d(LOG_TAG, "Updating workout ${workoutGuiModel.workoutId}")
        overviewWorkoutAdapter.update(workoutGuiModel)
    }

    private fun showWorkoutList(resultList: MutableList<WorkoutGuiModel>) {
        Log.d(LOG_TAG, "Results loaded: " + resultList.size)
        overviewWorkoutAdapter.items = resultList
    }
}