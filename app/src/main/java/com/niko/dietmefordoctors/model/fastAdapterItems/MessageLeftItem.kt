package com.niko.dietmefordoctors.model.fastAdapterItems

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.model.Message
import com.niko.dietmefordoctors.utils.colorOf
import com.niko.dietmefordoctors.utils.consts.Ids
import com.niko.dietmefordoctors.utils.toDp
import kotlinx.android.synthetic.main.message_left_item.view.*
import top.defaults.drawabletoolbox.DrawableBuilder
import java.text.SimpleDateFormat
import java.util.*


class MessageLeftItem(val message: Message) : AbstractItem<MessageLeftItem, MessageLeftItem.ViewHolder>() {

    override fun getType() = Ids.MESSAGE_LEFT_ITEM_ID

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    override fun getLayoutRes() = R.layout.message_left_item

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<MessageLeftItem>(view) {

        override fun bindView(item: MessageLeftItem, payloads: MutableList<Any>) {
            view.bg_message.background = DrawableBuilder()
                    .rectangle()
                    .cornerRadius(view.toDp(12f))
                    .solidColor(view.colorOf(R.color.cyan_600))
                    .build()

            val timeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())


            view.time.text =  timeFormat.format(item.message.date.toDate())
            view.messageText.text = item.message.text
        }

        override fun unbindView(item: MessageLeftItem) {
            view.time.text = null
            view.messageText.text = null
            view.bg_message.background = null
        }

    }
}