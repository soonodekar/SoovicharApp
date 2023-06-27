package com.soonodekar.myapp1.recievers


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent



class OnTimeReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {
            if(onTimeReceiverListener != null) {
                onTimeReceiverListener!!.onTimeReached(context, intent)
            }
    }


    interface OnTimeReceiverListener{
        fun onTimeReached(context: Context, intent: Intent)
    }

    var onTimeReceiverListener : OnTimeReceiverListener? = null

}