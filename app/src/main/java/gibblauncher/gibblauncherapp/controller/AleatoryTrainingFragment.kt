package gibblauncher.gibblauncherapp.controller

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.helper.RetrofitInitializer
import gibblauncher.gibblauncherapp.model.Bounces
import gibblauncher.gibblauncherapp.model.Training
import gibblauncher.gibblauncherapp.model.TrainingDataApi
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_aleatory_training.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AleatoryTrainingFragment : Fragment(), View.OnClickListener {

    private var training = Training()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_aleatory_training, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trainingTitle = arguments?.getString("trainingTitle")

        (activity as AppCompatActivity).supportActionBar?.title = trainingTitle

        trainingTitle?.let { initializeTrainingData(it) }

        buttonStartAleatoryTrainingFragment.setOnClickListener(this)

    }

    private fun takeTrainingInDatabase(trainingTitle: String): Training {
        val realm = Realm.getDefaultInstance()
        val results = realm.where<Training>().`in`("title", arrayOf(trainingTitle)).findFirst()

        return results as Training
    }

    private fun initializeTrainingData(trainingTitle: String) {
        training = takeTrainingInDatabase(trainingTitle)

        trainingPositionAleatoryTrainingFragment.setText(training.launcherPosition?.let { parseLauncherPosition(it) })

    }

    override fun onClick(v: View?) {
        val dialog = progressDialogChanges()

        val call = trainingData()?.let { RetrofitInitializer().apiService().post(it) }
        call!!.enqueue(object: Callback<Bounces?> {
            override fun onResponse(call: Call<Bounces?>?,
                                    response: Response<Bounces?>?) {
                dialog.dismiss()

                response?.body()?.let {
                    for(bounce in it.bounces)
                        Log.d("Response", bounce.x.toString() + " " + bounce.y)
                }
            }

            override fun onFailure(call: Call<Bounces?>?,
                                   t: Throwable?) {
                dialog.dismiss()

                Log.d("Response", t?.message)
            }
        })
    }

    private fun trainingData(): TrainingDataApi? {
        // TODO - fazer lógica pra pegar o ID no banco e incrementar
        val id = "IdAleatorio"
        val launcherPosition = parseLauncherPositionString(trainingPositionAleatoryTrainingFragment.text.toString())
        val shots = listOf(training.possibleShots[(0 until training.possibleShots.size).random()],
                training.possibleShots[(0 until training.possibleShots.size).random()],
                training.possibleShots[(0 until training.possibleShots.size).random()],
                training.possibleShots[(0 until training.possibleShots.size).random()],
                training.possibleShots[(0 until training.possibleShots.size).random()],
                training.possibleShots[(0 until training.possibleShots.size).random()],
                training.possibleShots[(0 until training.possibleShots.size).random()],
                training.possibleShots[(0 until training.possibleShots.size).random()],
                training.possibleShots[(0 until training.possibleShots.size).random()],
                training.possibleShots[(0 until training.possibleShots.size).random()])


        var trainingDataApi = launcherPosition?.let { TrainingDataApi(id, it, shots) }

        return trainingDataApi
    }

    fun IntRange.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    private fun progressDialogChanges(): ProgressDialog {
        var dialog = createDialog("Enviando informações para o lançador!")

        val handler = Handler()
        handler.postDelayed({ dialog.setMessage("Iniciando treinamento!") }, 3000)
        handler.postDelayed({ dialog.setMessage("Analizando jogadas!") }, 10000)


        return dialog
    }

    private fun parseLauncherPositionString(position: String): Int? {
        var launcherPositionString : Int? = null
        when(position) {
            "Esquerda" -> launcherPositionString = 0
            "Centro" -> launcherPositionString = 1
            "Direita" -> launcherPositionString = 2
        }

        return launcherPositionString
    }

    private fun createDialog(message: String): ProgressDialog {
        val dialog = ProgressDialog(context)
        dialog.setMessage(message)
        dialog.setTitle("")
        dialog.setCancelable(false)
        dialog.isIndeterminate = true
        dialog.show()

        return dialog
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
