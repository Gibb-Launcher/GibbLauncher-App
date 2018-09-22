package gibblauncher.gibblauncherapp.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.model.BounceLocation
import java.util.*
import android.annotation.SuppressLint
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    private var tableTennisLocation = IntArray(2)
    private var screenWidth = 0
    private var screenHeight = 0
    private var isShow: Boolean = false
    private var quantityIn: Int = 0
    private var quantityOut: Int = 0
    private var percent: Float = 0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
        getScreenMetrics()
>>>>>>> e7f8645... Adding bounce information in ball
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (!isShow) {
            val linearLayout = findViewById<LinearLayout>(R.id.table_tennis)

            linearLayout.getLocationOnScreen(tableTennisLocation)

            val hawkeyeLayout = findViewById<RelativeLayout>(R.id.hawkeye_result)
            showBounceLocations(hawkeyeLayout)
            setContentView(hawkeyeLayout)
            this.isShow = true
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    fun showBounceLocations(hawkeyeLayout: RelativeLayout) {
        val listOfBounceLocation: MutableList<BounceLocation> = createBounceLocations()


        for ((index, bounceLocation) in listOfBounceLocation.withIndex()) {
            if(bounceLocation.axisX >= 0 && bounceLocation.axisY >= 0){
                val imageView = ImageView(this)

                imageView.x = tableTennisLocation[0] + bounceLocation.axisX
                imageView.y = tableTennisLocation[1] + bounceLocation.axisY
                imageView.id = View.generateViewId()

                val params = RelativeLayout.LayoutParams(60, 60)

                imageView.setImageResource(R.drawable.circle_ball)

                imageView.setOnClickListener {
                    //Your code here

                    val toolTip = TextView(this)
                    toolTip.text = "Jogada $index"
                    val textColor = ContextCompat.getColor(this, R.color.colorText)
                    toolTip.setTextColor(textColor)

                    val layoutParams = RelativeLayout.LayoutParams(250,60)
                    val shapeDrawable = ContextCompat.getDrawable(this, R.drawable.background_text)

                    toolTip.gravity = Gravity.CENTER
                    toolTip.setPaddingRelative( 0,0,0,5)
                    toolTip.background = shapeDrawable


                    if(imageView.x + 250 > screenWidth){
                        toolTip.x = screenWidth - 270.0f
                    } else if(imageView.x < 100) {
                        toolTip.x = 0.0f
                    } else {
                        toolTip.x = imageView.x - 100
                    }

                    toolTip.y = imageView.y - 70

                    hawkeyeLayout.addView(toolTip, layoutParams)

                    Handler().postDelayed({
                        hawkeyeLayout.removeView(toolTip)
                    }, 1000L)
                }


                hawkeyeLayout.addView(imageView, params)
            }

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

    fun getScreenMetrics() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
    }

    fun rand(s: Int, e: Int) = Random().nextInt(e + 1 - s) + s

>>>>>>> d13b19f... Adding bounce points in view
}
