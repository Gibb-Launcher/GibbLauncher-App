package gibblauncher.gibblauncherapp.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.model.Training
import io.realm.Realm
import io.realm.kotlin.where
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
        val realm = Realm.getDefaultInstance()
        val results = realm.where<Training>().findAll().toArray()

        return results.map { it as Training }
    }
}
