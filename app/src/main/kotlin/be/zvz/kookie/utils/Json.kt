package be.zvz.kookie.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder

internal object Json {
    internal val jsonMapper: ObjectMapper = jacksonMapperBuilder()
        .addModule(AfterburnerModule())
        .build()
}
