package AwsConfigThing

import com.amazonaws.regions.Regions

class AwsConfigConstants {
    companion object {
        const val CUSTOMER_SPECIFIC_ENDPOINT = "a1frq7egwcqa3o-ats.iot.ap-south-1.amazonaws.com"
        const val COGNITO_POOL_ID = "ap-south-1:6fc9785b-d815-4104-9600-1f80e65b9b63"
        val MY_REGION = Regions.AP_SOUTH_1
        var UUID_FILTER = "00001234-0000-1000-8000-00805f9b34fb"
        const val SET_CONFIG = "sdk/Falcon/setconfig"
        const val GET_CONFIG = "sdk/Falcon/getconfig"
    }
}