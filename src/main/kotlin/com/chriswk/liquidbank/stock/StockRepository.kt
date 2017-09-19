package com.chriswk.liquidbank.stock

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import reactor.core.publisher.Flux

interface StockRepository : ElasticsearchRepository<StockItem, Long> {

    fun findByProducer(producer: String): Flux<StockItem>
}