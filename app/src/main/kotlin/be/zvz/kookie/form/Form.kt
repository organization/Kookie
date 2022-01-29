package be.zvz.kookie.form

abstract class Form {

    abstract fun jsonSerialize(): Map<*, *>
}
