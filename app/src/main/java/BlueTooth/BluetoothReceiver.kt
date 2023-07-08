package Bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BluetoothReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            if (state == BluetoothAdapter.STATE_ON) {
                Log.v(TAG, "BtHome state :  STATE_ON")
            }
            if (state == BluetoothAdapter.STATE_OFF) {
                Log.v(TAG, "BtHome state :  STATE_OFF")
            }
        }
    }

    companion object {
        private val TAG = BluetoothReceiver::class.java.simpleName
    }
}