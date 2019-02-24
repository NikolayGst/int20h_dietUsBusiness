package com.niko.dietmefordoctors.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.model.User
import com.niko.dietmefordoctors.utils.colorOf
import com.niko.dietmefordoctors.utils.toDp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_item.view.*
import top.defaults.drawabletoolbox.DrawableBuilder

class UserItemAdapter(val context: Context?, val items: List<User>) : PagerAdapter() {

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun getCount() = items.size

    override fun instantiateItem(container: ViewGroup, position: Int): View {

        val item = items[position]

        val view = layoutInflater.inflate(R.layout.user_item, container, false)

      if (item.age != 0) {
          view.textYear.text = "${item.age} y.o."
      }
        view.textFullname.text = item.username
        view.textHeight.text = "${item.height} cm"
        view.textWeight.text = "${item.weight} kg"
        //view.goal.text = item.

        Picasso.get().load(item.photo).into(view.userPhoto)

        view.diet.background = DrawableBuilder()
                .rectangle()
                .cornerRadii(view.toDp(18f), 0, 0, view.toDp(18f))
                .solidColor(view.colorOf(R.color.cyan_600))
                .rippleColor(view.colorOf(R.color.cyan_800))
                .build()

        view.chat.background = DrawableBuilder()
                .rectangle()
                .cornerRadii(0, view.toDp(18f), view.toDp(18f), 0)
                .solidColor(view.colorOf(R.color.cyan_600))
                .rippleColor(view.colorOf(R.color.cyan_800))
                .build()

        container.addView(view)

        return view

    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

}