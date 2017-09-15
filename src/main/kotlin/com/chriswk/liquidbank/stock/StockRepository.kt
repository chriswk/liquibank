package com.chriswk.liquidbank.stock

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface StockRepository : ElasticsearchRepository<StockItem, Long>