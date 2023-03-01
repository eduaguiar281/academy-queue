package io.arcotech.academyqueue

import com.fasterxml.jackson.databind.ObjectMapper
import io.arcotech.academyqueue.models.Mensagem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.future.await
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest

suspend fun main() {
    while(true){
        println("Iniciando leitura da fila")
        receiveSerializedPersonsFromQueue("https://sqs.us-east-2.amazonaws.com/337381194561/arcoacademy.fifo")
            .collect { mensagem ->
                // Faça algo com a pessoa recebida da fila
                println("Mensagem enviada por ${mensagem.nomeRemetente}. Mensagem:${mensagem.mensagem} as ${mensagem.dataHoraMensagem}")
            }
        println("Finalizando leitura da fila")
    }
}


inline fun <reified T> ObjectMapper.readValue(json: String)  = this.readValue(json, T::class.java)

fun receiveSerializedPersonsFromQueue(queueUrl: String): Flow<Mensagem> = flow {
    val sqsClient = SqsAsyncClient.builder().build()
    val objectMapper = ObjectMapper()

    while (true) {
        Thread.sleep(1000L)
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .build()

        val messages = sqsClient.receiveMessage(receiveMessageRequest).await().messages()
        if (messages.size == 0)
            println("Nenhuma mensagem encontrada na fila")
        for (message in messages) {
            val mensagem = objectMapper.readValue<Mensagem>(message.body())
            emit(mensagem)

            val deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build()
            sqsClient.deleteMessage(deleteMessageRequest).await()
        }
    }
}
