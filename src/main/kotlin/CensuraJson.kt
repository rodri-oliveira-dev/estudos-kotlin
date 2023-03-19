import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

class CensuraJson(private val names: Set<String>) {

    private val gson = GsonBuilder()
        .serializeNulls()
        .create()

    fun toJson(classOrDataClass: Any?): String {
        if (classOrDataClass == null) {
            return "O primeiro parâmetro não pode ser nulo."
        }

        val jsonTree = gson.toJsonTree(classOrDataClass)
        modificaJsonTree(jsonTree)
        return gson.toJson(jsonTree)
    }

    private fun modificaJsonTree(jsonElement: JsonElement) {
        when (jsonElement) {
            is JsonObject -> {
                for ((key, value) in jsonElement.entrySet()) {
                    if (names.contains(key)) {
                        if (value is JsonObject) {
                            for ((subKey, subValue) in value.entrySet()) {
                                value.addProperty(subKey, "oculto")
                            }
                        } else {
                            jsonElement.addProperty(key, "oculto")
                        }
                    } else {
                        modificaJsonTree(value)
                    }
                }
            }

            is JsonArray -> {
                for (value in jsonElement) {
                    modificaJsonTree(value)
                }
            }
        }
    }
}
