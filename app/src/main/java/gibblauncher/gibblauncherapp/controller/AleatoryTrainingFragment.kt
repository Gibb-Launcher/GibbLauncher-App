package gibblauncher.gibblauncherapp.controller

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.helper.RetrofitInitializer
import gibblauncher.gibblauncherapp.model.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.exceptions.RealmMigrationNeededException
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_aleatory_training.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AleatoryTrainingFragment : Fragment(), View.OnClickListener {

    private var training = Training()
    private lateinit var realm: Realm
    private var trainingTitle: String? = null
    private var saveShots: RealmList<String> = RealmList()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_aleatory_training, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trainingTitle = arguments?.getString("trainingTitle")

        (activity as AppCompatActivity).supportActionBar?.title = trainingTitle

        trainingTitle?.let { initializeTrainingData(it) }

        buttonStartAleatoryTrainingFragment.setOnClickListener(this)

    }

    private fun takeTrainingInDatabase(trainingTitle: String): Training {
        realm = Realm.getDefaultInstance()


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
        call!!.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?,
                                    response: Response<String>?) {

                response?.body()?.let {

                    if(it.equals("Ok")){
                        createAlertDialog("Treino realizado com sucesso!")
                    } else if(it.equals("Fail")) {
                        createAlertDialog("Outro jogador já está conectado no Lançador!")
                    }
                    dialog.dismiss()
                }


            }

            override fun onFailure(call: Call<String>?,
                                   t: Throwable?) {
                dialog.dismiss()

                Toast.makeText(context, "Erro na conexão com o lançador!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createAlertDialog(message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle("")
        builder.setPositiveButton("OK") { _,_ ->

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun nextId(): Int {
        val currentIdNum = realm.where(TrainingResult::class.java).max("id")
        val nextId: Int
        if (currentIdNum == null) {
            nextId = 1
        } else {
            nextId = currentIdNum!!.toInt() + 1
        }

        return nextId
    }

    private fun trainingData(): TrainingDataApi? {
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

        saveShots!!.addAll(shots)

        val ip : String = activity.intent.extras.getString("IP")
        val mac : String = activity.intent.extras.getString("MAC")

        var realm = Realm.getDefaultInstance()
        val id_trainingResult = nextId()

        try {
            realm.executeTransaction { realm ->
                // Add a Training
                val trainingResult = realm.createObject<TrainingResult>()
                trainingResult.title = trainingTitle
                trainingResult.id = id_trainingResult
                trainingResult.shots = training.shots
                trainingResult.dateTrainingResult = Date()
            }
        } catch (e: Exception) {
            Log.d("Erro", e.message)
        }


        var trainingDataApi = launcherPosition?.let { TrainingDataApi(id_trainingResult, it+1, shots!!, ip, mac) }

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
}
