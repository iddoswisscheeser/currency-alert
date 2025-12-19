package com.student.currencyalert.ui.history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedWindow by remember { mutableStateOf(TimeWindow.ONE_WEEK) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(title = { Text("Exchange Rate History") })
        
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
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    update = { chart ->
                        val entries = uiState.historyData.mapIndexed { index, rate ->
                            Entry(index.toFloat(), rate.rate.toFloat())
                        }
                        
                        val dataSet = LineDataSet(entries, "CAD to KRW").apply {
                            color = android.graphics.Color.BLUE
                            setCircleColor(android.graphics.Color.BLUE)
                            lineWidth = 2f
                            circleRadius = 4f
                        }
                        
                        chart.data = LineData(dataSet)
                        chart.invalidate()
                    }
                )
            }
        }
    }
}
