package be.zvz.kookie.nbt.tag

import be.zvz.kookie.nbt.NbtDataException

class ReaderTracker(private val maxDepth: Int) {
    private var currentDepth = 0
    fun protectDepth(execute: () -> Unit) {
        if (maxDepth > 0 && ++currentDepth > maxDepth) {
            throw NbtDataException("Nesting level too deep: reached max depth of this.maxDepth tags")
        }
        try {
            execute()
        } finally {
            --currentDepth
        }
    }
}
