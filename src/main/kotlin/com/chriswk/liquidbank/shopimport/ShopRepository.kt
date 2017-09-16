package com.chriswk.liquidbank.shopimport

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository


interface ShopRepository : ElasticsearchRepository<Shop, String>