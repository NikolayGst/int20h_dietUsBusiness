package com.niko.dietmefordoctors.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.os.Handler
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


fun Context.postAction(millis: Long = 2500, action: () -> Unit) {
    Handler().postDelayed(action, millis)
}

fun postAction(millis: Long = 2500): Completable {
    return Completable.timer(millis, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
}

fun Activity.openActivity(clazz: Class<out Activity>, closePreviousActivity: Boolean = false) {
    startActivity(Intent(this, clazz))
    if (closePreviousActivity) {
        this.finish()
    }
}

fun Fragment.openActivity(clazz: Class<out Activity>) {
    startActivity(Intent(activity, clazz))
}

inline fun ViewGroup.forEach(action: (Int, View) -> Unit) {
    for (i in 0 until childCount) {
        action(i, getChildAt(i))
    }
}

fun View.colorOf(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this.context, color)
}

fun Context.colorOf(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.toDp(size: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, resources.displayMetrics).toInt()
}

fun Context.toDp(size: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, resources.displayMetrics).toInt()
}

fun View.toSp(size: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, resources.displayMetrics).toInt()
}

fun Context.toSp(size: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, resources.displayMetrics).toInt()
}

fun Context.dimenOf(resId: Int): Float {
    return resources.getDimension(resId)
}

fun Context.dimenOffsetOf(resId: Int): Int {
    return resources.getDimensionPixelOffset(resId)
}

fun Context.toPx(size: Int): Int {
    val displayMetrics = resources.displayMetrics
    return Math.round(size * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

fun Context.getScreenSize(): Point {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

fun View.locateView(): Rect {
    val loc = IntArray(2)
    this.getLocationOnScreen(loc)
    val location = Rect()
    location.left = loc[0]
    location.top = loc[1]
    location.right = location.left + this.width
    location.bottom = location.top + this.height
    return location
}

/**
 * метод возвращает окончание для множественного числа слова на основании числа и массива
 * окончаний
 *
 * @param number Integer Число на основе которого нужно сформировать окончание
 * @param arrayRes Array Массив слов или окончаний для чисел (1, 4, 5), например ['яблоко',
 * 'яблока', 'яблок']
 * @return String
 */
fun Fragment.ending(number: Int, arrayRes: Int): String {

    val arrayEndings = resources.getStringArray(arrayRes)

    var selectedNumber = number

    val finalEnding: String
    val i: Int
    selectedNumber %= 100
    if (selectedNumber in 11..19) {
        finalEnding = arrayEndings[2]
    } else {
        i = selectedNumber % 10
        finalEnding = when (i) {
            1 -> arrayEndings[0]
            2, 3, 4 -> arrayEndings[1]
            else -> arrayEndings[2]
        }
    }
    return finalEnding
}

/**
 * метод возвращает окончание для множественного числа слова на основании числа и массива
 * окончаний
 *
 * @param number Integer Число на основе которого нужно сформировать окончание
 * @param arrayRes Array Массив слов или окончаний для чисел (1, 4, 5), например ['яблоко',
 * 'яблока', 'яблок']
 * @return String
 */
fun Context.ending(number: Int, arrayRes: Int): String {

    val arrayEndings = resources.getStringArray(arrayRes)

    var selectedNumber = number

    val finalEnding: String
    val i: Int
    selectedNumber %= 100
    if (selectedNumber in 11..19) {
        finalEnding = arrayEndings[2]
    } else {
        i = selectedNumber % 10
        finalEnding = when (i) {
            1 -> arrayEndings[0]
            2, 3, 4 -> arrayEndings[1]
            else -> arrayEndings[2]
        }
    }
    return finalEnding
}