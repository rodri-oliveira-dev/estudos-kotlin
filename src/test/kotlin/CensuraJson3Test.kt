import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CensuraJson3Test {

    private lateinit var censuraJson3: CensuraJson3

    @BeforeEach
    fun setUp() {
        censuraJson3 = CensuraJson3(setOf("password", "secret"))
    }

    @Test
    fun toJson_NullObject_ReturnsEmptyJsonObject() {
        val json = censuraJson3.toJson(null)
        assertEquals("{}", json)
    }

    @Test
    fun toJson_EmptyNames_ReturnsOriginalJson() {
        val json = """{"username":"john","password":"1234","age":25.0}"""
        val obj = Gson().fromJson(json, Any::class.java)
        val censuraJson3 = CensuraJson3(emptySet())
        val result = censuraJson3.toJson(obj)
        assertEquals(json, result)
    }

    @Test
    fun toJson_JsonWithSensitiveData_ReturnsJsonWithSensitiveDataCensored() {
        val json =
            """{"username":"john", "password":"1234", "address":{"street":"Elm Street"}, "secret":"42"}"""
        val expected =
            """{"username":"john","password":"oculto","address":{"street":"Elm Street"},"secret":"oculto"}"""
        val obj = Gson().fromJson(json, Any::class.java)
        val result = censuraJson3.toJson(obj)
        assertEquals(expected, result)
    }

    @Test
    fun toJson_JsonWithArray_ReturnsJsonWithArrayCensored() {
        val json = """{"username":"john","password":"1234","hobbies":["swimming","reading","gaming"]}"""
        val expected = """{"username":"john","password":"oculto","hobbies":["oculto","oculto","oculto"]}"""
        val obj = Gson().fromJson(json, Any::class.java)
        val result = censuraJson3.toJson(obj)
        assertEquals(expected, result)
    }
}
