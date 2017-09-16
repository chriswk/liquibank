package com.chriswk.liquidbank.shopimport

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ShopConsumer(val es: ShopRepository, val objectMapper: ObjectMapper) {
    val logger = LoggerFactory.getLogger(ShopConsumer::class.java)!!
    @KafkaListener(topics = arrayOf("shops"))
    fun indexMessage(message: ConsumerRecord<String, String>) {
        val shop = objectMapper.readValue(message.value(), Shop::class.java)
        logger.info("Indexing $shop")
        es.index(shop)
    }
}