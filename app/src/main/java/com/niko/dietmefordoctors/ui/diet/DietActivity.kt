package com.niko.dietmefordoctors.ui.diet

import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.ui.common.activities.BaseActivity
import com.niko.dietmefordoctors.utils.Log
import com.niko.dietmefordoctors.utils.colorOf
import com.niko.dietmefordoctors.utils.consts.Collection
import com.niko.dietmefordoctors.utils.forEach
import com.niko.dietmefordoctors.utils.rx.RxFirestore
import com.niko.dietmefordoctors.utils.toast
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_diet.*
import top.defaults.drawabletoolbox.DrawableBuilder


class DietActivity : BaseActivity() {

    private var userId: String = ""

    private var position: Int = 1

    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.niko.dietmefordoctors.R.layout.activity_diet)

        currentUser = FirebaseAuth.getInstance().currentUser!!

        if (intent.extras != null) {
            userId = intent.extras.getString("userId")
        }

        monday.background = DrawableBuilder()
                .rectangle()
                .rounded()
                .solidColor(colorOf(com.niko.dietmefordoctors.R.color.white))
                .build()

        btnNext.background = DrawableBuilder()
                .rectangle()
                .rounded()
                .ripple()
                .rippleColor(colorOf(com.niko.dietmefordoctors.R.color.cyan_800))
                .solidColor(colorOf(com.niko.dietmefordoctors.R.color.cyan_600))
                .build()

        btnSave.background = DrawableBuilder()
                .rectangle()
                .rounded()
                .ripple()
                .rippleColor(colorOf(com.niko.dietmefordoctors.R.color.cyan_800))
                .solidColor(colorOf(com.niko.dietmefordoctors.R.color.cyan_600))
                .build()

        btnNext.setOnClickListener {
            handleNextPosition()
        }

        btnSave.setOnClickListener {

            val days = mapOf("days" to mapOf("monday" to mapOf("eats" to listOf(
                    mapOf("gram" to "170", "name" to "oatmeal", "type" to 0),
                    mapOf("gram" to "250", "name" to "chicken soup", "type" to 1),
                    mapOf("gram" to "70", "name" to "cottage cheese", "type" to 2))
            )))

            RxFirestore.setDocumentWithRandomId(days,
                    Collection.DIETS, currentUser.uid, Collection.USERS, userId, "diet").subscribeBy(onComplete = {
                Log.d("susccess")
                toast("success")
            }, onError = {
                Log.d("error send diet: ${it.message}")
            }).tracked()
        }
    }

    private fun handleNextPosition() {
        daysList.forEach { pos, view ->
            if (pos == position) {
                view.background = DrawableBuilder()
                        .rectangle()
                        .rounded()
                        .solidColor(colorOf(com.niko.dietmefordoctors.R.color.white))
                        .build()
                (view as Button).setTextColor(colorOf(R.color.black))
            } else {
                view.background = null
                (view as Button).setTextColor(colorOf(R.color.grey_700))
            }
        }

        daysList.getChildAt(position).parent.requestChildFocus(daysList.getChildAt(position), daysList.getChildAt(position))

        position = if (position == 6) 0 else ++position

    }
}
