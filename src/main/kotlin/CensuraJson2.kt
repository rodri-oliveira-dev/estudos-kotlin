import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class CensuraJson2(private val names: Set<String>) {

    private val gson = GsonBuilder()
        .serializeNulls()
        .create()

    fun toJson(obj: Any?): String? {
        if (obj == null || names.isEmpty()) {
            return null
        }
        val jsonObject = gson.toJsonTree(obj).asJsonObject
        filterJsonObject(jsonObject)
        return gson.toJson(jsonObject)
    }

    private fun filterJsonObject(jsonObject: JsonObject) {
        for ((key, value) in jsonObject.entrySet()) {
            if (names.contains(key)) {
                if (value.isJsonObject) {
                    filterJsonObject(value.asJsonObject)
                } else if (value.isJsonArray) {
                    filterJsonArray(value.asJsonArray)
                } else if (value.isJsonPrimitive) {
                    value.asJsonPrimitive.apply {
                        if (isBoolean || isNumber || isString) {
                            jsonObject.addProperty(key, "oculto")
                        }
                    }
                } else {
                    jsonObject.addProperty(key, "oculto")
                }
            } else if (value.isJsonObject) {
                filterJsonObject(value.asJsonObject)
            } else if (value is JsonArray) {
                for (subValue in value) {
                    if (subValue.isJsonObject) {
                        filterJsonObject(subValue.asJsonObject)
                    }
                }
            }
        }
    }

    private fun filterJsonArray(jsonArray: JsonArray) {
        for ((index, value) in jsonArray.withIndex()) {
            if (value.isJsonObject) {
                filterJsonObject(value.asJsonObject)
            } else if (value.isJsonArray) {
                filterJsonArray(value.asJsonArray)
            } else if (value.isJsonPrimitive) {
                value.asJsonPrimitive.apply {
                    if (isBoolean || isNumber || isString) {
                        jsonArray.set(index, gson.toJsonTree("oculto"))
                    }
                }
            } else {
                jsonArray.set(index, gson.toJsonTree("oculto"))
            }
        }
    }
}
