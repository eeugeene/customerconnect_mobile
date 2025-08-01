package com.yourpackage.customerconnectmobile.data.model

data class CreateCustomerRequest(
    val name: String,
    val email: String,
    val phone: String?
)