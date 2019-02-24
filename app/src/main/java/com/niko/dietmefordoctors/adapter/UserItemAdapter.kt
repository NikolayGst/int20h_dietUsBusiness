package com.niko.dietmefordoctors.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.model.Doctor
import com.niko.dietmefordoctors.model.User
import com.niko.dietmefordoctors.ui.ChatActivity
import com.niko.dietmefordoctors.ui.diet.DietActivity
import com.niko.dietmefordoctors.utils.colorOf
import com.niko.dietmefordoctors.utils.consts.Collection
import com.niko.dietmefordoctors.utils.rx.RxFirestore
import com.niko.dietmefordoctors.utils.toDp
import com.squareup.picasso.Picasso
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.user_item.view.*
import top.defaults.drawabletoolbox.DrawableBuilder

class UserItemAdapter(val context: Context?, val deleteCallback: () -> Unit) : PagerAdapter() {

    private var items = emptyList<User>()

    fun addItems(items: List<User>) {
        this.items = items
        notifyDataSetChanged()
    }

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount() = items.size

    override fun instantiateItem(container: ViewGroup, position: Int): View {

        val currentUser = FirebaseAuth.getInstance().currentUser!!

        val item = items[position]

        val view = layoutInflater.inflate(R.layout.user_item, container, false)

        if (item.age != 0) {
            view.textYear.text = "${item.age} y.o."
        }
        view.textFullname.text = item.username
        view.textHeight.text = "${item.height} cm"
        view.textWeight.text = "${item.weight} kg"
        view.goal.text = when(item.activity) {
            0 -> "lose weight"
            1 -> "normal weight"
            else -> "gain weight"
        }

        Picasso.get().load(item.photo).into(view.userPhoto)

        view.delete.background = DrawableBuilder()
                .rectangle()
                .cornerRadii(view.toDp(18f), 0, 0, view.toDp(18f))
                .solidColor(view.colorOf(R.color.cyan_600))
                .rippleColor(view.colorOf(R.color.cyan_800))
                .build()

        view.delete.setOnClickListener {

            RxFirestore[Collection.DOCTORS, currentUser.uid]
                    .map { it.toObject(Doctor::class.java)!! }
                    .flatMapCompletable {

                        it.users.remove(item.id)
                        RxFirestore.updateDocument(mapOf("users" to it.users), Collection.DOCTORS, currentUser.uid)
                    }.subscribeBy(onComplete = {
                        deleteCallback()
                    }, onError = {

                    })
        }

        view.chat.background = DrawableBuilder()
                .rectangle()
                .solidColor(view.colorOf(R.color.cyan_600))
                .rippleColor(view.colorOf(R.color.cyan_800))
                .build()

        view.diet.background = DrawableBuilder()
                .rectangle()
                .cornerRadii(0, view.toDp(18f), view.toDp(18f), 0)
                .solidColor(view.colorOf(R.color.cyan_600))
                .rippleColor(view.colorOf(R.color.cyan_800))
                .build()

        view.diet.setOnClickListener {
            val intent = Intent(context, DietActivity::class.java)
            intent.putExtra("userId", item.id)
            context!!.startActivity(intent)
        }

        view.chat.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", item.id)
            context!!.startActivity(intent)
        }

        container.addView(view)

        return view

    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }
}