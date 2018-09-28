package gibblauncher.gibblauncherapp.controller

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.model.Training
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_aleatory_training.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AleatoryTrainingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AleatoryTrainingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AleatoryTrainingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_aleatory_training, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trainingTitle = arguments?.getString("trainingTitle")

        (activity as AppCompatActivity).supportActionBar?.title = trainingTitle

        trainingTitle?.let { initializeTrainingData(it) }
    }

    private fun takeTrainingInDatabase(trainingTitle: String): Training {
        val realm = Realm.getDefaultInstance()
        val results = realm.where<Training>().`in`("title", arrayOf(trainingTitle)).findFirst()

        return results as Training
    }

    private fun initializeTrainingData(trainingTitle: String) {
        var training = takeTrainingInDatabase(trainingTitle)

        trainingPositionAleatoryTrainingFragment.setText(training.launcherPosition?.let { parseLauncherPosition(it) })

    }

    private fun parseLauncherPosition(position: Int): String? {
        var launcherPositionString : String? = null
        when(position) {
            0 -> launcherPositionString = "Esquerda"
            1 -> launcherPositionString = "Centro"
            2 -> launcherPositionString = "Direita"
        }

        return launcherPositionString
    }

}
