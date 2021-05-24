package be.zvz.kookie.lang

import be.zvz.kookie.utils.Union

class TranslationContainer(
    val text: String,
    array: List<Union.U3<String, Float, Int>>,
) {
    val params: List<String>

    init {
        params = mutableListOf<String>().apply {
            array.forEach { raw ->
                raw.use(
                    { add(it) },
                    { add(it.toString()) },
                    { add(it.toString()) },
                )
            }
        }
    }

    fun getParameter(index: Int): String = params[index]
}
