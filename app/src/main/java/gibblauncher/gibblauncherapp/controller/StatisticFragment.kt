package gibblauncher.gibblauncherapp.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Description

import gibblauncher.gibblauncherapp.R
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.data.RadarData
import android.graphics.Color
import com.github.mikephil.charting.data.RadarDataSet
import android.app.DatePickerDialog
import android.util.Log
import gibblauncher.gibblauncherapp.model.BounceLocation
import gibblauncher.gibblauncherapp.model.Training
import gibblauncher.gibblauncherapp.model.TrainingResult
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_statistic.*
import java.util.*


class StatisticFragment : Fragment() {
    private var initialYear: Int? = 0
    private var initialMonth: Int? = 0
    private var initialDay: Int? = 0
    private var finalYear: Int? = 0
    private var finalMonth: Int? = 0
    private var finalDay: Int? = 0
    private lateinit var realm : Realm
    private lateinit var listTrainingResults : List<TrainingResult>
    private lateinit var listTraining : List<Training>
    private lateinit var listBounceLocation : List<BounceLocation>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_statistic, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        try {
            Realm.init(context)
            val config = RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build()
            realm = Realm.getInstance(config)
        } catch (ex: RealmMigrationNeededException) {
            realm = Realm.getDefaultInstance()
        }


        listTrainingResults = realm.where<TrainingResult>().findAll()
        listTraining = realm.where<Training>().findAll()
        listBounceLocation = realm.where<BounceLocation>().findAll()


