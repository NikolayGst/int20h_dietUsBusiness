package com.niko.dietmefordoctors.ui.main

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.adapter.UserItemAdapter
import com.niko.dietmefordoctors.model.Doctor
import com.niko.dietmefordoctors.model.User
import com.niko.dietmefordoctors.ui.common.activities.BaseActivity
import com.niko.dietmefordoctors.ui.profile.ProfileActivity
import com.niko.dietmefordoctors.utils.*
import com.niko.dietmefordoctors.utils.consts.Collection
import com.niko.dietmefordoctors.utils.rx.RxFirestore
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var currentUser: FirebaseUser

    private lateinit var adapter: UserItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentUser = FirebaseAuth.getInstance().currentUser!!

        initViews()

        initViewPager()

        getList()

    }

    private fun initViews() {
        back.setOnClickListener {
            finish()
        }

        profile.setOnClickListener {
            openActivity(ProfileActivity::class.java)
        }

    }

    private fun initViewPager() {

        swipe.setColorSchemeColors(colorOf(R.color.colorPrimary),colorOf(R.color.colorAccent), colorOf(R.color.cyan_800))

        swipe.setOnRefreshListener {
            getList()
        }

        pagerUsers.setPadding(toPx(40), toPx(150), toPx(40), 0)
        pagerUsers.clipToPadding = false

        adapter = UserItemAdapter(this) {
            getList()
        }
        pagerUsers.adapter = adapter

    }

    private fun getList() {
        RxFirestore[Collection.DOCTORS, currentUser.uid]
                .map { it.toObject(Doctor::class.java)!! }
                .concatMapIterable { it.users }
                .concatMap { RxFirestore[Collection.USERS, it].map { user -> user.toObject(User::class.java)!! } }
                .toList().toObservable()
                .doOnSubscribe { swipe.isRefreshing = true }
                .doOnTerminate { swipe.isRefreshing = false }
                .subscribeBy(onNext = {
                    Log.d("list users ${it.size}")
                    onSuccessLoadUsers(it)
                }, onError = {
                    Log.d("error load users ${it.message}")
                    toast("error load users")
                })
                .tracked()

    }

    private fun onSuccessLoadUsers(users: List<User>) {
        if (users.isEmpty()) {
            emptyList.visible()
            pagerUsers.gone()
        } else {
            emptyList.gone()
            pagerUsers.visible()
            adapter.addItems(users)
        }
    }
}
