package be.zvz.kookie.lang

class TranslationContainer(
    val text: String,
    val params: List<String> = listOf(), // TODO: Receive String, Float, Int
) {
    fun getParameter(index: Int): String = params[index]
}
