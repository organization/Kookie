package be.zvz.kookie.network.mcpe.protocol.types

class StructureEditorData {
    enum class Type(val id: Int) {
        DATA(0),
        SAVE(1),
        LOAD(2),
        CORNER(3),
        INVALID(4),
        EXPORT(5)
    }

    lateinit var structureName: String
    lateinit var structureDataField: String
    var includePlayers: Boolean = false
    var showBoundingBox: Boolean = false
    var structureBlockType: Int = 0
    lateinit var structureSettings: StructureSettings
    var structureRedstoneSaveMove: Int = 0
}
