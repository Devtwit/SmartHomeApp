package Data

data class ResponseData(var location: String, val devices: Map<String, Map<String, Any>>, val address: String)
