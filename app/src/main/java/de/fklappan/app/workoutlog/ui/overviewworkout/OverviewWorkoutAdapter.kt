package de.fklappan.app.workoutlog.ui.overviewworkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.fklappan.app.workoutlog.R
import kotlinx.android.synthetic.main.list_item_workout.view.*

class OverviewWorkoutAdapter(private val clickListener: (WorkoutGuiModel) -> Unit) : RecyclerView.Adapter<OverviewWorkoutAdapter.ViewHolder>() {

    var items: MutableList<WorkoutGuiModel> = ArrayList()
        set(value) {
            items.clear()
            items.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_workout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), clickListener)
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(workoutGuiModel: WorkoutGuiModel, clickListener: (WorkoutGuiModel) -> Unit) {
            itemView.textViewContent.text = workoutGuiModel.text
            itemView.setOnClickListener { clickListener.invoke(workoutGuiModel) }
        }

    }
}