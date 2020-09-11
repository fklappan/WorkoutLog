package de.fklappan.app.workoutlog.ui.detailviewworkout

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.domain.toPrettyString
import kotlinx.android.synthetic.main.list_item_workout.view.textViewContent
import kotlinx.android.synthetic.main.list_item_workout_result.view.*

class WorkoutResultAdapter(private val clickListener: (WorkoutResultGuiModel) -> Unit,
                           private val optionsListener: (View, WorkoutResultGuiModel) -> Unit) :
    RecyclerView.Adapter<WorkoutResultAdapter.ViewHolder>() {

    var items: MutableList<WorkoutResultGuiModel> = ArrayList()
        set(value) {
            items.clear()
            items.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_workout_result,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), clickListener, optionsListener)
    }

    override fun getItemCount() = items.size

    fun addItem(workoutResultGuiModel: WorkoutResultGuiModel, index: Int) {
        items.add(index, workoutResultGuiModel)
        notifyItemInserted(index)
    }

    fun deleteItem(workoutResultGuiModel: WorkoutResultGuiModel): Int {
        val removedIndex = items.indexOf(workoutResultGuiModel)
        if (items.remove(workoutResultGuiModel)) {
            notifyItemRemoved(removedIndex)
        }
        return removedIndex
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(workoutGuiModel: WorkoutResultGuiModel,
                     clickListener: (WorkoutResultGuiModel) -> Unit,
                     optionsListener: (View, WorkoutResultGuiModel) -> Unit) {
            itemView.textViewDate.text = workoutGuiModel.date.toPrettyString()
            itemView.textViewContent.text = workoutGuiModel.score
            itemView.setOnClickListener { clickListener.invoke(workoutGuiModel) }
            itemView.imageButtonMore.setOnClickListener { optionsListener.invoke(it, workoutGuiModel) }
        }

    }
}