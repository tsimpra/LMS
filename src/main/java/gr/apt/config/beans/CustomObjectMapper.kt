package gr.apt.config.beans

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import javax.enterprise.inject.Produces
import javax.inject.Singleton

class CustomObjectMapper {
    // Replaces the CDI producer for ObjectMapper built into Quarkus
    @Singleton
    @Produces
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
        mapper.findAndRegisterModules()
        return mapper
    }
}