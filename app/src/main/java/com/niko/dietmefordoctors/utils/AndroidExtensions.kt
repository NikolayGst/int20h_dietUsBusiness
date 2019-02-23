package com.niko.dietmefordoctors.utils

import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.widget.Toast

fun Context.toast(@StringRes msg: Int, duration: Int = Toast.LENGTH_LONG) {
    toast(getString(msg), duration)
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, msg, duration).show()
}

fun Fragment.toast(@StringRes msg: Int, duration: Int = Toast.LENGTH_LONG) {
    toast(getString(msg), duration)
}

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireContext(), msg, duration).show()
}