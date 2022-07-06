package com.kakao.adfit.publisher.sample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val items = listOf(
            Item("Banner Sample (Kotlin)", BannerSampleActivity::class.java),
            Item("Banner Sample (Java)", BannerJavaSampleActivity::class.java),
            Item("Native AD Sample (Kotlin)", NativeAdSampleActivity::class.java),
            Item("Native AD Sample (Java)", NativeAdJavaSampleActivity::class.java),
            Item("BizBoard AD Sample (Kotlin)", BizBoardAdSampleActivity::class.java),
            Item("BizBoard AD Sample (Java)", BizBoardAdJavaSampleActivity::class.java),
            Item("About", AboutActivity::class.java)
        )

        setContentView(R.layout.activity_main)

        recyclerView.adapter = Adapter(items)
        recyclerView.layoutManager = LayoutManager(this)
        recyclerView.addItemDecoration(ItemDecoration(this))
    }

    class Item<T : Activity>(val text: String, private val activityClass: Class<T>) {

        fun onClick(context: Context) {
            context.startActivity(Intent(context, activityClass))
        }
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    class Adapter(private val items: List<Item<out Activity>>) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val density = parent.context.resources.displayMetrics.density
            val horizontalMargin = (16 * density).toInt()
            val verticalMargin = (12 * density).toInt()

            val textView = TextView(parent.context)
            textView.textSize = 23f
            textView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            textView.setPadding(horizontalMargin, verticalMargin, horizontalMargin, horizontalMargin)

            return ViewHolder(textView)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            val textView = holder.textView
            textView.text = item.text
            textView.setOnClickListener { item.onClick(it.context) }
        }
    }

    class LayoutManager(context: Context) : LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    class ItemDecoration(context: Context) : DividerItemDecoration(context, VERTICAL)
}
