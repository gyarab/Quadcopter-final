package com.example.minion.droneapp

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class Weather :Thread() {
    private val TAG = "GraphicActivity"
    internal var data = ""
    internal var description = ""
    internal var wind = ""
    internal var temperature = ""
    override fun run() {
        doInBackground()
        onPostExecute()
    }
     fun doInBackground(vararg voids: Void?): Void? {
        try {
            val url:URL? =
                URL("http://api.openweathermap.org/data/2.5/weather?q=Prague,czech%20republic&appid=cd8e3911011695f53feec21b9f77b3b9&units=metric")
            if(url == null) throw IllegalArgumentException("url")
            val httpURLConnection = url?.openConnection() as HttpURLConnection
            if(httpURLConnection == null) throw IllegalArgumentException("httpURLConnection")
            val inputStream = httpURLConnection.inputStream
            if(inputStream == null) throw IllegalArgumentException("inputStream")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            if(bufferedReader == null) throw IllegalArgumentException("bufferedReader")
            var line: String? = ""
            while (line != null) {
                data += line!!
                line = bufferedReader.readLine()

            }

            val reader = JSONObject(data)
            val arr = reader.getJSONArray("weather")
            for (x in 0 until arr.length()) {
                description = arr.getJSONObject(x).getString("description")
            }
            val temp = reader.getJSONObject("main")
            temperature = temp.get("temp").toString() + ""
            val win = reader.getJSONObject("wind")
            wind = win.get("speed").toString() + ""


        } catch (e: MalformedURLException) {

            e.printStackTrace()

        } catch (e: IOException) {

            e.printStackTrace()

        } catch (e: JSONException) {

            e.printStackTrace()

        }
        catch (e:IllegalAccessException){
            e.printStackTrace()
        }

        return null
    }

     fun onPostExecute() {
        //super.onPostExecute(aVoid)
        GraphicActivity.weatherStr = this.data
        GraphicActivity.description = this.description
        GraphicActivity.temperature = this.temperature
        GraphicActivity.wind = this.wind
         Log.i("App",this.description)


    }
}
