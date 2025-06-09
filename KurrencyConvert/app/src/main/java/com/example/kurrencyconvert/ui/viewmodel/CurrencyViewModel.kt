package com.example.kurrencyconvert.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kurrencyconvert.data.api.CurrencyApi
import com.example.kurrencyconvert.data.model.ConversionHistory
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.asStateFlow

class CurrencyViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.exchangerate.host/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(CurrencyApi::class.java)
    private val database = FirebaseDatabase.getInstance().reference.child("conversions")
    private val apiKey = "f53fe76372b717ca79c99d06d9c266ea"

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()

    private val _sourceCurrency = MutableStateFlow("USD")
    val sourceCurrency: StateFlow<String> = _sourceCurrency.asStateFlow()

    private val _targetCurrency = MutableStateFlow("EUR")
    val targetCurrency: StateFlow<String> = _targetCurrency.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _conversionHistory = MutableStateFlow<List<ConversionHistory>>(emptyList())
    val conversionHistory: StateFlow<List<ConversionHistory>> = _conversionHistory

    private val _currencies = MutableStateFlow(listOf(
        "USD","MAD" ,"EUR", "GBP", "JPY", "CAD", "AUD", "DZD", "BRL", "CNY", "INR"
    )) // Hardcoded currency list
    val currencies: StateFlow<List<String>> = _currencies.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val history = mutableListOf<ConversionHistory>()
                for (child in snapshot.children) {
                    child.getValue(ConversionHistory::class.java)?.let {
                        history.add(it)
                    }
                }
                _conversionHistory.value = history.sortedByDescending { it.timestamp }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = "Failed to load history: ${error.message}"
            }
        })
    }

    fun updateAmount(newAmount: String) {
        _amount.value = newAmount
    }

    fun selectSourceCurrency(currency: String) {
        _sourceCurrency.value = currency
    }

    fun selectTargetCurrency(currency: String) {
        _targetCurrency.value = currency
    }

    fun swapCurrencies() {
        val temp = _sourceCurrency.value
        _sourceCurrency.value = _targetCurrency.value
        _targetCurrency.value = temp
    }

    fun convertCurrency() {
        val amountValue = _amount.value.toDoubleOrNull()
        if (amountValue != null && amountValue > 0) {
            _isLoading.value = true
            _error.value = null
            viewModelScope.launch {
                try {
                    val response = api.convertCurrency(
                        _sourceCurrency.value,
                        _targetCurrency.value,
                        amountValue,
                        apiKey
                    )
                    if (response.success) {
                        _result.value = "%.2f".format(response.result)
                        saveToFirebase(_sourceCurrency.value, _targetCurrency.value, amountValue, response.result)
                    } else {
                        _error.value = "Conversion failed. Please check your input or try again."
                    }
                } catch (e: Exception) {
                    _error.value = e.message ?: "An error occurred during conversion"
                } finally {
                    _isLoading.value = false
                }
            }
        } else {
            _error.value = "Please enter a valid amount"
        }
    }

    private fun saveToFirebase(from: String, to: String, amount: Double, result: Double) {
        val conversion = ConversionHistory(
            source = from,
            target = to,
            amount = amount,
            result = result,
            timestamp = System.currentTimeMillis()
        )
        database.push().setValue(conversion)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Conversion saved to Firebase: $conversion")
                } else {
                    println("Failed to save conversion to Firebase: ${task.exception?.message}")
                }
            }
    }

    fun clearError() {
        _error.value = null
    }
} 