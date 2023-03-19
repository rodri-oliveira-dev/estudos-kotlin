data class ExemploObjeto(
    val nome: String,
    val idade: Int,
    val email: String,
    val subObjeto: ExemploSubObjeto,
    val listaSubObjeto: List<ExemploSubObjeto>,
    val sublista: List<ExemploSubObjetoComLista>
)

data class ExemploSubObjeto(
    val campo1: String,
    val campo2: Int
)

data class ExemploSubObjetoComLista(
    val campo3: List<ExemploSubObjeto>
)

fun main(args: Array<String>) {
    val subObjeto = ExemploSubObjeto("Texto", 42)
    val subComLista = ExemploSubObjetoComLista(listOf(subObjeto, subObjeto))
    val objeto = ExemploObjeto(
        "João", 25, "joao@email.com",
        subObjeto,
        listOf(subObjeto, subObjeto, subObjeto),
        listOf(subComLista, subComLista)
    )
    val campos = setOf("idade", "campo1")

    val parse = CensuraJson3(campos)
    val json = parse.toJson(objeto)
    println(json)
}
