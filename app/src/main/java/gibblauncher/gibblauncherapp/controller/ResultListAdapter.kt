package gibblauncher.gibblauncherapp.controller

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gibblauncher.gibblauncherapp.R
import kotlinx.android.synthetic.main.result_item.view.*

// TODO - Mudar a lista recebida para uma lista de resultados
class ResultListAdapter(private val results: List<String>,
                          private val context: Context) : RecyclerView.Adapter<ResultListAdapter.ViewHolder>() {
    // TODO - Mudar String para result
    var onItemClick: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if(results != null){
            val result = results[position]

            holder?.let {
                it.bindView(result)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.result_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (results != null){

            return results.size
        } else {
            return 0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(results[adapterPosition])
            }
        }
        // TODO - Mudar o result para result
        fun bindView(result: String) {
            val resultNumber = itemView.resultItemTitle

            resultNumber.text = result
        }
    }

}
