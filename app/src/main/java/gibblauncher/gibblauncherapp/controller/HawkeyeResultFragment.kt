package gibblauncher.gibblauncherapp.controller

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.model.BounceLocation
import java.util.*
import java.util.zip.Inflater


class HawkeyeResultFragment : Fragment() {
    private var tableTennisLocation = IntArray(2)
    private var screenWidth = 0
    private var screenHeight = 0
    private var isShow: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hawkeye_result, container, false)
        val hawkeyeLayout = view.findViewById<RelativeLayout>(R.id.hawkeye_result)
        getScreenMetrics()

//        if (!isShow) {
//            val linearLayout = view.findViewById<LinearLayout>(R.id.table_tennis)
//
//            linearLayout.getLocationOnScreen(tableTennisLocation)
//
//            showBounceLocations(hawkeyeLayout)
//            this.isShow = true
//        }
        return view
    }

    private fun showBounceLocations(hawkeyeLayout: RelativeLayout) {
        val listOfBounceLocation: MutableList<BounceLocation> = createBounceLocations()

        for ((index, bounceLocation) in listOfBounceLocation.withIndex()) {
            if (bounceLocation.axisX >= 0 && bounceLocation.axisY >= 0) {
                val imageView = ImageView(this.context)

                imageView.x = tableTennisLocation[0] + bounceLocation.axisX
                imageView.y = screenHeight + 50.0f
                imageView.id = View.generateViewId()

                val params = RelativeLayout.LayoutParams(60, 60)

                imageView.setImageResource(R.drawable.circle_ball)

                imageView.setOnClickListener {
                    showTipBounceLocation(index, imageView, hawkeyeLayout)
                }

                imageView.layoutParams = params

                hawkeyeLayout.addView(imageView)

                generateAnimationInBounceLocation(imageView, bounceLocation, index)
            }
        }
    }

    private fun generateAnimationInBounceLocation(imageView: ImageView, bounceLocation: BounceLocation, index: Int) {
        val path = Path()
        path.moveTo(imageView.x + 200, screenHeight + 50.0f)
        val y = tableTennisLocation[1] + bounceLocation.axisY

        val sideCurve = if (index % 2 == 0) -1 else 1

        path.cubicTo(imageView.x, screenHeight + 50.0f,
                imageView.x + (300 * sideCurve), y + (screenHeight - y) / 2,
                imageView.x, y)

        ObjectAnimator.ofFloat(imageView, View.X, View.Y, path).apply {
            duration = 1000
            startDelay = 1000L * index
            start()
        }
    }

    private fun showTipBounceLocation(index: Int, imageView: ImageView, hawkeyeLayout: RelativeLayout) {
        val toolTip = TextView(this.context)
        toolTip.text = "Jogada $index"
        val textColor = ContextCompat.getColor(this.context, R.color.colorText)
        toolTip.setTextColor(textColor)

        val layoutParams = RelativeLayout.LayoutParams(250, 60)
        val shapeDrawable = ContextCompat.getDrawable(this.context, R.drawable.background_text)

        toolTip.gravity = Gravity.CENTER
        toolTip.setPaddingRelative(0, 0, 0, 5)
        toolTip.background = shapeDrawable

        toolTip.x = if (imageView.x + 250 > screenWidth)
            screenWidth - 270.0f
        else if (imageView.x < 100)
            0.0f
        else
            imageView.x - 100

        toolTip.y = imageView.y - 70

        hawkeyeLayout.addView(toolTip, layoutParams)

        Handler().postDelayed({
            hawkeyeLayout.removeView(toolTip)
        }, 1000L)
    }


    private fun createBounceLocations(): MutableList<BounceLocation> {
        val listOfBounceLocation: MutableList<BounceLocation> = mutableListOf()

        for (index in 0..9) {
            val x = rand(0, 1000)
            val y = rand(0, 1000)
            val bounceLocation = BounceLocation(x.toFloat(), y.toFloat())
            listOfBounceLocation.add(bounceLocation)
        }
        return listOfBounceLocation
    }

    private fun getScreenMetrics() {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
    }

    private fun rand(initial: Int, final: Int) = Random().nextInt(final + 1 - initial) + initial

}
