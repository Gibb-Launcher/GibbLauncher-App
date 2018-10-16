package gibblauncher.gibblauncherapp.controller

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast

import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.helper.RetrofitInitializer
import gibblauncher.gibblauncherapp.model.Bounces
import gibblauncher.gibblauncherapp.model.Training
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_training.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrainingFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_training, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trainingTitle = arguments?.getString("trainingTitle")

        (activity as AppCompatActivity).supportActionBar?.title = trainingTitle

        trainingTitle?.let { initializeTrainingData(it) }

        buttonStartTrainingFragment.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        progressBarFragmentTraining.visibility = View.VISIBLE
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        val call = RetrofitInitializer().apiService().post()
        call.enqueue(object: Callback<Bounces?> {
            override fun onResponse(call: Call<Bounces?>?,
                                    response: Response<Bounces?>?) {
                progressBarFragmentTraining.visibility = View.INVISIBLE
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                response?.body()?.let {
                    for(bounce in it.bounces)
                        Log.d("Response", bounce.x.toString() + " " + bounce.y)
                }
            }

            override fun onFailure(call: Call<Bounces?>?,
                                   t: Throwable?) {
                progressBarFragmentTraining.visibility = View.INVISIBLE
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)


                Log.d("Response", t?.message)
            }
        })

        context.toast("Iniciando treinamento.")
    }

    private fun initializeTrainingData(trainingTitle: String) {
        var training = takeTrainingInDatabase(trainingTitle)

        trainingPositionTrainingFragment.setText(training.launcherPosition?.let { parseLauncherPosition(it) })
        trainingShotOneTrainingFragment.setText(training.shots[0])
        trainingShotTwoTrainingFragment.setText(training.shots[1])
        trainingShotThreeTrainingFragment.setText(training.shots[2])
        trainingShotFourTrainingFragment.setText(training.shots[3])
        trainingShotFiveTrainingFragment.setText(training.shots[4])
        trainingShotSixTrainingFragment.setText(training.shots[5])
        trainingShotSevenTrainingFragment.setText(training.shots[6])
        trainingShotEightTrainingFragment.setText(training.shots[7])
        trainingShotNineTrainingFragment.setText(training.shots[8])
        trainingShotTenTrainingFragment.setText(training.shots[9])
    }

    private fun takeTrainingInDatabase(trainingTitle: String): Training {
        val realm = Realm.getDefaultInstance()
        val results = realm.where<Training>().`in`("title", arrayOf(trainingTitle)).findFirst()

        return results as Training
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

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
