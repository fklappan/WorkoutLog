package de.fklappan.app.workoutlog.ui.detailviewworkout

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import de.fklappan.app.workoutlog.ui.overviewworkout.DeleteViewModel
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.card_workout_detail.*
import kotlinx.android.synthetic.main.detailview_workout.*
import kotlinx.android.synthetic.main.overview.floatingActionButton
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// TODO 29.07.2019 Flo Add possible argument keys as const val EXTRA_blabla
// TODO 29.07.2019 Flo remove the state stuff and refactor viewmodel
// TODO 29.07.2019 Flo use ctrl + OLI

class DetailviewWorkoutFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelDetail: DetailviewWorkoutViewModel
    private lateinit var viewModelDelete: DeleteViewModel
    private lateinit var workoutResultAdapter: WorkoutResultAdapter
    private lateinit var snackbarDelete: Snackbar
    private var disposableDelete = CompositeDisposable()

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
        workoutResultAdapter = WorkoutResultAdapter(this::clickedResult, this::clickedOptions)
        recyclerViewResults.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerViewResults.adapter = workoutResultAdapter
        snackbarDelete = Snackbar.make(requireView(), getString(R.string.success_deleted_result), Snackbar.LENGTH_LONG)
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light, requireActivity().theme))
        setHasOptionsMenu(true)
        imageButtonExpand.setOnClickListener{onExpandClicked()}
    }

    fun clickedResult(workoutResultGuiModel: WorkoutResultGuiModel) {
        val bundle = bundleOf("resultId" to workoutResultGuiModel.resultId)
        Navigation.findNavController(requireView()).navigate(R.id.detailViewResultFragment, bundle)
    }

    @SuppressLint("RestrictedApi")
    fun clickedOptions(view: View, workoutResultGuiModel: WorkoutResultGuiModel) {
        val menuBuilder = MenuBuilder(requireContext())
        val inflater = MenuInflater(requireContext())
        inflater.inflate(R.menu.options, menuBuilder)

        val menuHelper = MenuPopupHelper(requireContext(), menuBuilder, view)
        menuHelper.setForceShowIcon(true)
        val callback = object: MenuBuilder.Callback {
            override fun onMenuModeChange(menu: MenuBuilder) {
            }

            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                return menuItemClicked(item, workoutResultGuiModel)
            }
        }
        menuBuilder.setCallback(callback)
        menuHelper.show()
    }

    fun menuItemClicked(item: MenuItem, workoutResultGuiModel: WorkoutResultGuiModel) : Boolean {
        when(item.itemId) {
            R.id.action_delete -> {
                prepareDeleteResult(workoutResultGuiModel)
                return true
            }
            R.id.action_edit -> {
                val bundle = bundleOf("resultId" to workoutResultGuiModel.resultId)
                Navigation.findNavController(requireView())
                    .navigate(R.id.editResultFragment, bundle)
                return true
            }
        }
        return false
    }

    private fun prepareDeleteResult(workoutResultGuiModel: WorkoutResultGuiModel) {
        Log.d(LOG_TAG, "deleteResult")

        val deletedIndex = workoutResultAdapter.deleteItem(workoutResultGuiModel)

        snackbarDelete
            .setAction(getString(R.string.undo)) { undoDelete(workoutResultGuiModel, deletedIndex) }
            .show()

        disposableDelete.add(
            Single.just(workoutResultGuiModel)
                .delay(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::deleteForGood)
        )
    }

    private fun undoDelete(workoutResultGuiModel: WorkoutResultGuiModel, deletedIndex: Int) {
        disposableDelete.clear()
        workoutResultAdapter.addItem(workoutResultGuiModel, deletedIndex)
    }

    private fun deleteForGood(workoutResultGuiModel: WorkoutResultGuiModel) {
        viewModelDelete.deleteResult(workoutResultGuiModel.resultId)
//        vi.deleteWorkout(workoutGuiModel.workoutId)
    }

    private fun onExpandClicked() {
        imageButtonExpand.isSelected = !imageButtonExpand.isSelected
        // TODO: 15.07.20 animate
        if (imageButtonExpand.isSelected) {
            imageButtonExpand.setImageDrawable(resources.getDrawable(R.drawable.expand_more, null))
            textViewWorkoutDetails.visibility = View.GONE
        } else {
            imageButtonExpand.setImageDrawable(resources.getDrawable(R.drawable.expand_less, null))
            textViewWorkoutDetails.visibility = View.VISIBLE
        }

    }

    private fun initFab() {
        floatingActionButton.setOnClickListener { view ->
            // TODO 05.07.2019 Flo maybe simply passing arguments is not the correct thing to do. But it should be ok for now
            Navigation.findNavController(requireView())
                .navigate(R.id.action_detailviewWorkoutFragment_to_addResultFragment, arguments)
        }
    }

    private fun initViewModels() {
        viewModelDetail = ViewModelProvider(this, viewModelFactory).get(DetailviewWorkoutViewModel::class.java)
        viewModelDelete = ViewModelProvider(this, viewModelFactory).get(DeleteViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelDetail.state.observe(viewLifecycleOwner, Observer { state -> updateState(state)})
    }

    private fun fetchData() {
        viewModelDetail.loadWorkout(requireArguments().getInt("workoutId"))
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
    }

    private fun editWorkout() {
        Navigation.findNavController(requireView())
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