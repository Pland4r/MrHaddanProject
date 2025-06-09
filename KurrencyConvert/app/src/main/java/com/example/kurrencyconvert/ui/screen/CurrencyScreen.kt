package com.example.kurrencyconvert.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kurrencyconvert.ui.viewmodel.CurrencyViewModel
import com.example.kurrencyconvert.ui.components.AnimatedGradientBackground
import com.example.kurrencyconvert.ui.components.GlassCard
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.text.Placeholder
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(
    onNavigateToHistory: () -> Unit,
    viewModel: CurrencyViewModel = viewModel()
) {
    val amount by viewModel.amount.collectAsState()
    val result by viewModel.result.collectAsState()
    val sourceCurrency by viewModel.sourceCurrency.collectAsState()
    val targetCurrency by viewModel.targetCurrency.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currencies by viewModel.currencies.collectAsState()

    AnimatedGradientBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "Currency\nConverter",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007BFF)
                    ),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Text(
                    text = "From",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                var expandedFrom by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedFrom,
                    onExpandedChange = { expandedFrom = !expandedFrom },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = sourceCurrency,
                        onValueChange = { /* Not directly editable in this design */ },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFrom) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color(0xFFF0F0F0),
                            unfocusedContainerColor = Color(0xFFF0F0F0),
                            focusedTextColor = Color.DarkGray,
                            unfocusedTextColor = Color.DarkGray,
                            focusedLabelColor = Color.DarkGray,
                            unfocusedLabelColor = Color.DarkGray,
                            focusedTrailingIconColor = Color.DarkGray,
                            unfocusedTrailingIconColor = Color.DarkGray
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedFrom,
                        onDismissRequest = { expandedFrom = false }
                    ) {
                        currencies.forEach { currency ->
                            DropdownMenuItem(text = { Text(currency) }, onClick = {
                                viewModel.selectSourceCurrency(currency)
                                expandedFrom = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "To",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                var expandedTo by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedTo,
                    onExpandedChange = { expandedTo = !expandedTo },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = targetCurrency,
                        onValueChange = { /* Not directly editable in this design */ },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTo) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color(0xFFF0F0F0),
                            unfocusedContainerColor = Color(0xFFF0F0F0),
                            focusedTextColor = Color.DarkGray,
                            unfocusedTextColor = Color.DarkGray,
                            focusedLabelColor = Color.DarkGray,
                            unfocusedLabelColor = Color.DarkGray,
                            focusedTrailingIconColor = Color.DarkGray,
                            unfocusedTrailingIconColor = Color.DarkGray
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTo,
                        onDismissRequest = { expandedTo = false }
                    ) {
                        currencies.forEach { currency ->
                            DropdownMenuItem(text = { Text(currency) }, onClick = {
                                viewModel.selectTargetCurrency(currency)
                                expandedTo = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Amount",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { viewModel.updateAmount(it) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color(0xFFF0F0F0),
                            unfocusedContainerColor = Color(0xFFF0F0F0),
                            focusedTextColor = Color.DarkGray,
                            unfocusedTextColor = Color.DarkGray,
                            focusedLabelColor = Color.DarkGray,
                            unfocusedLabelColor = Color.DarkGray
                        )
                    )
                    Text(text = sourceCurrency,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.convertCurrency() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00E676)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Convert",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Result",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = if (isLoading) "Loading..." else if (error != null) "Error: $error" else result.ifEmpty { "Converted Amount" },
                        onValueChange = { },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color(0xFFF0F0F0),
                            unfocusedContainerColor = Color(0xFFF0F0F0),
                            focusedTextColor = Color.DarkGray,
                            unfocusedTextColor = Color.DarkGray,
                            focusedLabelColor = Color.DarkGray,
                            unfocusedLabelColor = Color.DarkGray
                        )
                    )
                    Text(text = targetCurrency,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                    )
                }
            }
            IconButton(
                onClick = onNavigateToHistory,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 48.dp, end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "History",
                    tint = Color(0xFF007BFF),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCurrencyScreen() {
    MaterialTheme {
        CurrencyScreen(onNavigateToHistory = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyDropdown(
    selectedCurrency: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    currencies: List<String>,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCurrency,
            onValueChange = { },
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency) },
                    onClick = {
                        onCurrencySelected(currency)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
} 