package com.niko.dietmefordoctors.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.model.Message
import com.niko.dietmefordoctors.model.fastAdapterItems.MessageLeftItem
import com.niko.dietmefordoctors.model.fastAdapterItems.MessageRightItem
import com.niko.dietmefordoctors.ui.common.activities.BaseActivity
import com.niko.dietmefordoctors.utils.Log
import com.niko.dietmefordoctors.utils.colorOf
import com.niko.dietmefordoctors.utils.consts.Collection
import com.niko.dietmefordoctors.utils.rx.RxFirestore
import com.niko.dietmefordoctors.utils.toDp
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_chat.*
import top.defaults.drawabletoolbox.DrawableBuilder

class ChatActivity : BaseActivity() {

    private var userId: String = ""

    private lateinit var fastAdapter: FastAdapter<IItem<*, *>>
    private lateinit var itemAdapter: ItemAdapter<IItem<*, *>>

    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        currentUser = FirebaseAuth.getInstance().currentUser!!

        if (intent.extras != null) {
            userId = intent.extras.getString("userId")
        }

        back.setOnClickListener {
            finish()
        }

        lrEdit.background = DrawableBuilder()
                .rectangle()
                .strokeWidth(2)
                .cornerRadius(toDp(6f))
                .strokeColor(colorOf(R.color.grey_200))
                .build()

        initRecycler()

        sendMessage.setOnClickListener {

            if (editMessage.text.isNotEmpty()) {
                send(editMessage.text.toString())
            }

            editMessage.text = null

        }

        loadMessages()

    }

    private fun send(msg: String) {
        RxFirestore.setDocumentWithRandomId(mapOf(
                "date" to Timestamp.now(),
                "text" to msg,
                "uid" to currentUser.uid
        ), Collection.DIETS, currentUser.uid, Collection.USERS, userId, "chat").subscribeBy(onComplete={
            Log.d("suscces")
        }, onError = {
            Log.d("error send message: ${it.message}")
        }).tracked()
    }

    private lateinit var messageListener: ListenerRegistration

    private fun loadMessages() {
        messageListener = FirebaseFirestore.getInstance().collection(Collection.DIETS).document(currentUser.uid)
                .collection(Collection.USERS).document(userId)
                .collection("chat")
                .addSnapshotListener { querySnapshot, e ->

                    var messages = emptyList<Message>()

                    if (querySnapshot != null && !querySnapshot.isEmpty) {
                        messages = querySnapshot.toObjects(Message::class.java)
                    }

                    setMessages(messages)
                }
    }

    private fun initRecycler() {
        itemAdapter = ItemAdapter()

        fastAdapter = FastAdapter.with(itemAdapter)

        recyclerMessages.layoutManager = LinearLayoutManager(this)
        recyclerMessages.adapter = fastAdapter
        recyclerMessages.itemAnimator = null

    }

    private fun setMessages(messages: List<Message>) {
        itemAdapter.clear()

        messages.sortedBy { message -> message.date }.forEach {
            when (it.uid) {
                currentUser.uid -> itemAdapter.add(MessageRightItem(it))
                else -> itemAdapter.add(MessageLeftItem(it))
            }
        }

        if (fastAdapter.itemCount != 0) {
            recyclerMessages.scrollToPosition(fastAdapter.itemCount - 1)
        }

    }
}

