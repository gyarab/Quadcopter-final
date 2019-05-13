package com.example.minion.droneapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.InputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class BTCommunication {

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var bluetoothSocket: BluetoothSocket
    private lateinit var inputStream: InputStream
    private lateinit var dataFromDrone: DataFromDrone
    private var connected = false
    private lateinit var btDevice: BluetoothDevice
    private val handler = Handler()
    private lateinit var mainActivity: MainActivity
    private lateinit var bluetoothDevice:BluetoothDevice

    fun getBTDevicesList(): ArrayList<BluetoothDevice> {
        if (bluetoothAdapter.isEnabled) {
            var bondedDevices = bluetoothAdapter.bondedDevices
            var list = ArrayList<BluetoothDevice>()
            for (device in bondedDevices) list.add(device)
            return list
        }
        return ArrayList()
    }

    fun connectToDevice(bluetoothDevice: BluetoothDevice, mainActivity: MainActivity) {
        this.bluetoothDevice = bluetoothDevice
        this.mainActivity = mainActivity
        handler.post(runnable)
    }

    private val runnable = Runnable {
        try {
            btDevice = bluetoothAdapter.getRemoteDevice(bluetoothDevice.address)
            bluetoothSocket =
                btDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
            bluetoothAdapter.cancelDiscovery()
            bluetoothSocket.connect()
            inputStream = bluetoothSocket.inputStream
            dataFromDrone = DataFromDrone(0.0, 0.0, 0.0, 0.0)
            DataParser(inputStream, dataFromDrone).start()
            connected = true
            mainActivity.changeLayout()
        } catch (e: Exception) {
            connected = false
            mainActivity.changeLayout()
        }
    }




    fun getPitch() = dataFromDrone.pitch
    fun getRoll() = dataFromDrone.roll
    fun getYaw() = dataFromDrone.yaw
    fun getVoltage() = dataFromDrone.voltage
    fun isConnected() = connected
    fun getName():String = btDevice.name
    fun getPercentageVoltage() = ((dataFromDrone.voltage-9)/(12.6-9)*100).toInt()


}

private data class DataFromDrone(var pitch: Double, var roll: Double, var yaw: Double, var voltage: Double)


private class DataParser(val inputStream: InputStream, var dataFromDrone: DataFromDrone) : Thread() {
    override fun run() {
        val stringsWithData = ArrayList<String>()
        while (true) {
            var s = ""
            while (true) {
                while (inputStream.available() != 0) {
                    var c = inputStream.read().toChar()
                    if (c == '\n') {
                        //Log.i("App", "$s    ${s.length}")
                        stringsWithData.add(s)
                        if (stringsWithData.size == 4) parseData(stringsWithData)
                        s = ""
                    } else s += c
                }
                sleep(10)
            }
        }

    }

    private fun parseData(arrayList: ArrayList<String>) {
        try {
            for (string in arrayList) {
                when {
                    string.contains("x") -> dataFromDrone.pitch = string.drop(4).toDouble() / 1000
                    string.contains("y") -> dataFromDrone.roll = string.drop(4).toDouble() / 1000
                    string.contains("z") -> dataFromDrone.yaw = string.drop(4).toDouble() / 1000
                    string.contains("v") -> dataFromDrone.voltage = string.drop(4).toDouble() / 100
                }
            }
            arrayList.removeAll(arrayList)
        } catch (e: Exception){}
    }
}