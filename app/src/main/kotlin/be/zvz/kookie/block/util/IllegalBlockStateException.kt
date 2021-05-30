package be.zvz.kookie.block.util

class IllegalBlockStateException : IllegalArgumentException {
    constructor() : super()
    constructor(s: String) : super(s)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}
