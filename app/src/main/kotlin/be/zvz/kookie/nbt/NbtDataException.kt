package be.zvz.kookie.nbt

class NbtDataException(
    msg: String?,
    cause: Throwable? = null
) : NbtException(msg, cause)
