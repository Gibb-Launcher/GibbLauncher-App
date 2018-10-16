package gibblauncher.gibblauncherapp.controller

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.helper.RetrofitInitializer
import gibblauncher.gibblauncherapp.model.Test
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
        val call = RetrofitInitializer().apiService().get()
        call.enqueue(object: Callback<Test?> {
            override fun onResponse(call: Call<Test?>?,
                                    response: Response<Test?>?) {
                response?.body()?.let {
                    Log.d("Response", it.data)
                }
            }

            override fun onFailure(call: Call<Test?>?,
                                   t: Throwable?) {
                Log.d("Response", "what")
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
