package com.niko.dietmefordoctors.model

import com.google.firebase.Timestamp

class Message(val date: Timestamp = Timestamp.now(), val text: String = "", val uid: String = "")
