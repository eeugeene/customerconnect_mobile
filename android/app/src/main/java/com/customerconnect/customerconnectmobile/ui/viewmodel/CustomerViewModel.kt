package com.yourpackage.customerconnectmobile.ui.viewmodel // Adjust package name

import android.app.Application
import androidx.activity.result.launch
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yourpackage.customerconnectmobile.MainApplication // Import your Application class
import com.yourpackage.customerconnectmobile.data.model.* // Import your data models
// ApiService is already accessible via MainApplication, no direct import needed here unless you pass it differently
import kotlinx.coroutines.launch
import java.io.IOException

class CustomerViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = (application as MainApplication).apiService

    private val _customers = MutableLiveData<List<Customer>>()
    val customers: LiveData<List<Customer>> = _customers

    private val _analyticsSummary = MutableLiveData<AnalyticsSummary>()
    val analyticsSummary: LiveData<AnalyticsSummary> = _analyticsSummary

    private val _loginStatus = MutableLiveData<LoginResult>() // To communicate login success/failure more clearly
    val loginStatus: LiveData<LoginResult> = _loginStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String?>() // For simple messages
    val toastMessage: LiveData<String?> = _toastMessage

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginStatus.value = LoginResult.Loading
            try {
                val response = apiService.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body() != null) {
                    _loginStatus.value = LoginResult.Success(response.body()!!.message)
                    // Optionally fetch data immediately after login
                    // fetchCustomers()
                    // fetchAnalytics()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Invalid credentials or server error"
                    _loginStatus.value = LoginResult.Error("Login failed: ${response.code()} - $errorBody")
                }
            } catch (e: IOException) {
                _loginStatus.value = LoginResult.Error("Network error: ${e.message}")
            } catch (e: Exception) {
                _loginStatus.value = LoginResult.Error("An unexpected error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchCustomers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getCustomers()
                if (response.isSuccessful) {
                    _customers.value = response.body()
                } else {
                    _toastMessage.value = "Error fetching customers: ${response.code()}"
                }
            } catch (e: IOException) {
                _toastMessage.value = "Network error fetching customers: ${e.message}"
            } catch (e: Exception) {
                _toastMessage.value = "Unexpected error fetching customers: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createNewCustomer(name: String, email: String, phone: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val customerData = mutableMapOf("name" to name, "email" to email)
                phone?.takeIf { it.isNotBlank() }?.let { customerData["phone"] = it } // Add phone only if not null/blank

                val response = apiService.createCustomer(customerData) // Or use CreateCustomerRequest
                if (response.isSuccessful) {
                    _toastMessage.value = "Customer created successfully!"
                    fetchCustomers() // Refresh list
                } else {
                    _toastMessage.value = "Error creating customer: ${response.code()} - ${response.errorBody()?.string()}"
                }
            } catch (e: IOException) {
                _toastMessage.value = "Network error: ${e.message}"
            } catch (e: Exception) {
                _toastMessage.value = "Unexpected error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchAnalytics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getAnalyticsSummary()
                if (response.isSuccessful) {
                    _analyticsSummary.value = response.body()
                } else {
                    _toastMessage.value = "Error fetching analytics: ${response.code()}"
                }
            } catch (e: IOException) {
                _toastMessage.value = "Network error fetching analytics: ${e.message}"
            } catch (e: Exception) {
                _toastMessage.value = "Unexpected error fetching analytics: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Call this to clear the toast message after it's shown
    fun onToastMessageShown() {
        _toastMessage.value = null
    }
}

// Sealed class to represent different states of login
sealed class LoginResult {
    object Loading : LoginResult()
    data class Success(val message: String) : LoginResult()
    data class Error(val errorMessage: String) : LoginResult()
}