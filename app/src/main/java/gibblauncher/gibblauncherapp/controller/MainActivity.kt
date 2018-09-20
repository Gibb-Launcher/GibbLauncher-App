package gibblauncher.gibblauncherapp.controller

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.ActionMode
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import gibblauncher.gibblauncherapp.R
import android.widget.LinearLayout
import gibblauncher.gibblauncherapp.model.BounceLocation
import java.util.*


class MainActivity : AppCompatActivity() {
    private val myLocation = IntArray(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
<<<<<<< HEAD
=======
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        val imageView = findViewById<ImageView>(R.id.table_tennis)

        imageView.getLocationOnScreen(myLocation)
        println(myLocation[0])
        println(myLocation[1])


        val hawkeyeLayout = findViewById<RelativeLayout>(R.id.hawkeye_result)
        showBounceLocations(hawkeyeLayout)
        setContentView(hawkeyeLayout)
    }

    fun showBounceLocations(hawkeyeLayout: RelativeLayout) {
        val listOfBounceLocation: MutableList<BounceLocation> = createBounceLocations()

        for(bounceLocation in listOfBounceLocation){
            val imageView = ImageView(this)
            imageView.x=myLocation[0] + bounceLocation.axisX
            imageView.y=myLocation[1] + bounceLocation.axisY

            val parms = LinearLayout.LayoutParams(50, 50)

            imageView.layoutParams = parms

            imageView.setImageResource(R.drawable.circle_failed)

            hawkeyeLayout.addView(imageView)
        }
    }

    fun createBounceLocations(): MutableList<BounceLocation> {
        val listOfBounceLocation: MutableList<BounceLocation> = mutableListOf()

        for (index in 0..9) {
            val x = rand(0, 1000)
            val y = rand(0, 1000)
            val bounceLocation = BounceLocation(x.toFloat(), y.toFloat())
            listOfBounceLocation.add(bounceLocation)
        }
        return listOfBounceLocation
    }

    fun rand(s: Int, e: Int) = Random().nextInt(e + 1 - s) + s

>>>>>>> d13b19f... Adding bounce points in view
}
