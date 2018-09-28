package gibblauncher.gibblauncherapp.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description

import gibblauncher.gibblauncherapp.R
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.data.RadarData
import android.graphics.Color
import com.github.mikephil.charting.data.RadarDataSet


class StatisticFragment : Fragment() {
    private val yData = floatArrayOf(20f, 10f, 20f, 10f, 40f)
    private val xData = arrayOf("Jogada 1", "Jogada 2", "Jogada 3", "Jogada 4", "Jogada 5")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_statistic, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        createPieChart()
        createBarChartPosition()
        createBarChartPlay()
        createRadarChart()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun createPieChart() {
        val pieChart = view!!.findViewById<PieChart>(R.id.pie_chart)

        val description = Description()
        description.text = "Jogadas Feitas (%)"
        pieChart.description = description
        pieChart.isRotationEnabled = true
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
        val barChart = view!!.findViewById<BarChart>(R.id.bar_chart)

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
        val barChart = view!!.findViewById<HorizontalBarChart>(R.id.horizontal_bar_chart)

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
        barChart.data = data

        // Create the labels for the bars
        val xVals: MutableList<String> = mutableListOf()
        xVals.add("Jogada 1")
        xVals.add("Jogada 2")
        xVals.add("Jogada 3")
        xVals.add("Jogada 4")
        xVals.add("Jogada 5")
        xVals.add("Jogada 6")
        xVals.add("Jogada 7")

        // Display labels for bars
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xVals)

        // Set the maximum value that can be taken by the bars
        barChart.axisLeft.axisMaximum = 100f

        // Bars are sliding in from left to right
        barChart.animateXY(1000, 1000)
        // Display scores inside the bars
        barChart.setDrawValueAboveBar(false)

        // Hide grid lines
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        // Hide graph description
        barChart.description.isEnabled = false
        // Hide graph legend
        barChart.legend.isEnabled = false

        barChart.axisRight.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)


        // Design
        dataSet.setColors(*ColorTemplate.VORDIPLOM_COLORS)
        data.setValueTextSize(13f)

        barChart.invalidate()
    }

    private fun createRadarChart() {
        val entries: MutableList<RadarEntry> = mutableListOf()

        val radarChart = view!!.findViewById<RadarChart>(R.id.radar_chart)

        // TODO get training and plays
        entries.add(RadarEntry (95f, "Treinos Vencidos"))
        entries.add(RadarEntry (50f, "Jogadas da Esquerda"))
        entries.add(RadarEntry (40f, "Jogadas Certas"))
        entries.add(RadarEntry (70f, "Jogadas da Direita"))
        entries.add(RadarEntry (50f, "Paralela"))

        val dataSet = RadarDataSet(entries, "Skills")
        dataSet.color = Color.BLUE
        dataSet.setDrawFilled(true)

        // Create the labels for the skills
        val xVals: MutableList<String> = mutableListOf()
        xVals.add("Treinos Vencidos")
        xVals.add("Jogadas da Esquerda")
        xVals.add("Jogadas Certas")
        xVals.add("Jogadas da Direita")
        xVals.add("Paralela")

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
