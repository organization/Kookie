package be.zvz.kookie.network.mcpe.convert

class SkinAdapterSingleton {

    companion object {
        var adapter: SkinAdapter? = null
            get() {
                if (field == null) {
                    adapter = LegacySkinAdapter()
                }
                return adapter
            }
            set(value: SkinAdapter?) {
                field = value
            }
    }
}
