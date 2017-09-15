package com.chriswk.liquidbank.stock

import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import okhttp3.HttpUrl
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Component
class StockCsvParser(@Value("\${wine.stock.path}") val stockFile: File) {
    val csvSchema = CsvSchema.builder()
            .setUseHeader(true)
            .setColumnSeparator(';')
            .build()
    val mapper = CsvMapper()

    fun parseStock(): List<StockItem> {
        val iterator: MappingIterator<Map<String, String>> = mapper.readerFor(Map::class.java).with(csvSchema).readValues(stockFile)
        return iterator.readAll().map {
            StockItem(
                    timestamp = it.get("Datotid")!!.toZonedDateTime(),
                    productId = it.get("Varenummer")?.toLong(),
                    productName = it.get("Varenavn"),
                    productType = it.get("Varetype"),
                    volume = it.get("Volum"),
                    price = it.get("Pris"),
                    pricePerLiter = it.get("Literpris"),
                    selection = it.get("Produktutvalg"),
                    shopCategory = it.get("Butikkategori"),
                    body = it.get("Fylde")?.toInt(),
                    freshness = it.get("Friskhet")?.toInt(),
                    tannins = it.get("Garvestoffer")?.toInt(),
                    bitterness = it.get("Bitterhet")?.toInt(),
                    sweetness = it.get("Sodme")?.toInt(),
                    colour = it.get("Farge"),
                    nose = it.get("Smak"),
                    matches = getSuitedFood(it),
                    country = it.get("Land"),
                    district = it.get("Distrikt"),
                    subDistrict = it.get("Underdistrikt"),
                    vintage = it.get("Argang"),
                    mainIngredient = it.get("Rastoff"),
                    method = it.get("Metode"),
                    abv = it.get("Alkohol"),
                    sugarPerLiter = it.get("Sukker"),
                    acidPerLiter = it.get("Syre"),
                    storageHint = it.get("Lagringsgrad"),
                    producer = it.get("Produsent"),
                    engros = it.get("Grossist"),
                    distributor = it.get("Distributor"),
                    packaging = it.get("Emballasjetype"),
                    cork = it.get("Korktype"),
                    referenceUrl = it.get("Vareurl")
            )
        }
    }

    private fun getSuitedFood(data: Map<String, String>): List<String> = listOf(
            data.get("Passertil01").orEmpty(),
            data.get("Passertil02").orEmpty(),
            data.get("Passertil03").orEmpty()
    ).filter { it.isEmpty() }


}

fun String.toZonedDateTime(): ZonedDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME).atZone(ZoneOffset.UTC)