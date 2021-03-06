package juit.webkiosk.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.Image
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

import juit.webkiosk.R
import juit.webkiosk.model.AttendanceResponseSingle

/**
 * Created by puneet on 05/10/17.
 */

class TimetableAdapter(private val mContext: Context, private val attendanceList: List<AttendanceResponseSingle>, private val listener: OnItemClickListener) : RecyclerView.Adapter<TimetableAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: AttendanceResponseSingle)
        fun onInfoButtonClick(item: AttendanceResponseSingle)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var subjectName: TextView
        var subjectCode: TextView
        var attDetail: TextView
        var progressBar: ProgressBar
        var infoBtn: ImageButton
        var viewBackground: RelativeLayout? = null
        var viewForeground: RelativeLayout? = null

        init {
            subjectName = itemView.findViewById<TextView>(R.id.subjectName) as TextView
            subjectCode = itemView.findViewById<TextView>(R.id.subjectCode) as TextView
            attDetail = itemView.findViewById<TextView>(R.id.attDetail) as TextView
            progressBar = itemView.findViewById<ProgressBar>(R.id.progress) as ProgressBar
            infoBtn = itemView.findViewById<ImageButton>(R.id.info) as ImageButton
            //infoBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.delete));
            infoBtn.visibility = View.GONE
        }

        fun bind(attendanceResponseSingle: AttendanceResponseSingle, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.onItemClick(attendanceResponseSingle) }
            infoBtn.setOnClickListener { listener.onInfoButtonClick(attendanceResponseSingle) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_attendance_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(attendanceList[position], listener)
        val attDetails = attendanceList[position]
        holder.subjectName.text = attDetails.name
        holder.subjectCode.text = attDetails.code
        val total = attDetails.Total
        val lect = attDetails.Lecture
        val tut = attDetails.Tutorial

        try {
            val iAtt = Integer.valueOf(total)!!
            holder.progressBar.progress = iAtt
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setProgressBarColor(holder.progressBar, iAtt)
            } else {
                if (iAtt <= 60) {
                    holder.progressBar.progressDrawable.setColorFilter(
                            Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
                }
                if (iAtt <= 80 && iAtt >= 60) {
                    holder.progressBar.progressDrawable.setColorFilter(
                            Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN)
                }
                if (iAtt >= 80) {
                    holder.progressBar.progressDrawable.setColorFilter(
                            Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
                }
            }

            var _attDet = ""
            if (lect != null && !lect.isEmpty()) {
                _attDet = _attDet + "Lecture : " + lect + "   |   "
            }
            if (tut != null && !tut.isEmpty()) {
                _attDet = _attDet + "Tutorial : " + tut + "   |   "
            }
            _attDet = _attDet + "Total : " + total
            holder.attDetail.text = _attDet
        } catch (e: Exception) {
            holder.progressBar.progress = 0
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun setProgressBarColor(progressBar: ProgressBar, iAtt: Int) {
        if (iAtt <= 60) {
            progressBar.progressTintList = ColorStateList.valueOf(Color.RED)
        }
        if (iAtt <= 80 && iAtt >= 60) {
            progressBar.progressTintList = ColorStateList.valueOf(Color.YELLOW)
        }
        if (iAtt >= 80) {
            progressBar.progressTintList = ColorStateList.valueOf(Color.GREEN)
        }
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }

}
