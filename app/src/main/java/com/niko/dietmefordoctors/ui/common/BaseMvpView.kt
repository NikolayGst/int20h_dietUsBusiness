package com.banketos.ui.common

import com.arellomobile.mvp.MvpView

interface BaseMvpView : MvpView {

    fun onError(error: Throwable)

    fun showProgress()

    fun hideProgress()

}