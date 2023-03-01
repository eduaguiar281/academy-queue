package io.arcotech.academyqueue.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.github.javafaker.Faker
import java.time.LocalDateTime


data class Mensagem(
    @JsonProperty("nomeRemetente")
    val nomeRemetente:String = Faker().name().fullName(),
    @JsonProperty("mensagem")
    val mensagem: String = "Olá Mundo",
    @JsonProperty("dataHoraMensagem")
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val dataHoraMensagem: LocalDateTime = LocalDateTime.now()
)
