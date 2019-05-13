package com.example.minion.droneapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.*
import android.widget.*

val btCommunication = BTCommunication()
lateinit var dateTime:DateTime
class MainActivity : AppCompatActivity() {

    private val metrics = DisplayMetrics()
    private val handler = Handler()
    private lateinit var root:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {

        windowManager.defaultDisplay.getMetrics(metrics)
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        root = findViewById<LinearLayout>(R.id.Main)
        if(btCommunication.isConnected()){
            makeConnectedTextView(root, btCommunication.getName())
        }else {
            setupMainTextBar(root)
            if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
                makeClickableList(root, btCommunication.getBTDevicesList())
            } else {
                makeInformTextView(root)
                handler.post(runnable)

            }
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
                handler.post(this)
            } else {
                findViewById<LinearLayout>(R.id.Main).removeViewAt(1)
                makeClickableList(findViewById(R.id.Main), btCommunication.getBTDevicesList())
                handler.removeCallbacks(this)
            }


        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.BlueTooth -> {

            }
            R.id.PepaActivity -> {
                if(btCommunication.isConnected()){
                    val intent = Intent(this, GraphicActivity::class.java)
                    startActivity(intent)
                    finish()
                }else Toast.makeText(applicationContext,"Pro zobrazení hodnot je potřeba se nejprve připojit k nějakému zařízení",Toast.LENGTH_SHORT).show()
            }
            R.id.Log -> {
                if(btCommunication.isConnected()){
                    val intent = Intent(this, LogActivity::class.java)
                    startActivity(intent)
                    finish()

                }else Toast.makeText(applicationContext,"Pro zobrazení hodnot je potřeba se nejprve připojit k nějakému zařízení",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupMainTextBar(linearLayout: LinearLayout) {
        var text = TextView(applicationContext)
        text.setTextColor(Color.BLACK)
        text.textSize = 20F
        text.height = 100
        text.text = "Vyberte váš vysílač"
        text.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        linearLayout.addView(text)
    }

    private fun makeClickableList(linearLayout: LinearLayout, pairedDevices: ArrayList<BluetoothDevice>) {
        val listView = ListView(applicationContext)
        listView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        listView.minimumWidth = metrics.widthPixels
        val pairedDevicesNames = ArrayList<String>()
        for (pairDevice in pairedDevices) pairedDevicesNames.add(pairDevice.name)
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pairedDevicesNames)
        listView.setOnItemClickListener { parent, view, position, id ->

            run {
                val selected = parent.getItemAtPosition(position) as String
                for (pairedDevice in pairedDevices) if (pairedDevice.name == selected){
                   btCommunication.connectToDevice(pairedDevice,this)

                }
            }

        }
        linearLayout.addView(listView)

    }

    private fun makeInformTextView(linearLayout: LinearLayout) {
        val textView = TextView(applicationContext)
        textView.text = "Prosím zapněte bluetooth"
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textView.textSize = 16F
        textView.gravity = Gravity.CENTER
        linearLayout.addView(textView)
    }

    private fun makeConnectedTextView(linearLayout: LinearLayout, name:String){
        val textView = TextView(applicationContext)
        textView.text = "Jste připojeni k zářízení: $name"
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textView.textSize = 20F
        textView.height = 100
        textView.setTextColor(Color.BLACK)
        textView.gravity = Gravity.CENTER
        linearLayout.addView(textView)
    }
    fun changeLayout(){
        if (btCommunication.isConnected()) {
            Toast.makeText(
                applicationContext,
                "Úspěšně jste se připojili k zařízení ${btCommunication.getName()}",
                Toast.LENGTH_SHORT
            ).show()
            root.removeAllViews()
            makeConnectedTextView(root, btCommunication.getName())
            dateTime = DateTime()
        } else Toast.makeText(applicationContext,"Nastala chyba při připojování k zařízení ${btCommunication.getName()}",Toast.LENGTH_SHORT).show()
    }
}


