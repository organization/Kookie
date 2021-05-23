package be.zvz.kookie.permission

import com.koloboke.collect.map.hash.HashObjObjMaps

class Permission(
    val name: String,
    var description: String? = null,
    val children: MutableMap<String, Boolean> = HashObjObjMaps.newMutableMap()
) {
    fun getPermissibles(): Map<String, Permissible> = PermissionManager.getPermissionSubscriptions(name)

    fun recalculatePermissibles() {
        getPermissibles().forEach {
            TODO("it.recalculatePermissions()")
        }
    }

    fun addChild(name: String, value: Boolean) {
        children[name] = value
        recalculatePermissibles()
    }

    fun removeChild(name: String) {
        children.remove(name)
        recalculatePermissibles()
    }
}
