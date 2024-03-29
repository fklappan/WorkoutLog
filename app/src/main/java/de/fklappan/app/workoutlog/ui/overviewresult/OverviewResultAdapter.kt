package de.fklappan.app.workoutlog.ui.overviewresult

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.databinding.ListItemOverviewResultBinding
import de.fklappan.app.workoutlog.domain.toPrettyString
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel

class OverviewResultAdapter(private val clickListener: (WorkoutResultGuiModel) -> Unit)
    : RecyclerView.Adapter<OverviewResultAdapter.ViewHolder>() {

    var items: MutableList<OverviewResultGuiModel> = ArrayList()
        set(value) {
            items.clear()
            items.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_overview_result, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], clickListener)
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemOverviewResultBinding.bind(itemView)

        fun bindItem(workoutGuiModel: OverviewResultGuiModel, clickListener: (WorkoutResultGuiModel) -> Unit) {
            with (binding) {
                textViewDate.text = workoutGuiModel.result.date.toPrettyString()
                textViewWorkoutCaption.text = workoutGuiModel.workout.text
                textViewResultCaption.text = workoutGuiModel.result.score
                itemView.setOnClickListener { clickListener.invoke(workoutGuiModel.result) }
            }

        }
    }
}