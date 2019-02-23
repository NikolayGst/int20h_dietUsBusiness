package com.niko.dietmefordoctors.ui.auth

import android.content.Intent
import android.os.Bundle
import com.banketos.ui.common.activities.BaseActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.GoogleAuthProvider
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.utils.Log
import com.niko.dietmefordoctors.utils.rx.RxAuth
import com.niko.dietmefordoctors.utils.toast
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleApiClient: GoogleApiClient? = null

    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        initGoogleSignIn()

        signIn.setOnClickListener {
            startGoogleAuthorization()
        }
    }

    /**
     * Метод инициализации гугл авторизации
     */
    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("664032155490-r3gm2v7uu89pat9skvgkolqf7smm9mn6.apps.googleusercontent.com")
            .requestScopes(Scope(Scopes.PROFILE), Scope(Scopes.EMAIL))
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    private fun startGoogleAuthorization() {
        Log.v("get google sign in data")
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                Log.d("onSuccess load GoogleSignInAccount: " + result.signInAccount!!.idToken!!)
                onSuccessGoogleAuthorization(account!!.email, account.idToken)
            } else {
                Log.v("Google Sign In failed")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onSuccessGoogleAuthorization(email: String?, idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        RxAuth.signInWithCredential(credential)
            .subscribeBy(
                onNext = {

                }, onError = {
                    toast("ошибка авторизации пользователя")
                }
            ).tracked()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}
