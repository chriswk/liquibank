package com.chriswk.liquidbank.shopimport

import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import io.prometheus.client.Collector
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Component
class ShopCsvParser(@Value("\${shop.path}") val stockFile: File) {
    val csvSchema = CsvSchema.builder()
            .setUseHeader(true)
            .setColumnSeparator(';')
            .build()
    val mapper = CsvMapper()

    fun parseStock(): List<Shop> {
        val iterator: MappingIterator<Map<String, String>> = mapper.readerFor(Map::class.java).with(csvSchema).readValues(stockFile)
        return iterator.readAll().map {
            Shop(
                    timestamp = it.get("Datotid")!!.toZonedDateTime(),
                    shopname = it.get("Butikknavn"),
                    shopId = Collector.sanitizeMetricName(it.getValue("Butikknavn")),
                    streetAddress = it.get("Gateadresse"),
                    postalCode = it.get("Gate_postnummer"),
                    postalCity = it.get("Gate_poststed"),
                    postalAddress = it.get("Postadresse"),
                    postalPostCode = it.get("Post_postnummer"),
                    postalPostCity = it.get("Post_poststed"),
                    phoneNo = it.get("Telefonnummer"),
                    category = it.get("Kategori"),
                    position = getPosition(it),
                    weekNumber = it.get("Ukenummer")?.toInt(),
                    weekOpening = getWeekOpening(it),
                    nextWeekNumber = it.get("Ukenummer_neste")?.toInt(),
                    nextWeekOpening = getNextWeekOpening(it)
            )
        }
    }

    private fun getNextWeekOpening(data: Map<String, String>): WeekOpening =
        WeekOpening(
                openingHoursMon = getOpening(data, "neste_mandag"),
                openingHoursTue = getOpening(data, "neste_tirsdag"),
                openingHoursWed = getOpening(data, "neste_onsdag"),
                openingHoursThu = getOpening(data, "neste_torsdag"),
                openingHoursFri = getOpening(data, "neste_fredag"),
                openingHoursSat = getOpening(data, "neste_lørdag")
        )


    private fun getOpening(data: Map<String, String>, key: String): OpeningHours? = data.get(key)?.toOpeningHours()


    private fun getWeekOpening(data: Map<String, String>): WeekOpening =
        WeekOpening(
                openingHoursMon = getOpening(data, "mandag"),
                openingHoursTue = getOpening(data, "tirsdag"),
                openingHoursWed = getOpening(data, "onsdag"),
                openingHoursThu = getOpening(data, "torsdag"),
                openingHoursFri = getOpening(data, "fredag"),
                openingHoursSat = getOpening(data, "lørdag")
        )

    private fun getPosition(data: Map<String, String>): GeoPoint? {
        val lat = data.get("GPS_breddegrad")?.replace(",", ".")?.toDouble()
        val lon = data.get("GPS_lengdegrad")?.replace(",", ".")?.toDouble()
        if (lat != null && lon != null) {
            return GeoPoint(lat, lon)
        } else {
            return null
        }
    }

}

fun String.toOpeningHours(): OpeningHours {
    val hours = this.split("-")
    return OpeningHours(hours[0].toLocalTime(), hours[1].toLocalTime())
}

fun String.toLocalTime(): LocalTime = LocalTime.parse(this, DateTimeFormatter.ofPattern("HHmm"))
fun String.toZonedDateTime(): ZonedDateTime {
    return if(!this.trim().isBlank()) {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME).atZone(ZoneOffset.UTC)
    } else {
        ZonedDateTime.now()
    }
}
