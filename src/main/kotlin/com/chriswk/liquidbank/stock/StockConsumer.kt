package com.chriswk.liquidbank.stock

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class StockConsumer(val stockRepository: StockRepository, val objectMapper: ObjectMapper) {
    val logger: Logger = LoggerFactory.getLogger(StockConsumer::class.java)

    @KafkaListener(topicPattern = "shop\\.stock\\.product.*")
    fun listen(stockItem: ConsumerRecord<Long, String>) {
        val item = objectMapper.readValue<StockItem>(stockItem.value())
        logger.info("Indexing ${item}")
        stockRepository.index(item)
    }
}