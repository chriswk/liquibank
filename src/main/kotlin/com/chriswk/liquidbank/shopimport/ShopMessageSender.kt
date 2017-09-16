package com.chriswk.liquidbank.shopimport

import com.chriswk.liquidbank.stock.StockMessageSender
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.io.File
import java.time.Instant

@Component
class ShopMessageSender(@Value("\${shop.path}") val shopFile: File,
                        @Value("\${shop.timestamp.file}") val lastUpdatedFile: File,
                        val kafka: KafkaTemplate<String, String>,
                        val csvParser: ShopCsvParser,
                        val objectMapper: ObjectMapper) {
    val logger = LoggerFactory.getLogger(ShopMessageSender::class.java)

    fun sendLines() {
        if (!lastUpdatedFile.exists() || shopFile.lastModified() > lastUpdatedFile.lastModified()) {
            csvParser.parseStock().forEach {
                kafka.send("shops", it.shopId, objectMapper.writeValueAsString(it))
                logger.debug("Sending ${it.shopname}")
            }
        } else {
            logger.info("Had already processed file at ${lastUpdatedFile.lastModified()}")
        }
        lastUpdatedFile.writeText(Instant.now().toEpochMilli().toString())
    }
}