package gibblauncher.gibblauncherapp.controller


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.model.TrainingResult
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_result_list.*
import java.text.SimpleDateFormat
import java.util.*

class ResultListFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_result_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Resultados"

        setRecyclerView()

    }

    private fun setRecyclerView() {
        val recyclerView = recyclerViewResults
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = ResultListAdapter(results(), context)
        recyclerView.setHasFixedSize(true)

        (recyclerView.adapter as ResultListAdapter).onItemClick = { result ->
           openHawkeyeResultFragment()
        }
    }

    private fun openHawkeyeResultFragment() {
        var hawkeyeResultFragment = HawkeyeResultFragment()

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_container, hawkeyeResultFragment)
                .addToBackStack(null)
                .commit()

        val handler = Handler()
        handler.postDelayed(Runnable {
            hawkeyeResultFragment.displayBalls()
        },1000)
    }

    // TODO - Mudar para uma lista de resultados
    private fun results(): List<String> {


        return takeResultsInDatabase()
    }

    private fun takeResultsInDatabase(): List<String> {
        val realm = Realm.getDefaultInstance()
        val results = realm.where<TrainingResult>().findAll()
        var listTrainingResult : MutableList<String> = mutableListOf()

        for(trainingResult in results){
            listTrainingResult.add(trainingResult.title!! + "  -  " + formatDate(trainingResult.dateTrainingResult!!))
        }

        return  listTrainingResult
    }

    private  fun formatDate(date : Date) : String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return dateFormat.format(date)
    }

}
