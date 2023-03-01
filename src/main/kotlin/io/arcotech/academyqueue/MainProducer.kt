package io.arcotech.academyqueue

import com.fasterxml.jackson.databind.ObjectMapper
import io.arcotech.academyqueue.models.Mensagem
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun main() {
    while(true){
        val mensagem = Mensagem()
        println(mensagem)
        sendSerializedPersonToQueue("https://sqs.us-east-2.amazonaws.com/337381194561/arcoacademy.fifo", mensagem)
        Thread.sleep(2000L)
    }

}

suspend fun sendSerializedPersonToQueue(queueUrl: String, mensagem: Mensagem) {
    val sqsClient = SqsAsyncClient.builder()
        .build()

    val objectMapper = ObjectMapper()
    val messageBody = objectMapper.writeValueAsString(mensagem)

    val sendMessageRequest = SendMessageRequest.builder()
        .queueUrl(queueUrl)
        .messageBody(messageBody)
        .messageGroupId(UUID.randomUUID().toString())
        .messageDeduplicationId(UUID.randomUUID().toString())
        .build()

    return suspendCoroutine { continuation ->
        sqsClient.sendMessage(sendMessageRequest).thenApply { _ ->
            continuation.resume(Unit)
        }.exceptionally { throwable ->
            continuation.resumeWithException(throwable)
        }
    }
}