package com.yourpackage.customerconnectmobile.data.remote // Adjust package name

import com.yourpackage.customerconnectmobile.data.model.* // Import your data models
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse>

    @GET("customers")
    suspend fun getCustomers(): Response<List<Customer>>

    @POST("customers")
    suspend fun createCustomer(@Body customer: Map<String, String>): Response<Customer> // Or: @Body customer: CreateCustomerRequest

    @GET("customers/{id}")
    suspend fun getCustomer(@Path("id") customerId: Int): Response<Customer>

    @PUT("customers/{id}")
    suspend fun updateCustomer(@Path("id") customerId: Int, @Body customer: Map<String, String>): Response<Customer> // Or: @Body customer: UpdateCustomerRequest

    @DELETE("customers/{id}")
    suspend fun deleteCustomer(@Path("id") customerId: Int): Response<ApiResponse>

    @GET("analytics/summary")
    suspend fun getAnalyticsSummary(): Response<AnalyticsSummary>
}