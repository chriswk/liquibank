package com.chriswk.liquidbank

import com.chriswk.liquidbank.shopimport.ShopDownloader
import com.chriswk.liquidbank.stock.StockDownloader
import com.chriswk.liquidbank.stock.StockMessageSender
import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class LiquidbankApplication

fun main(args: Array<String>) {
    System.setProperty("es.set.netty.runtime.available.processors", "false")
    val ctx = SpringApplication.run(LiquidbankApplication::class.java, *args)
    val stockDownloader: StockDownloader = ctx.getBean<StockDownloader>()
    val stockMessageSender: StockMessageSender = ctx.getBean<StockMessageSender>()
    val shopDownloader: ShopDownloader = ctx.getBean<ShopDownloader>()
    stockDownloader.refreshCsv()
    shopDownloader.refreshCsv()
    stockMessageSender.sendLines()
}
