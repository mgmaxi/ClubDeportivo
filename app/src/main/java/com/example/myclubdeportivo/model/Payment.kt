package com.example.myclubdeportivo.model

data class Payment(
    val id: Long = 0,
    val memberId: String,
    val amount: String,
    val date: String,
    val paymentMethod: String,
    val installments: String
)