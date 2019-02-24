package com.niko.dietmefordoctors.ui.profile

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.model.Doctor
import com.niko.dietmefordoctors.ui.common.activities.BaseActivity
import com.niko.dietmefordoctors.ui.main.MainActivity
import com.niko.dietmefordoctors.utils.Log
import com.niko.dietmefordoctors.utils.colorOf
import com.niko.dietmefordoctors.utils.consts.Collection
import com.niko.dietmefordoctors.utils.openActivity
import com.niko.dietmefordoctors.utils.rx.RxFirestore
import com.niko.dietmefordoctors.utils.toast
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_profile.*
import top.defaults.drawabletoolbox.DrawableBuilder

class ProfileActivity : BaseActivity() {

    private var age = 18
    private var patients = 1
    private var users: List<String> = emptyList()

    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        currentUser = FirebaseAuth.getInstance().currentUser!!

        initView()

        updateProfile()

    }

    private fun updateProfile() {
        RxFirestore[Collection.DOCTORS, currentUser.uid]
                .map { it.toObject(Doctor::class.java)!! }
                .onErrorReturnItem(Doctor())
                .doOnNext {
                    Log.d("get profile doctor : $it")
                }
                .subscribeBy(onNext = {
                    age = it.age
                    patients = it.max_users
                    users = it.users
                    editFullname.setText(it.username)
                    editEducation.setText(it.education)
                    refreshAge()
                    refreshPatients()
                }).tracked()
    }

    private fun initView() {
        agePlus.setOnClickListener {
            age = if (age == 60) 60 else ++age
            refreshAge()
        }

        ageMinus.setOnClickListener {
            age = if (age == 18) 18 else --age
            refreshAge()
        }

        patientPlus.setOnClickListener {
            patients = if (patients == 10) 10 else ++patients
            refreshPatients()
        }

        patientMinus.setOnClickListener {
            patients = if (patients == 1) 1 else --patients
            refreshPatients()
        }

        btnSave.background = DrawableBuilder()
                .rectangle()
                .rounded()
                .ripple()
                .rippleColor(colorOf(R.color.cyan_800))
                .solidColor(colorOf(R.color.cyan_600))
                .build()

        btnSave.setOnClickListener {
            if (editEducation.text.isEmpty()) {
                toast("Enter pls education")
            } else {

                val params = mapOf(
                        "id" to currentUser.uid,
                        "max_users" to patients,
                        "username" to editFullname.text.toString(),
                        "sex" to if (!switchSex.isChecked) "male" else "female",
                        "photo" to currentUser.photoUrl.toString(),
                        "email" to currentUser.email.toString(),
                        "age" to age,
                        "users" to users,
                        "education" to editEducation.text.toString()
                )

                RxFirestore.setDocument(
                        params, Collection.DOCTORS, currentUser.uid).subscribeBy(
                        onComplete = {
                            toast("Профиль успешно обновлен")
                            openActivity(MainActivity::class.java, true)
                        }, onError = {
                    Log.d("error update ${it.message}")
                    toast("Ошибка обновления профиля")
                }
                )
            }
        }
    }

    private fun refreshAge() {
        textAge.text = "$age"
    }

    private fun refreshPatients() {
        textPatients.text = "$patients"
    }
}
