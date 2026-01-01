package com.student.currencyalert

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.student.currencyalert.ui.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission result handled
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("CAD to KRW Exchange Rate") },
            actions = {
                IconButton(onClick = {
                    context.startActivity(
                        android.content.Intent(context, com.student.currencyalert.ui.history.HistoryActivity::class.java)
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.ShowChart,
                        contentDescription = "History"
                    )
                }
                IconButton(onClick = {
                    context.startActivity(
                        android.content.Intent(context, com.student.currencyalert.ui.settings.SettingsActivity::class.java)
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        )
        
        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isLoading),
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: ${uiState.error}")
                    }
                }
                uiState.rates?.isEmpty() == true && !uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No rates available")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.rates?.toList() ?: emptyList()) { (currency, rate) ->
                            RateItem(currency = currency, rate = rate)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RateItem(currency: String, rate: Double) {
    var cadAmount by remember { mutableStateOf("") }
    var krwAmount by remember { mutableStateOf("") }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "1 CAD",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = String.format("%.2f KRW", rate),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            Divider()
            
            OutlinedTextField(
                value = cadAmount,
                onValueChange = { 
                    cadAmount = it
                    krwAmount = it.toDoubleOrNull()?.let { cad -> 
                        String.format("%.2f", cad * rate)
                    } ?: ""
                },
                label = { Text("CAD Amount") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = krwAmount,
                onValueChange = { 
                    krwAmount = it
                    cadAmount = it.toDoubleOrNull()?.let { krw -> 
                        String.format("%.2f", krw / rate)
                    } ?: ""
                },
                label = { Text("KRW Amount") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}
