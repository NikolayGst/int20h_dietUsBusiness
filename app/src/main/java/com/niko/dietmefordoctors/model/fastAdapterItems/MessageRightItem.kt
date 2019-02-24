package com.niko.dietmefordoctors.model.fastAdapterItems

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.niko.dietmefordoctors.R
import com.niko.dietmefordoctors.model.Message
import com.niko.dietmefordoctors.utils.colorOf
import com.niko.dietmefordoctors.utils.consts.Ids
import com.niko.dietmefordoctors.utils.toDp
import kotlinx.android.synthetic.main.message_right_item.view.*
import top.defaults.drawabletoolbox.DrawableBuilder
import java.text.SimpleDateFormat
import java.util.*

class MessageRightItem(val message: Message) : AbstractItem<MessageRightItem, MessageRightItem.ViewHolder>() {

    override fun getType() = Ids.MESSAGE_RIGHT_ITEM_ID

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    override fun getLayoutRes() = R.layout.message_right_item

    class ViewHolder(val view: View) : FastAdapter.ViewHolder<MessageRightItem>(view) {

        override fun bindView(item: MessageRightItem, payloads: MutableList<Any>) {

            view.bg_message.background = DrawableBuilder()
                    .rectangle()
                    .cornerRadius(view.toDp(12f))
                    .solidColor(view.colorOf(R.color.user_message_color))
                    .build()

            val timeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())

            view.time.text =  timeFormat.format(item.message.date.toDate())
            view.messageText.text = item.message.text
        }

        override fun unbindView(item: MessageRightItem) {
            view.time.text = null
            view.messageText.text = null
        }

    }
}