package com.niko.dietmefordoctors.model

data class Doctor(
        val id: String = "",
        val username: String = "",
        val max_users: Int = 1,
        val age: Int = 18,
        val users: MutableList<String> = mutableListOf(),
        val education: String = ""
)