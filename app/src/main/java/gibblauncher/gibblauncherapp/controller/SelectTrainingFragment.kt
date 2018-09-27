package gibblauncher.gibblauncherapp.controller

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.model.Training
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_select_training.*

class SelectTrainingFragment : Fragment(), View.OnClickListener {

    private lateinit var realm: Realm

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_select_training, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Novo Treino"

        arguments?.getInt("position")

        checkboxSelectTrainingFragment.setOnCheckedChangeListener { buttonView, isChecked ->
            changeSpinners(!isChecked)
        }

        buttonSaveTrainingSelectFragment!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        // Open the realm for the UI thread.
        realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            // Add a Training
            val training = realm.createObject<Training>("Titulo 4")
            training.shots.add("Shot")
            training.shots.add("Shot")
            training.shots.add("Shot")
            training.shots.add("Shot")
            training.shots.add("Shot")
        }

        val results = realm.where<Training>().findAll()
        Log.d("Resultados", results.toString());
    }

    private fun changeSpinners(change: Boolean) {
        spinnerTrainingOne.isEnabled = change
        spinnerTrainingTwo.isEnabled = change
        spinnerTrainingThree.isEnabled = change
        spinnerTrainingFour.isEnabled = change
        spinnerTrainingFive.isEnabled = change
        spinnerTrainingSix.isEnabled = change
        spinnerTrainingSeven.isEnabled = change
        spinnerTrainingEight.isEnabled = change
        spinnerTrainingNine.isEnabled = change
        spinnerTrainingTen.isEnabled = change
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
