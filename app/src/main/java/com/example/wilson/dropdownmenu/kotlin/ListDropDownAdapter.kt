package com.example.wilson.dropdownmenu.kotlin

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.wilson.dropdownmenu.R

/**
 * Created by ggg on 2018/12/6.
 */
class ListDropDownAdapter constructor(context: Context, list: List<String>) : BaseAdapter() {

    private var context: Context = context
    private var list: List<String> = list
    var checkPosition: Int = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): String {
        return list[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder
        var view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_drop_down, null)
            viewHolder = ViewHolder()
            viewHolder.tv = view.findViewById(R.id.tv)
            viewHolder.tv.gravity = Gravity.CENTER
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        fillValue(position, viewHolder)
        return view

    }

    private fun fillValue(index: Int, viewHolder: ViewHolder) {
        viewHolder.tv.text = list[index]
        if (checkPosition != -1) {
            if (checkPosition == index) {
                viewHolder.tv.setTextColor(context.resources.getColor(R.color.drop_drop_selected))
                viewHolder.tv.setBackgroundResource(R.color.check_bg)
            } else {
                viewHolder.tv.setTextColor(context.resources.getColor(R.color.drop_drop_unselected))
                viewHolder.tv.setBackgroundResource(R.color.white)
            }
        }

    }

    class ViewHolder {
        lateinit var tv: TextView
    }

}