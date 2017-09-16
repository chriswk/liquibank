package com.chriswk.liquidbank.stock

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.io.File
import java.time.Instant

@Component
class StockMessageSender(@Value("\${wine.stock.path}") val stockFile: File,
                         @Value("\${stock.timestamp.file}") val timestampFile: File,
                         val kafkaTemplate: KafkaTemplate<String, String>,
                         val csvParser: StockCsvParser,
                         val objectMapper: ObjectMapper) {
    val logger = LoggerFactory.getLogger(StockMessageSender::class.java)
    fun sendLines() {
        if (!timestampFile.exists() || timestampFile.lastModified() < stockFile.lastModified()) {
            csvParser.parseStock().forEach {
                kafkaTemplate.send("shop.stock.product.${sanity(it.productType)}", it.productId.toString(), objectMapper.writeValueAsString(it))
                logger.debug("Sending ${it.productName}")
            }
        } else {
            logger.info("Already processed current file")
        }
        timestampFile.writeText(Instant.now().toEpochMilli().toString())
    }

    fun sanity(topicName: String?) = io.prometheus.client.Collector.sanitizeMetricName(topicName)

}