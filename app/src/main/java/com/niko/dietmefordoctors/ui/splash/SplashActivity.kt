package com.niko.dietmefordoctors.ui.splash

import android.os.Bundle
import com.banketos.ui.common.activities.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.ui.auth.AuthActivity
import com.niko.dietmefordoctors.ui.main.MainActivity
import com.niko.dietmefordoctors.ui.profile.ProfileActivity
import com.niko.dietmefordoctors.utils.Log
import com.niko.dietmefordoctors.utils.consts.Collection
import com.niko.dietmefordoctors.utils.openActivity
import com.niko.dietmefordoctors.utils.rx.RxFirestore
import io.reactivex.rxkotlin.subscribeBy

class SplashActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            RxFirestore[Collection.DOCTORS, auth.currentUser!!.uid].subscribeBy(
                onNext = {
                    Log.d("doctor profile exist? ${it.exists()}")
                    if (it.exists()) {
                        Log.d("Main")
                        openActivity(MainActivity::class.java, true)
                    } else {
                        Log.d("Profile")
                        openActivity(ProfileActivity::class.java, true)
                    }
                }, onError = {
                    Log.d("error get profile doctor")
                    openActivity(ProfileActivity::class.java, true)
                }
            ).tracked()
        } else {
            Log.d("auth")
            openActivity(AuthActivity::class.java, true)
        }

    }
}
