package com.example.minion.droneapp

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager

class GraphicActivity : AppCompatActivity() {

    private lateinit var canvas: MyCanvas
    private val TAG = "GraphicActivity"
    private lateinit var weather: Weather
    override fun onCreate(savedInstanceState: Bundle?) {


        askForPermission(Manifest.permission.INTERNET, 1)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        lowBatteryNotification()
        //dronConnected()
        weather = Weather()
        weather.start()
        canvas = MyCanvas(this, description, temperature, wind)
        lowBatteryNotification()
        setContentView(canvas)
    }

    private fun lowBatteryNotification() {

        val ncbuilder = NotificationCompat.Builder(this)
            .setContentText("Napětí na baterii je příliš nízké!!!")
            .setSmallIcon(R.drawable.notification_icon_background)

        val notIntent = Intent(this, GraphicActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        ncbuilder.setContentIntent(pIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, ncbuilder.build())

    }

    private fun dronConnected() {

        val ncbuilder2 = NotificationCompat.Builder(this)
            .setContentText("Jste připojeni k vašemu dronovi!!")
            .setSmallIcon(R.drawable.notification_icon_background)

        val notIntent2 = Intent(this, GraphicActivity::class.java)
        val pIntent2 = PendingIntent.getActivity(this, 0, notIntent2, PendingIntent.FLAG_UPDATE_CURRENT)
        ncbuilder2.setContentIntent(pIntent2)

        val manager2 = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager2.notify(0, ncbuilder2.build())


    }

    private fun askForPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@GraphicActivity, permission) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this@GraphicActivity, permission)) {


                ActivityCompat.requestPermissions(this@GraphicActivity, arrayOf(permission), requestCode)

            } else {

                ActivityCompat.requestPermissions(this@GraphicActivity, arrayOf(permission), requestCode)
            }
        } else {
            Log.d(TAG, "Jupppp" + Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        var weatherStr: String? = null
        var temperature = "0"
        var wind = "0"
        var description = "ne"
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.BlueTooth -> {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.PepaActivity -> {

            }
            R.id.Log -> {
                startActivity(Intent(this,LogActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
