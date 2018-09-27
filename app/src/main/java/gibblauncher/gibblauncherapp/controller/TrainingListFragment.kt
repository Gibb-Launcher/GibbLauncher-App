package gibblauncher.gibblauncherapp.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.model.Training
import kotlinx.android.synthetic.main.fragment_select_training.*
import kotlinx.android.synthetic.main.fragment_training_list.*

class TrainingListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_training_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Treinamentos"

        val recyclerView = recyclerViewTrainings
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = TrainingListAdapter(trainings(), context)
        recyclerView.setHasFixedSize(true)

    }

    private fun trainings(): List<Training> {
        val training = Training()
        training.title = "Titulo"
        return listOf(
                training,
                training,
                training)
    }
}
