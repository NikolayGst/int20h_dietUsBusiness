package com.niko.dietmefordoctors.model

data class User(
    val username: String = "",
    val age: Int = 0,
    val photo: String = "",
    val activity: Int = 0,
    val diet_id: String? = null,
    val doctor_id: String? = null,
    val height: Int = 0,
    val weight: Int = 0,
    val sex: String = "")
