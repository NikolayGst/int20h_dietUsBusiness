package com.niko.dietmefordoctors.ui.common.activities

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseActivity  : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Метод для сохранения подписок в коллекцию
     */
    fun Disposable.tracked() {
        compositeDisposable.add(this)
    }

    /**
     * Если есть подписки, отписываем их
     */
    private fun unsubscribeAll() {
        //Log.v("Отписываемся от подписок: " + compositeDisposable.size())
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeAll()
    }

}