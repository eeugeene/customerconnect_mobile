package com.yourpackage.customerconnectmobile.data.model // Adjust package name

import com.google.gson.annotations.SerializedName

data class Customer(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    @SerializedName("created_at")
    val createdAt: String
)