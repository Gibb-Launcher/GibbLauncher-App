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
import gibblauncher.gibblauncherapp.model.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_select_training.*
import kotlinx.android.synthetic.main.fragment_training.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class TrainingFragment : Fragment(), View.OnClickListener {

    private lateinit var realm: Realm
    private var trainingTitle: String? = null
    private lateinit var training:  Training

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_training, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trainingTitle = arguments?.getString("trainingTitle")

        (activity as AppCompatActivity).supportActionBar?.title = trainingTitle

        trainingTitle?.let { initializeTrainingData(it) }

        buttonStartTrainingFragment.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val dialog = progressDialogChanges()

        val call = trainingData()?.let { RetrofitInitializer().apiService().post(it) }
        call!!.enqueue(object: Callback<Bounces?> {
            override fun onResponse(call: Call<Bounces?>?,
                                    response: Response<Bounces?>?) {
                dialog.dismiss()

                response?.body()?.let {

                    saveBounceLocationInDatabase(it.bounces, it.id_trainingResult)
                }
            }

            override fun onFailure(call: Call<Bounces?>?,
                                   t: Throwable?) {
                dialog.dismiss()

                Toast.makeText(context, "Erro na conexão com o lançador!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveBounceLocationInDatabase(bounces : List<BounceLocation>, id_trainingResult : Int) {
        val title = trainingPositionTrainingFragment.text

        if(title != null && title.isNotEmpty()) {
            // Open the realm for the UI thread.
            var realm = Realm.getDefaultInstance()


            try {
                realm.executeTransaction { realm ->
                    // Add a Training
                    var trainingResult = realm.where<TrainingResult>().equalTo("id", id_trainingResult).findFirst()

                    for(bounce in bounces){
                        if (trainingResult != null) {
                            trainingResult.bouncesLocations.add(bounce)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("Erro", e.message)
            }

        } else {
            Toast.makeText(context, "Erro ao salvar locais onde a bolinha pingou", Toast.LENGTH_LONG).show()
        }
    }

    private fun nextId(): Int {
        realm = Realm.getDefaultInstance()
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

        val launcherPosition = parseLauncherPositionString(trainingPositionTrainingFragment.text.toString())
        val shots = listOf(trainingShotOneTrainingFragment.text.toString(),
                                    trainingShotTwoTrainingFragment.text.toString(),
                                    trainingShotThreeTrainingFragment.text.toString(),
                                    trainingShotFourTrainingFragment.text.toString(),
                                    trainingShotFiveTrainingFragment.text.toString(),
                                    trainingShotSixTrainingFragment.text.toString(),
                                    trainingShotSevenTrainingFragment.text.toString(),
                                    trainingShotEightTrainingFragment.text.toString(),
                                    trainingShotNineTrainingFragment.text.toString(),
                                    trainingShotTenTrainingFragment.text.toString())



        val ip : String = activity.intent.extras.getString("IP")
        val mac : String = activity.intent.extras.getString("MAC")

        var realm = Realm.getDefaultInstance()


        try {
            realm.executeTransaction { realm ->
                // Add a Training
                val trainingResult = realm.createObject<TrainingResult>()
                trainingResult.title = trainingTitle
                trainingResult.id = nextId()
                trainingResult.shots = training.shots
                trainingResult.dateTrainingResult = Date()
            }
        } catch (e: Exception) {
            Log.d("Erro", e.message)
        }


        var trainingDataApi = launcherPosition?.let { TrainingDataApi(1, it+1, shots, ip, mac) }

        return trainingDataApi
    }

    private fun progressDialogChanges(): ProgressDialog {
        var dialog = createDialog("Enviando informações para o lançador!")

        val handler = Handler()
        handler.postDelayed({ dialog.setMessage("Iniciando treinamento!") }, 1000)
        handler.postDelayed({ dialog.setMessage("Analizando jogadas!") }, 1000)


        return dialog
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

    private fun initializeTrainingData(trainingTitle: String) {
        training = takeTrainingInDatabase(trainingTitle)

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
        var realm = Realm.getDefaultInstance()

        val results = realm!!.where<Training>().`in`("title", arrayOf(trainingTitle)).findFirst()

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

    private fun parseLauncherPositionString(position: String): Int? {
        var launcherPositionString : Int? = null
        when(position) {
            "Esquerda" -> launcherPositionString = 0
            "Centro" -> launcherPositionString = 1
            "Direita" -> launcherPositionString = 2
        }

        return launcherPositionString
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
