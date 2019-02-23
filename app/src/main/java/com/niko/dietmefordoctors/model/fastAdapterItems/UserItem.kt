package com.niko.dietmefordoctors.model.fastAdapterItems

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.model.User
import com.niko.dietmefordoctors.utils.consts.Ids

class UserItem(user: User) : AbstractItem<UserItem, UserItem.ViewHolder>() {

    override fun getType() = Ids.USER_ITEM_ID

    override fun getViewHolder(v: View) = ViewHolder(v)

    override fun getLayoutRes() = R.layout.user_item

    class ViewHolder(view: View) : FastAdapter.ViewHolder<UserItem>(view) {

        override fun bindView(item: UserItem, payloads: MutableList<Any>) {

        }

        override fun unbindView(item: UserItem) {

        }



    }
}