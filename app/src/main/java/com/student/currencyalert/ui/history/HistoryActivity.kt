package com.student.currencyalert.ui.history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HistoryScreen()
            }
        }
    }
}

enum class TimeWindow(val label: String, val days: Int) {
    ONE_DAY("1 Day", 1),
    ONE_WEEK("1 Week", 7),
    ONE_MONTH("1 Month", 30),
    ONE_YEAR("1 Year", 365),
    FIVE_YEARS("5 Years", 1825)
}

class DateAxisValueFormatter(
    private val timestamps: List<Long>,
    private val timeWindow: TimeWindow
) : ValueFormatter() {
    
    private val dateFormat = when (timeWindow) {
        TimeWindow.ONE_DAY -> SimpleDateFormat("h a", Locale.US)
        TimeWindow.ONE_WEEK -> SimpleDateFormat("MMM d", Locale.US)
        TimeWindow.ONE_MONTH -> SimpleDateFormat("MMM d", Locale.US)
        TimeWindow.ONE_YEAR -> SimpleDateFormat("MMM", Locale.US)
        TimeWindow.FIVE_YEARS -> SimpleDateFormat("yyyy", Locale.US)
    }
    
    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        return if (index >= 0 && index < timestamps.size) {
            dateFormat.format(Date(timestamps[index]))
        } else {
            ""
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedWindow by remember { mutableStateOf(TimeWindow.ONE_WEEK) }
    val context = androidx.compose.ui.platform.LocalContext.current
    
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) {
        android.graphics.Color.WHITE
    } else {
        android.graphics.Color.BLACK
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Exchange Rate History") },
            navigationIcon = {
                IconButton(onClick = { (context as? ComponentActivity)?.finish() }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
        
        // Time window selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TimeWindow.values().forEach { window ->
                FilterChip(
                    selected = selectedWindow == window,
                    onClick = { 
                        selectedWindow = window
                        viewModel.loadHistory(window.days)
                    },
                    label = { Text(window.label) }
                )
            }
        }
        
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Text("Error: ${uiState.error}")
            }
            else -> {
                AndroidView(
                    factory = { context ->
                        LineChart(context).apply {
                            description.isEnabled = false
                            setTouchEnabled(true)
                            isDragEnabled = true
                            setScaleEnabled(true)
                            
                            xAxis.position = XAxis.XAxisPosition.BOTTOM
                            xAxis.granularity = 1f
                            xAxis.setLabelCount(6, false)
                            xAxis.textColor = textColor
                            axisLeft.textColor = textColor
                            axisRight.textColor = textColor
                            legend.textColor = textColor
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    update = { chart ->
                        val entries = uiState.historyData.mapIndexed { index, rate ->
                            Entry(index.toFloat(), rate.rate.toFloat())
                        }
                        
                        val timestamps = uiState.historyData.map { it.timestamp }
                        
                        val dataSet = LineDataSet(entries, "CAD to KRW").apply {
                            color = android.graphics.Color.BLUE
                            setCircleColor(android.graphics.Color.BLUE)
                            lineWidth = 2f
                            circleRadius = 4f
                            valueTextColor = textColor
                        }
                        
                        chart.xAxis.valueFormatter = DateAxisValueFormatter(timestamps, selectedWindow)
                        chart.xAxis.textColor = textColor
                        chart.axisLeft.textColor = textColor
                        chart.axisRight.textColor = textColor
                        chart.legend.textColor = textColor
                        
                        chart.data = LineData(dataSet)
                        chart.invalidate()
                    }
                )
            }
        }
    }
}
