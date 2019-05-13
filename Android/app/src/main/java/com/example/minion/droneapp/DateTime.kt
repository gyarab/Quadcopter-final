package com.example.minion.droneapp

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.RequiresApi
import java.lang.NumberFormatException
import java.time.LocalDateTime.*

@TargetApi(Build.VERSION_CODES.O)
class DateTime {
    private var dateTime = now()
    private var firstMinute = 0
    private var firstHour = 0
    private var firstSecond = 0
    private var estimatedSeconds:Long = 0
    private var lastSecond:Int = 0
    init {
        firstMinute = dateTime.minute
        firstHour = dateTime.hour
        firstSecond = dateTime.second
        lastSecond = firstSecond
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTime():String{
        dateTime = now()
        return "${toOrdinaryNumberFormat(dateTime.hour)}:${toOrdinaryNumberFormat(dateTime.minute)}:${toOrdinaryNumberFormat(dateTime.second)}:${nanoFormat(dateTime.nano/1000000)}"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getConnectedTime():String{
        if (lastSecond!= now().second){
            lastSecond = now().second
            estimatedSeconds++
        }
        var calculationSeconds = estimatedSeconds
        var hours = calculationSeconds/3600
        calculationSeconds %= 3600
        var minutes = calculationSeconds/60
        calculationSeconds %= 60
        var seconds = calculationSeconds

        return "${toOrdinaryNumberFormat(hours)}:${toOrdinaryNumberFormat(minutes)}:${toOrdinaryNumberFormat(seconds)}"
    }
    private fun toOrdinaryNumberFormat(number:Long) = if (number<10) "0$number" else "$number"
    private fun toOrdinaryNumberFormat(number:Int) = if (number<10) "0$number" else "$number"
    private fun nanoFormat(number: Int) = if(number<10) "00$number" else if (number<100) "0$number" else "$number"

}