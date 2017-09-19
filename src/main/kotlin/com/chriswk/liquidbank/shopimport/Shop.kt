package com.chriswk.liquidbank.shopimport

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.CompletionField
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.LocalTime
import java.time.ZonedDateTime

@Document(indexName = "shops")
data class Shop(val timestamp: ZonedDateTime, @Id val shopId: String, @CompletionField val shopname: String?, val streetAddress: String?, val postalCode: String?,
                val postalCity: String?, val postalAddress: String?, val postalPostCode: String?, val postalPostCity: String?,
                val phoneNo: String?, @Field(type = FieldType.keyword) val category: String?, val position: GeoPoint?, val weekNumber: Int?,
                val weekOpening: WeekOpening?, val nextWeekNumber: Int?, val nextWeekOpening: WeekOpening?
                )
data class OpeningHours(val opens: LocalTime, val closes: LocalTime)
data class GeoPoint(val lat: Double, val lon: Double)
data class WeekOpening(val openingHoursMon: OpeningHours?, val openingHoursTue: OpeningHours?, val openingHoursWed: OpeningHours?,
                       val openingHoursThu: OpeningHours?, val openingHoursFri: OpeningHours?, val openingHoursSat: OpeningHours?)