        clickListener()
        createPieChart()
        createBarChartPosition()
        createBarChartPlay()
        createRadarChart()
        super.onViewCreated(view, savedInstanceState)
    }

    fun clickListener(){
            btnInitialDate.setOnClickListener{
                // Get Current Date
                val c = Calendar.getInstance()
                initialYear = c.get(Calendar.YEAR)
                initialMonth = c.get(Calendar.MONTH)
                initialDay = c.get(Calendar.DAY_OF_MONTH)


                val datePickerDialog = DatePickerDialog(context,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            initialDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, initialYear!!, initialMonth!!, initialDay!!)
                datePickerDialog.show()
            }

        btnFinalDate.setOnClickListener{
            // Get Current Date
            val c = Calendar.getInstance()
            finalYear = c.get(Calendar.YEAR)
            finalMonth = c.get(Calendar.MONTH)
            finalDay = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(context,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        finalDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, finalYear!!, finalMonth!!, finalDay!!)
            datePickerDialog.show()
        }


    }


    private fun createPieChart() {
        val description = Description()
        var bounceIn = 0
        var bounceOut = 0

        listBounceLocation.forEach{
            if(it.x!! > 0 && it.y!! > 0 ){
                bounceIn++
            } else {
                bounceOut++
            }
        }

        val percentIn = (100.0F*bounceIn) / listBounceLocation.size
        val percentOut = (100.0F*bounceOut) / listBounceLocation.size


        val yData = floatArrayOf(percentIn, percentOut)
        val xData = arrayOf("Dentro", "Fora")

        description.text = "Jogadas Feitas (%)"
        pieChart.description = description
        pieChart.isRotationEnabled = false
        pieChart.holeRadius = 50f
        pieChart.transparentCircleRadius = 57f


        val holeColor = ContextCompat.getColor(this.context, R.color.colorHole)

        pieChart!!.setHoleColor(holeColor)

        val yEntrys: MutableList<PieEntry> = mutableListOf()

        for (i in 0 until yData.size) {
            yEntrys.add(PieEntry(yData[i], xData[i]))
        }

        //create the data set
        val pieDataSet = PieDataSet(yEntrys, "")
        pieDataSet.colors = (ColorTemplate.MATERIAL_COLORS + ColorTemplate.COLORFUL_COLORS).asList()

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(15f)
        pieData.setValueFormatter(PercentFormatter())
        val color = ContextCompat.getColor(this.context, R.color.colorWhite)
        pieData.setValueTextColor(color)
        pieChart.isHighlightPerTapEnabled = true
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.setUsePercentValues(true)
    }

    private fun createBarChartPosition() {
        val barEntries: MutableList<BarEntry> = mutableListOf()
        val barEntries1: MutableList<BarEntry> = mutableListOf()

        // TODO get plays per position
        barEntries.add(BarEntry(1f, 100f))
        barEntries.add(BarEntry(2f, 50f))
        barEntries.add(BarEntry(3f, 58f))

        barEntries1.add(BarEntry(1f, 50f))
        barEntries1.add(BarEntry(2f, 71f))
        barEntries1.add(BarEntry(3f, 30f))

        val barDataSet = BarDataSet(barEntries, "Dentro")
        barDataSet.setColors(ContextCompat.getColor(this.context, R.color.colorGreen))
        val barDataSet1 = BarDataSet(barEntries1, "Fora")
        barDataSet1.setColors(ContextCompat.getColor(this.context, R.color.colorRed))

        val listOfPositions: MutableList<String> = mutableListOf("Esquerda", "Centro", "Direita")


        val data = BarData(barDataSet, barDataSet1)
        barChart.data = data

        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = (IndexAxisValueFormatter(listOfPositions))
        xAxis.position = (XAxis.XAxisPosition.BOTTOM)
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.isGranularityEnabled = (true)

        val barSpace = 0.0f
        val groupSpace = 0.5f

        data.barWidth = (0.25f)

        barChart.axisRight.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)

        barChart.xAxis.axisMinimum = 0f
        barChart.description.isEnabled = false
        barChart.groupBars(0f, groupSpace, barSpace)
    }

    private fun createBarChartPlay() {
        // Create bars
        // TODO get plays
        val yValues: MutableList<BarEntry> = mutableListOf()
        yValues.add(BarEntry(0f, 1f))
        yValues.add(BarEntry(1f, 10f))
        yValues.add(BarEntry(2f, 11f))
        yValues.add(BarEntry(3f, 20f))
        yValues.add(BarEntry(4f, 15f))
        yValues.add(BarEntry(5f, 18f))
        yValues.add(BarEntry(6f, 77f))

        // Create a data set
        val dataSet = BarDataSet(yValues, "")
        dataSet.setDrawValues(true)

        // Create a data object from the dataSet
        val data = BarData(dataSet)
        // Format data as percentage
        data.setValueFormatter(PercentFormatter())

        // Make the chart use the acquired data
        horizontalBarChart.data = data

        // Create the labels for the bars
        val xVals: MutableList<String> = mutableListOf()
        xVals.add("Jogada 1")
        xVals.add("Jogada 2")
        xVals.add("Jogada 3")
        xVals.add("Jogada 4")
        xVals.add("Jogada 5")
        xVals.add("Jogada 6")
        xVals.add("Jogada 99")

        // Display labels for bars
        horizontalBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(xVals)

        // Set the maximum value that can be taken by the bars
        horizontalBarChart.axisLeft.axisMaximum = 100f

        // Bars are sliding in from left to right
        horizontalBarChart.animateXY(1000, 1000)
        // Display scores inside the bars
        horizontalBarChart.setDrawValueAboveBar(false)

        // Hide grid lines
        horizontalBarChart.axisLeft.isEnabled = false
        horizontalBarChart.axisRight.isEnabled = false
        // Hide graph description
        horizontalBarChart.description.isEnabled = false
        // Hide graph legend
        horizontalBarChart.legend.isEnabled = false

        horizontalBarChart.axisRight.setDrawGridLines(false)
        horizontalBarChart.axisLeft.setDrawGridLines(false)
        horizontalBarChart.xAxis.setDrawGridLines(false)


        // Design
        dataSet.setColors(*ColorTemplate.VORDIPLOM_COLORS)
        data.setValueTextSize(13f)

        horizontalBarChart.invalidate()
    }

    private fun createRadarChart() {
        val entries: MutableList<RadarEntry> = mutableListOf()

        var countCenter = 0F
        var countLong = 0F
        var countShort = 0F

        listTrainingResults.forEach{
            it.shots!!.forEach{
                  if(it.contains("Centro")){
                      countCenter++
                  } else if(it.contains("Curto")){
                      countShort++
                  } else {
                      countLong++
                  }
            }
        }


        entries.add(RadarEntry (countCenter, "Centro"))
        entries.add(RadarEntry (countShort, "Curto"))
        entries.add(RadarEntry (countLong, "Longo"))

        val dataSet = RadarDataSet(entries, "Locais de Jogadas")
        dataSet.color = Color.BLUE
        dataSet.setDrawFilled(true)

        // Create the labels for the skills
        val xVals: MutableList<String> = mutableListOf()
        xVals.add("Centro")
        xVals.add("Curto")
        xVals.add("Longo")

        // Display labels for bars
        radarChart.xAxis.valueFormatter = IndexAxisValueFormatter(xVals)
        radarChart.yAxis.axisMinimum = 0f
        radarChart.yAxis.mAxisMaximum = 100f

        val data = RadarData(dataSet)
        radarChart.data = data
        val description = Description()
        description.text = "Skill's do Jogador"
        radarChart.description = description
        radarChart.description.isEnabled = false

        radarChart.invalidate()
    }

}
