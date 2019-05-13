package com.example.minion.droneapp

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.TableLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MyCanvas(
    context: Context,
    internal var weather: String,
    internal var temperature: String,
    internal var wind: String
) :
    View(context) {
    internal lateinit var horizon_bm: Bitmap
    internal lateinit var attidute_bm: Bitmap
    internal lateinit var dash1_bm: Bitmap
    internal lateinit var battery_bm: Bitmap
    internal lateinit var background_bm: Bitmap
    internal var batteryLevel = btCommunication.getPercentageVoltage()
    internal var dash_width: Int = 0
    internal var dash_height: Int = 0
    internal var battery_width: Int = 0
    internal var battery_height: Int = 0
    internal var battery_x: Int = 0
    internal var battery_y: Int = 0
    internal var dash2_x: Int = 0
    internal var dash2_y: Int = 0
    internal var dash2_width: Int = 0
    internal var dash2_height: Int = 0
    internal var dash1_width: Int = 0
    internal var dash1_height: Int = 0
    internal var dash1_x: Int = 0
    internal var dash1_y: Int = 0
    internal var horizon_x: Int = 0
    internal var horizon_y: Float = 0.0.toFloat()
    internal var horizonHeight: Int = 0
    internal var horizonWidth: Int = 0
    internal var display_width: Int = 0
    internal var display_height: Int = 0
    internal var dron_attidute_width: Int = 0
    internal var dron_attidute_height: Int = 0
    internal lateinit var time: String
    internal var table: TableLayout? = null
    internal var angle = btCommunication.getRoll()

    init {
        setBackgroundColor(Color.MAGENTA)

        print(weather)
        display_width = context.resources.displayMetrics.widthPixels
        display_height = context.resources.displayMetrics.heightPixels

        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.horizon, option)

        horizonHeight = (option.outHeight/2.65).toInt()
        horizonWidth = (option.outWidth/2.65).toInt()

        horizon_x = (display_width-81) / 2 - (horizonWidth / 1)
        horizon_y = (display_height-130) / 2 - (horizon_y + horizonHeight / 2)

        BitmapFactory.decodeResource(resources, R.drawable.dron_naklon, option)

        dron_attidute_height = (option.outHeight/2.65).toInt()
        dron_attidute_width = (option.outWidth/2.65).toInt()

        BitmapFactory.decodeResource(resources, R.drawable.dash1, option)
        dash1_width = (option.outWidth/2.65).toInt()
        dash1_height = (option.outHeight/2.65).toInt()

        dash1_x = (display_width-81) / 2 - (250 + dash1_width / 2)
        dash1_y = (display_height) / 2 - (250 + dash1_height / 2)

        BitmapFactory.decodeResource(resources, R.drawable.dash, option)
        dash2_width = option.outWidth
        dash2_height = option.outHeight

        dash2_x = 0//display_width / 2 - (dash2_width + dash2_width / 2)
        dash2_y = -130//display_height / 2 - (dash2_height + dash2_height / 2)


        battery_x = display_width / 12
        battery_y = (display_height / 2.8.toFloat()).toInt()
        batteryLevel = btCommunication.getPercentageVoltage()
        BitmapFactory.decodeResource(resources, R.drawable.bat_0, option)
        battery_width = (option.outWidth/2.65).toInt()
        battery_height = (option.outHeight/2.65).toInt()


        horizon_bm = resize(BitmapFactory.decodeResource(resources, R.drawable.horizon),2.65.toFloat())
        attidute_bm = resize(BitmapFactory.decodeResource(resources, R.drawable.dron_naklon),2.65.toFloat())
        dash1_bm = resize(BitmapFactory.decodeResource(resources, R.drawable.dash1),2.65.toFloat())
        background_bm = resize(BitmapFactory.decodeResource(resources, R.drawable.dash),2.65.toFloat())


    }

    private fun resize(bitmap: Bitmap, ratio:Float):Bitmap{
        return Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width/ratio).roundToInt(),
            (bitmap.height/ratio).roundToInt(),
            false
        )
    }

    override fun onDraw(canvas: Canvas) {

        println(canvas.height.toString() + "   " + (display_height / 2 - dron_attidute_height) + "   " + (display_width / 2 - dron_attidute_width))


       if (batteryLevel >= 76)
            battery_bm = resize(BitmapFactory.decodeResource(resources, R.drawable.bat_100),2.65.toFloat())
        else if (batteryLevel <= 75 && batteryLevel >= 51)
            battery_bm = resize(BitmapFactory.decodeResource(resources, R.drawable.bat_75),2.65.toFloat())
        else if (batteryLevel <= 50 && batteryLevel >= 26)
            battery_bm = resize(BitmapFactory.decodeResource(resources, R.drawable.bat_50),2.65.toFloat())
        else if (batteryLevel <= 25 && batteryLevel >= 11)
            battery_bm = resize(BitmapFactory.decodeResource(resources, R.drawable.bat_25),2.65.toFloat())
        else if (batteryLevel <= 10 && batteryLevel >= 1)
            battery_bm = resize(BitmapFactory.decodeResource(resources, R.drawable.bat_10),2.65.toFloat())
        else
            battery_bm = resize(BitmapFactory.decodeResource(resources, R.drawable.bat_0),2.65.toFloat())
        batteryLevel = btCommunication.getPercentageVoltage()

        println(horizon_y)

        val rotator = Matrix()



        rotator.postRotate(angle.toFloat(), attidute_bm.width.toFloat()/2, attidute_bm.height.toFloat()/2)

        val xTranslate = (display_width+60) / 2 - attidute_bm.width/2
        val yTranslate = (display_height+140) / 2 - attidute_bm.height / 2

        rotator.postTranslate(xTranslate.toFloat(), yTranslate.toFloat())

        time = SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().time)
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.textSize = 50f
        canvas.drawBitmap(horizon_bm, horizon_x.toFloat(), horizon_y.toFloat(), null)
        canvas.drawBitmap(background_bm, dash2_x.toFloat(), dash2_y.toFloat(), null)

        canvas.drawBitmap(attidute_bm, rotator, null)
        canvas.drawBitmap(dash1_bm, dash1_x.toFloat(), dash1_y.toFloat(), null)

        canvas.drawBitmap(battery_bm, battery_x.toFloat(), battery_y.toFloat(), null)
        canvas.drawText(time, (display_width / 2 - 100).toFloat(), 100f, paint)
        canvas.drawText("$batteryLevel%", battery_x.toFloat(), battery_y.toFloat(), paint)
        paint.textSize = 50f
        canvas.drawText("weather: $weather", (display_width / 3).toFloat(), (display_height - 100).toFloat(), paint)
        canvas.drawText(
            "temperature: $temperature°C",
            (2 * display_width / 3).toFloat(),
            (display_height - 100).toFloat(),
            paint
        )
        canvas.drawText("wind speed: " + wind + "m/s", 20f, (display_height - 100).toFloat(), paint)

        angle = btCommunication.getRoll()
        println(horizon_y)
        horizon_y = btCommunication.getPitch().toFloat()*2// getAttiduteHorizon(horizon_y)

        invalidate()
    }

    companion object {

        fun getAttiduteHorizon(moveDirection: Int): Int {
            var moveDirection = moveDirection
            return moveDirection - 3
        }

        fun getAttiduteAngle(angle: Int): Int {
            var angle = angle
            return angle + 1
        }
    }


}
