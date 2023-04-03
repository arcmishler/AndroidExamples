package com.example.example35

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

// Pretty standard recyclerview
class WeatherRVAdapter(private val mListItems: List<WeatherTable>) :
    RecyclerView.Adapter<WeatherRVAdapter.ViewHolder>() {
    private var mContext: Context? = null

    class ViewHolder(itemLayout: View) : RecyclerView.ViewHolder(itemLayout) {
        var tempTv: TextView = itemLayout.findViewById<View>(R.id.rv_tv_temp) as TextView
        var cityTv: TextView = itemLayout.findViewById<View>(R.id.rv_tv_loc) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        val myView = layoutInflater.inflate(R.layout.recyclerview_layout, parent, false)
        return ViewHolder(myView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = mListItems[position]
        holder.cityTv.text = current.location
        holder.tempTv.text = "" + (JSONWeatherUtils.getWeatherData(current.weatherJson).temperature.temp - 273.15).roundToInt() + "C"
    }


    override fun getItemCount(): Int {
        return mListItems.size
    }
}