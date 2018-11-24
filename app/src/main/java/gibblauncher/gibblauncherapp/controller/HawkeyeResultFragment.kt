package gibblauncher.gibblauncherapp.controller

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import gibblauncher.gibblauncherapp.R
import gibblauncher.gibblauncherapp.model.BounceLocation
import gibblauncher.gibblauncherapp.model.TrainingResult
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.exceptions.RealmMigrationNeededException
import io.realm.kotlin.where
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_hawkeye_result.*
import java.text.SimpleDateFormat
import java.util.*


class HawkeyeResultFragment : Fragment(){
    private var finish = false
    private var screenWidth = 0
    private var screenHeight = 0
    private var isShow: Boolean = false
    private var bounceLocationIn: MutableList<BounceLocation> = mutableListOf()
    private var bounceLocationOut: MutableList<BounceLocation> = mutableListOf()
    var idTrainingResult : Int = -1

    fun displayBalls(){
        if (!isShow) {
            val hawkeyeLayout = view?.findViewById<RelativeLayout>(R.id.hawkeye_result)
            idTrainingResult = arguments.getInt("id")
            showHeader()
            showBounceLocations(hawkeyeLayout)
            this.isShow = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        println(isVisibleToUser)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        getScreenMetrics()

        return inflater.inflate(R.layout.fragment_hawkeye_result, container, false)
    }

    private fun showHeader() {
        var result = takeResultsInDatabase()
        name_training.text = result.title
        hour_hawkeye.text = formatHour(result.dateTrainingResult!!)
        date_training.text = formatDate(result.dateTrainingResult!!)
    }

    private  fun formatDate(date : Date) : String{
        val dateFormat = SimpleDateFormat("dd MMM yyyy")
        return dateFormat.format(date)
    }

    private  fun formatHour(date : Date) : String{
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        return dateFormat.format(date)
    }

    private fun showBounceLocations(hawkeyeLayout: RelativeLayout?) {
        val listOfBounceLocation: MutableList<BounceLocation> = takeResultsInDatabase().bouncesLocations

        for ((index, bounceLocation) in listOfBounceLocation.withIndex()) {
            if (bounceLocation.x!! >= 0 && bounceLocation.y!! >= 0) {
                val imageView = ImageView(this.context)

                imageView.x = bounceLocation.x!!
                imageView.y = screenHeight + 50.0f
                imageView.id = View.generateViewId()

                val params = RelativeLayout.LayoutParams(60, 60)

                imageView.setImageResource(R.drawable.circle_ball)

                imageView.setOnClickListener {
                    showTipBounceLocation(index, imageView, hawkeyeLayout)
                }

                imageView.layoutParams = params as ViewGroup.LayoutParams?

                hawkeyeLayout?.addView(imageView)

                generateAnimationInBounceLocation(imageView, bounceLocation, index)
                bounceLocationIn.add(bounceLocation)

                val handler = Handler()
                handler.postDelayed(Runnable {
                    val bounceIn = view?.findViewById<TextView>(R.id.field_in)
                    val bounceout = view?.findViewById<TextView>(R.id.field_out)
                    val percent = view?.findViewById<TextView>(R.id.field_percent)

                    val bouncesIn = bounceIn?.text.toString().toInt() + 1
                    val bouncesOut = bounceout?.text.toString().toInt()
                    val percentIn = bouncesIn.toFloat()*100 / (bouncesIn + bouncesOut)

                    bounceIn?.text = bouncesIn.toString()
                    percent?.text = "%.2f".format(percentIn) + "%"
                },0)
            } else{
                bounceLocationOut.add(bounceLocation)
                val handler = Handler()
                handler.postDelayed(Runnable {
                    val bounceIn = view?.findViewById<TextView>(R.id.field_in)
                    val bounceout = view?.findViewById<TextView>(R.id.field_out)
                    val percent = view?.findViewById<TextView>(R.id.field_percent)

                    val bouncesIn = bounceIn?.text.toString().toInt()
                    val bouncesOut = bounceout?.text.toString().toInt() + 1
                    val percentIn = bouncesIn.toFloat()*100 / (bouncesIn + bouncesOut)

                    bounceout?.text = bouncesOut.toString()
                    percent?.text = "%.2f".format(percentIn) + "%"
                },0)
            }
        }
    }

    private fun generateAnimationInBounceLocation(imageView: ImageView, bounceLocation: BounceLocation, index: Int) {
        val path = Path()
        path.moveTo(imageView.x + 200, screenHeight + 50.0f)

        val header = view?.findViewById<CardView>(R.id.header_hawk)

        val marginParams = header?.layoutParams as ViewGroup.MarginLayoutParams
        val y = bounceLocation.y!! + header.y + header.height + marginParams.bottomMargin

        val sideCurve = if (index % 2 == 0) -1 else 1

        path.cubicTo(imageView.x, screenHeight + 50.0f,
                imageView.x + (300 * sideCurve), y + (screenHeight - y) / 2,
                imageView.x, y)

        ObjectAnimator.ofFloat(imageView, View.X, View.Y, path).apply {
            finish = true
            duration = 0
            startDelay = 0L
            start()
        }
    }

    private fun showTipBounceLocation(index: Int, imageView: ImageView, hawkeyeLayout: RelativeLayout?) {
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

        hawkeyeLayout?.addView(toolTip, layoutParams)

        Handler().postDelayed({
            hawkeyeLayout?.removeView(toolTip)
        }, 0)
    }

    private fun createBounceLocations(): MutableList<BounceLocation> {
        val listOfBounceLocation: MutableList<BounceLocation> = mutableListOf()

        return listOfBounceLocation
    }

    private fun takeResultsInDatabase(): TrainingResult {

        var realm: Realm

        try {
            Realm.init(context)
            val config = RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build()
            realm = Realm.getInstance(config)
        } catch (ex: RealmMigrationNeededException) {
            realm = Realm.getDefaultInstance()
        }

        return realm.where<TrainingResult>().equalTo("id", idTrainingResult).findFirst()!!
    }

    private fun getScreenMetrics() {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
    }

    override fun onResume() {
        super.onResume()

        if (view == null) {
            return
        }

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                return if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    activity.onBackPressed()

                    true
                } else false
            }
        })
    }

}
