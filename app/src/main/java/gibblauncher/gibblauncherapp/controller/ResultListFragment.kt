package gibblauncher.gibblauncherapp.controller


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import gibblauncher.gibblauncherapp.R
import kotlinx.android.synthetic.main.fragment_result_list.*

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
            var hawkeyeResultFragment = HawkeyeResultFragment()

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, hawkeyeResultFragment)
                    .addToBackStack(null)
                    .commit()
        }
    }

    // TODO - Mudar para uma lista de resultados
    private fun results(): List<String> {
        return arrayListOf("Resultado um", "Resultado dois", "Resultado três")
    }

}
