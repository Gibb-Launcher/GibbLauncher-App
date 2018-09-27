package gibblauncher.gibblauncherapp.controller

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import gibblauncher.gibblauncherapp.model.Training
import android.content.Context
import android.view.LayoutInflater
import gibblauncher.gibblauncherapp.R
import kotlinx.android.synthetic.main.training_item.view.*

class TrainingListAdapter(private val trainings: List<Training>,
                          private val context: Context) : RecyclerView.Adapter<TrainingListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val training = trainings[position]

        holder?.let {
            it.bindView(training)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.training_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trainings.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(training: Training) {
            val title = itemView.trainingItemTitle

            title.text = training.title
        }
    }

}