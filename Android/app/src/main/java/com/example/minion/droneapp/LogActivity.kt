package com.example.minion.droneapp

import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.shapes.Shape
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.constraint.solver.widgets.Rectangle
import android.support.v7.app.AppCompatActivity
import android.text.style.LineHeightSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*


class LogActivity:AppCompatActivity() {
    private val metrics = DisplayMetrics()
    private val handler = Handler()
    private lateinit var scrollView:LinearLayout
    private lateinit var mainLayout: ScrollView
    private var autoScrollActive = true
    override fun onCreate(savedInstanceState: Bundle?) {
        windowManager.defaultDisplay.getMetrics(metrics)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mainLayout = findViewById(R.id.Log)
        scrollView = LinearLayout(applicationContext)
        scrollView.orientation = LinearLayout.VERTICAL
        mainLayout.addView(scrollView)
        scrollView.setOnClickListener {
            autoScrollActive = false
            Toast.makeText(applicationContext,"Auto scroll vypnutý, pro jeho zapnutí klepněte dlouze na obrazovku",Toast.LENGTH_SHORT).show()
        }
        scrollView.setOnLongClickListener {
            autoScrollActive = true
            Toast.makeText(applicationContext,"Auto scroll zapnutý, pro jeho vypnutí klepněte krátce na obrazovku",Toast.LENGTH_SHORT).show()
            true
        }
        Toast.makeText(applicationContext,"Auto scroll zapnutý, pro jeho vypnutí klepněte krátce na obrazovku",Toast.LENGTH_SHORT).show()

        handler.post(runnable)
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
                startActivity(Intent(this,GraphicActivity::class.java))
                finish()
            }
            R.id.Log -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
    val runnable = object : Runnable{
        override fun run() {
            if (scrollView.childCount==1000){
                scrollView.removeViewAt(0)
                scrollView.removeViewAt(0)
            }
            Thread.sleep(20)
            if (autoScrollActive) mainLayout.smoothScrollTo(0,scrollView.height)

            scrollView.addView(createLayoutWithInfo())
            scrollView.addView(createSpace(50))
            handler.post(this)
        }
    }






    @TargetApi(Build.VERSION_CODES.O)
    fun createLayoutWithInfo():LinearLayout{
        val linearLayout = LinearLayout(applicationContext)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.minimumWidth = metrics.widthPixels
        linearLayout.setPadding(50,0,50,0)

        var drawable = GradientDrawable()
        drawable.setColor(Color.LTGRAY)
        drawable.setStroke(2,Color.BLACK)

        linearLayout.background = drawable
        val mainTextView = TextView(applicationContext)
        mainTextView.setTextColor(Color.BLACK)
        mainTextView.textSize=18f
        mainTextView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        mainTextView.text = "Přijatá data v ${dateTime.getTime()}"
        mainTextView.width = metrics.widthPixels
        linearLayout.addView(createSpace(20))
        linearLayout.addView(mainTextView)
        linearLayout.addView(createAxesTextView("Náklon osy x: ${btCommunication.getPitch()}°"))
        linearLayout.addView(createAxesTextView("Náklon osy y: ${btCommunication.getRoll()}°"))
        linearLayout.addView(createAxesTextView("Náklon osy z: ${btCommunication.getYaw()}°"))
        linearLayout.addView(createAxesTextView("Napětí na baterii: ${btCommunication.getVoltage()}V"))
        linearLayout.addView(createAxesTextView("Délka spojení: ${dateTime.getConnectedTime()}"))
        linearLayout.addView(createSpace(20))

        return linearLayout
    }
    fun createAxesTextView(text:String):TextView{
        val textView = TextView(applicationContext)
        textView.text = text
        textView.textSize = 16f
        textView.width = metrics.widthPixels
        textView.setTextColor(Color.BLACK)
        textView.textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
        return textView
    }
    fun createSpace(height:Int):Space{
        var space = Space(applicationContext)
        space.minimumHeight = height
        return space
    }
}