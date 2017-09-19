package com.chriswk.liquidbank.stock

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StockController(val stockService: StockService) {

    @GetMapping("/producers")
    fun producers() = stockService.producerAggregate()

}