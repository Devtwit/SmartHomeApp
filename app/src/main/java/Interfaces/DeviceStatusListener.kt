package Interfaces

interface DeviceStatusListener {
    fun onDeviceStatusChanged(lightStatus: String, fanStatus: String)
}