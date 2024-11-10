package com.example.myclubdeportivo.model

data class Member(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val documentType: String,
    val documentNumber: String,
    val address: String,
    val phone: String,
    val isMember: Boolean
)
