package com.niko.dietmefordoctors.ui.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.model.Doctor
import com.niko.dietmefordoctors.model.User
import com.niko.dietmefordoctors.model.fastAdapterItems.UserItem
import com.niko.dietmefordoctors.ui.common.activities.BaseActivity
import com.niko.dietmefordoctors.utils.Log
import com.niko.dietmefordoctors.utils.consts.Collection
import com.niko.dietmefordoctors.utils.gone
import com.niko.dietmefordoctors.utils.rx.RxFirestore
import com.niko.dietmefordoctors.utils.toast
import com.niko.dietmefordoctors.utils.visible
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var currentUser: FirebaseUser

    private lateinit var fastAdapter: FastAdapter<UserItem>
    private lateinit var itemAdapter: ItemAdapter<UserItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentUser = FirebaseAuth.getInstance().currentUser!!

        initRecycler()

        getList()

    }

    private fun initRecycler() {
        itemAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(itemAdapter)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = fastAdapter

        swipe.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)

        swipe.setOnRefreshListener {
            getList()
        }

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

        itemAdapter.clear()

        if (users.isEmpty()) {
            emptyList.visible()
            recycler.gone()
        } else {
            emptyList.gone()
            recycler.visible()

            users.forEach { itemAdapter.add(UserItem(it)) }
        }
    }
}
