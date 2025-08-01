package com.yourpackage.customerconnectmobile // Adjust package name

import ...

class MainActivity : ComponentActivity() {

    private val customerViewModel: CustomerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomerConnectMobileTheme {
                // Observe toast messages from ViewModel
                val toastMessage by customerViewModel.toastMessage.observeAsState()
                androidx.compose.runtime.LaunchedEffect(toastMessage) {
                    toastMessage?.let {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                        customerViewModel.onToastMessageShown() // Reset after showing
                    }
                }

                // Simple navigation based on login status for now
                val loginResult by customerViewModel.loginStatus.observeAsState()
                var showLoginScreen by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(true) }

                androidx.compose.runtime.LaunchedEffect(loginResult) {
                    if (loginResult is LoginResult.Success) {
                        showLoginScreen = false // Navigate away from login
                        // TODO: Navigate to Customer List Screen
                        customerViewModel.fetchCustomers() // Fetch customers after successful login
                        customerViewModel.fetchAnalytics()
                    }
                }

                if (showLoginScreen) {
                    LoginScreen(customerViewModel)
                } else {
                    // Replace with your actual Customer List Screen or Dashboard
                    CustomerDashboardScreen(customerViewModel)
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun LoginScreen(viewModel: CustomerViewModel) {
    var username by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var password by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    val loginResult by viewModel.loginStatus.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.Text("Login to CustomerConnect", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

        androidx.compose.material3.OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { androidx.compose.material3.Text("Username") },
            singleLine = true
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))

        androidx.compose.material3.OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { androidx.compose.material3.Text("Password") },
            singleLine = true,
            // visualTransformation = PasswordVisualTransformation() // For password hiding
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            androidx.compose.material3.CircularProgressIndicator()
        } else {
            androidx.compose.material3.Button(onClick = { viewModel.login(username, password) }) {
                androidx.compose.material3.Text("Login")
            }
        }

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
        when (val result = loginResult) {
            is LoginResult.Error -> {
                androidx.compose.material3.Text(result.errorMessage, color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
            is LoginResult.Success -> {
                // Handled by LaunchedEffect in MainActivity to navigate
                // Text(result.message, color = MaterialTheme.colorScheme.primary)
            }
            LoginResult.Loading -> {
                // Optionally show a different loading indicator or text here
            }
            null -> {
                // Initial state
            }
        }
    }
}

// Placeholder for where you'd go after login
@androidx.compose.runtime.Composable
fun CustomerDashboardScreen(viewModel: CustomerViewModel) {
    val customers by viewModel.customers.observeAsState(initial = emptyList())
    val analytics by viewModel.analyticsSummary.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.Text("Customer Dashboard", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

        analytics?.let {
            androidx.compose.material3.Text("Total Customers: ${it.totalCustomers}")
            androidx.compose.material3.Text("New This Week: ${it.newThisWeek}")
        }
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

        if (isLoading && customers.isEmpty()) { // Show loading only if customers are not yet loaded
            androidx.compose.material3.CircularProgressIndicator()
        } else if (customers.isEmpty()) {
            androidx.compose.material3.Text("No customers found or not logged in.")
        } else {
            androidx.compose.material3.Text("Customers:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            customers.forEach { customer ->
                androidx.compose.material3.Text("${customer.name} - ${customer.email}")
            }
        }
        // Add buttons to create customers, etc.
    }
}

    