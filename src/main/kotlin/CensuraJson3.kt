import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParseException

class CensuraJson3(private val names: Set<String>) {

    private val gson = GsonBuilder()
        .serializeNulls()
        .create()

    fun toJson(obj: Any?): String {
        if (obj == null) {
            return "{}"
        }

        return try {
            if (names.isEmpty()) {
                gson.toJson(obj)
            }

            val jsonObject = gson.toJsonTree(obj).asJsonObject
            filterJsonElement(jsonObject)
            return gson.toJson(jsonObject)
        } catch (e: JsonParseException) {
            "{}"
        }
    }

    private fun filterJsonElement(jsonElement: JsonElement) {
        when {
            jsonElement.isJsonObject -> {
                val jsonObject = jsonElement.asJsonObject
                for ((key, value) in jsonObject.entrySet()) {
                    if (names.contains(key)) {
                        jsonObject.add(key, replaceValue(value))
                    } else {
                        filterJsonElement(value)
                    }
                }
            }

            jsonElement.isJsonArray -> {
                val jsonArray = jsonElement.asJsonArray
                for ((index, value) in jsonArray.withIndex()) {
                    jsonArray.set(index, replaceValue(value))
                }
            }
        }
    }

    private fun replaceValue(value: JsonElement): JsonElement {
        return when {
            value.isJsonObject || value.isJsonArray -> {
                filterJsonElement(value)
                value
            }

            value.isJsonPrimitive -> gson.toJsonTree("oculto")
            else -> gson.toJsonTree("oculto")
        }
    }
}
