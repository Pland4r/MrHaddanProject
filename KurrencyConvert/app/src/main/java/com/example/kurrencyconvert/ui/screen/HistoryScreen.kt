package com.example.kurrencyconvert.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kurrencyconvert.data.model.ConversionHistory
import com.example.kurrencyconvert.ui.viewmodel.CurrencyViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.kurrencyconvert.ui.components.AnimatedGradientBackground
import com.example.kurrencyconvert.ui.components.GlassCard
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: CurrencyViewModel = viewModel()
) {
    val history by viewModel.conversionHistory.collectAsState()

    AnimatedGradientBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "Conversion History",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { paddingValues ->
            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    GlassCard {
                        Text(
                            "No conversion history yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 8.dp), 
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = history,
                        key = { it.timestamp }
                    ) { conversion ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            HistoryItem(conversion = conversion)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(conversion: ConversionHistory) {
    val currencySymbols = mapOf(
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "JPY" to "¥",
        "CAD" to "$",
        "AUD" to "$",
        "DZD" to "DA", // Assuming 'DA' as a placeholder for Algerian Dinar symbol
        "BRL" to "R$",
        "CNY" to "¥",
        "INR" to "₹",
        "AOA" to "Kz" // Kwanza symbol
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White) // White background for card
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Placeholder for flags - not implemented due to lack of assets
                Text(
                    text = "${conversion.source} / ${conversion.target}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp // Adjusted font size
                    ),
                    color = Color.Black // Dark color for currency pair
                )
                Button(
                    onClick = { /* TODO: Implement action for opening conversion details */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0F7FA) // Light green background for Open button
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "Open",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00C853) // Darker green text for Open button
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start // Align to start for amounts
            ) {
                Text(
                    text = "${currencySymbols[conversion.source]}${"%.2f".format(conversion.amount)}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp // Larger font for amount
                    ),
                    color = Color.Black // Dark color for amount
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Converted to",
                    tint = Color.Gray // Gray arrow
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "${currencySymbols[conversion.target]}${"%.2f".format(conversion.result)}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp // Larger font for result
                    ),
                    color = Color.Black // Dark color for result
                )
            }
        }
    }
} 