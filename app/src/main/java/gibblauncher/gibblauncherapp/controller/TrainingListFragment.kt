package gibblauncher.gibblauncherapp.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.model.Training
import gibblauncher.gibblauncherapp.model.TrainingResult
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException
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

        setRecyclerView()

        setFabListener()
    }

    private fun setFabListener() {
        fabTrainingList.setOnClickListener {
            var playFragment = PlayFragment()

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, playFragment)
                    .addToBackStack(null)
                    .commit()
        }
    }

    private fun setRecyclerView() {
        val recyclerView = recyclerViewTrainings
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = TrainingListAdapter(trainings(), context)
        recyclerView.setHasFixedSize(true)

        (recyclerView.adapter as TrainingListAdapter).onItemClick = { training ->

            if(training.isAleatory) {
                openAleatoryTraining(training)
            } else {
                openTraining(training)
            }
        }
    }

    private fun openAleatoryTraining(training: Training) {
        var bundle = Bundle()
        var aleatoryTrainingFragment = AleatoryTrainingFragment()
        bundle.putString("trainingTitle", training.title)

        aleatoryTrainingFragment.arguments = bundle

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_container, aleatoryTrainingFragment)
                .addToBackStack(null)
                .commit()
    }

    private fun openTraining(training: Training) {
        var bundle = Bundle()
        var trainingFragment = TrainingFragment()
        bundle.putString("trainingTitle", training.title)

        trainingFragment.arguments = bundle

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_container, trainingFragment)
                .addToBackStack(null)
                .commit()
    }

    private fun trainings(): List<Training> {
        var realm:Realm? = null
        try {
            Realm.init(context)
            val config = RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build()
            realm = Realm.getInstance(config)
        } catch (ex: RealmMigrationNeededException) {
            realm = Realm.getDefaultInstance()
        }


        val results = realm?.where<Training>()?.findAll()?.toArray()

        return results!!.map { it as Training }
    }
}
