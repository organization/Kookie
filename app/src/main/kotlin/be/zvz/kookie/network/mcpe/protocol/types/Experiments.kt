package be.zvz.kookie.network.mcpe.protocol.types

import be.zvz.kookie.network.mcpe.serializer.PacketSerializer
import com.koloboke.collect.map.hash.HashObjObjMaps

class Experiments(private val experiments: MutableMap<String, Boolean>, private val hasPreviouslyUsedExperiments: Boolean) {

    fun getExperiments(): MutableMap<String, Boolean> = experiments

    fun hasPreviouslyUsedExperiments(): Boolean = hasPreviouslyUsedExperiments

    fun write(output: PacketSerializer) {
        experiments.forEach { (experimentName, value) ->
            output.putString(experimentName)
            output.putBoolean(value)
        }
        output.putBoolean(hasPreviouslyUsedExperiments)
    }

    companion object {
        fun read(input: PacketSerializer): Experiments {
            val experiments: MutableMap<String, Boolean> = HashObjObjMaps.newMutableMap()
            for (i in 0 until input.getLInt()) {
                val experimentName = input.getString()
                val enabled = input.getBoolean()
                experiments[experimentName] = enabled
            }
            val hasPreviouslyUsedExperiments = input.getBoolean()
            return Experiments(experiments, hasPreviouslyUsedExperiments)
        }
    }
}
