package com.yourpackage.customerconnectmobile.data.model // Adjust package name

import com.google.gson.annotations.SerializedName

data class AnalyticsSummary(
    @SerializedName("total_customers")
    val totalCustomers: Int,
    @SerializedName("new_this_week")
    val newThisWeek: Int
)