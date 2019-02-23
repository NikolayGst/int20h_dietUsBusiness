package com.niko.dietmefordoctors.ui.common.fragments

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment : Fragment() {

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

    override fun onDestroyView() {
        super.onDestroyView()
        unsubscribeAll()
    }

}