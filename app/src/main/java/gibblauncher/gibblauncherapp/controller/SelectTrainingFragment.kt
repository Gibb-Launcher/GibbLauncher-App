package gibblauncher.gibblauncherapp.controller

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast

import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.model.Training
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_select_training.*

class SelectTrainingFragment : Fragment(), View.OnClickListener {

    private lateinit var realm: Realm
    private var position: Int? = null
    private lateinit var myShoots: Array<String>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_select_training, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Novo Treino"

        position = arguments?.getInt("position")
        filteringShots()
        initializeSpinners()

        checkboxSelectTrainingFragment.setOnCheckedChangeListener { buttonView, isChecked ->
            changeSpinners(!isChecked)
        }

        buttonSaveTrainingSelectFragment!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        saveTrainingInDatabase()

        context.toast("Treino salvo com sucesso!")
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_container, BlankFragment())
                .commit()
        activity.onBackPressed()
    }


    private fun saveTrainingInDatabase() {
        val title = trainingTitleSelectTrainingFragment.text

        if(title != null && title.isNotEmpty()) {
            // Open the realm for the UI thread.
            var realm = Realm.getDefaultInstance()


            try {
                realm!!.executeTransaction { realm ->
                    // Add a Training
                    val training = realm.createObject<Training>(title.toString())
                    training.launcherPosition = position

                    if(checkboxSelectTrainingFragment.isChecked) {
                        training.isAleatory = true
                        for(shot in myShoots) training.possibleShots.add(shot)
                    } else {
                        training.shots.add(spinnerTrainingOne.selectedItem.toString())
                        training.shots.add(spinnerTrainingOne.selectedItem.toString())
                        training.shots.add(spinnerTrainingOne.selectedItem.toString())
                        training.shots.add(spinnerTrainingOne.selectedItem.toString())
                        training.shots.add(spinnerTrainingOne.selectedItem.toString())
                        training.shots.add(spinnerTrainingOne.selectedItem.toString())
                        training.shots.add(spinnerTrainingOne.selectedItem.toString())
                        training.shots.add(spinnerTrainingOne.selectedItem.toString())
                        training.shots.add(spinnerTrainingOne.selectedItem.toString())
                        training.shots.add(spinnerTrainingOne.selectedItem.toString())
                    }
                }
            } catch (e: RealmPrimaryKeyConstraintException) {
                trainingTitleSelectTrainingFragment.setError("Já existe um treino com esse nome!")
                trainingTitleSelectTrainingFragment.requestFocus()
            }


        } else {
            trainingTitleSelectTrainingFragment.setError("É necessário colocar um título!")
            trainingTitleSelectTrainingFragment.requestFocus()
        }
    }

    private fun filteringShots() {
        when(position) {
            2 -> myShoots = arrayOf("Forehand Cruzado - Longo", "Backhand Cruzado - Centro", "Backhand Paralelo - Longo", "Forehand Cruzado - Curto", "Backhand Paralelo - Curto")
            1 -> myShoots = arrayOf("Forehand Cruzado - Longo", "Backhand Paralelo - Centro", "Backhand Cruzado - Longo", "Forehand Cruzado - Curto", "Backhand Cruzado - Curto")
            0 -> myShoots = arrayOf("Forehand Paralelo - Longo", "Backhand Cruzado - Centro", "Backhand Cruzado - Longo", "Forehand Paralelo - Curto", "Backhand Cruzado - Curto")
        }
    }

    private fun initializeSpinners() {
        spinnerTrainingOne.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myShoots)
        spinnerTrainingTwo.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myShoots)
        spinnerTrainingThree.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myShoots)
        spinnerTrainingFour.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myShoots)
        spinnerTrainingFive.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myShoots)
        spinnerTrainingSix.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myShoots)
        spinnerTrainingSeven.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myShoots)
        spinnerTrainingEight.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myShoots)
        spinnerTrainingNine.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myShoots)
        spinnerTrainingTen.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, myShoots)
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

    override fun onResume() {
        super.onResume()

        if (view == null) {
            return
        }

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                return if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.main_container, BlankFragment())
                            .commit()
                    activity.onBackPressed()
                    true
                } else false
            }
        })
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
