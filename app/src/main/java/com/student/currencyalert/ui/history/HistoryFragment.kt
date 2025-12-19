package com.student.currencyalert.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    HistoryScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(title = { Text("7-Day History") })
        
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
                        .height(300.dp),
                    update = { chart ->
                        val entries = uiState.historyData.mapIndexed { index, rate ->
                            Entry(index.toFloat(), rate.rate.toFloat())
                        }
                        
                        val dataSet = LineDataSet(entries, "Exchange Rate").apply {
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
