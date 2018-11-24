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
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_result_list.*
import io.realm.exceptions.RealmMigrationNeededException



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

        recyclerView.adapter = ResultListAdapter(takeResultsInDatabase(), context)
        recyclerView.setHasFixedSize(true)

        (recyclerView.adapter as ResultListAdapter).onItemClick = { result ->
           openHawkeyeResultFragment(result)
        }
    }

    private fun openHawkeyeResultFragment(result: TrainingResult) {
        var hawkeyeResultFragment = HawkeyeResultFragment()

       var bundle = Bundle()
        bundle.putInt("id", result.id)

        hawkeyeResultFragment.arguments = bundle


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

    private fun takeResultsInDatabase(): List<TrainingResult> {
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


        val listTraining = realm?.where<TrainingResult>()?.findAll()

        return listTraining!!.reversed()
    }

}
