package gibblauncher.gibblauncherapp.controller

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast

import gibblauncher.gibblauncherapp.R
import kotlinx.android.synthetic.main.fragment_play.*

class PlayFragment : Fragment(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seekBarFragmentPlay!!.setOnSeekBarChangeListener(this)
        buttonNextFragmentPlay!!.setOnClickListener(this)
    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when(progress){
            0 -> {
                imageViewFragmentPlayLeft.visibility = View.VISIBLE
                imageViewFragmentPlayCenter.visibility = View.INVISIBLE
                imageViewFragmentPlayRight.visibility = View.INVISIBLE
            }
            1 -> {
                imageViewFragmentPlayLeft.visibility = View.INVISIBLE
                imageViewFragmentPlayCenter.visibility = View.VISIBLE
                imageViewFragmentPlayRight.visibility = View.INVISIBLE
            }
            2 -> {
                imageViewFragmentPlayLeft.visibility = View.INVISIBLE
                imageViewFragmentPlayCenter.visibility = View.INVISIBLE
                imageViewFragmentPlayRight.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(v: View?) {
        context.toast("Button")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}
