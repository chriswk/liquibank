package com.chriswk.liquidbank.stock

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.CompletionField
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.math.BigDecimal
import java.time.ZonedDateTime

@Document(indexName = "stock")
data class StockItem(val timestamp: ZonedDateTime, @Id val productId: Long? = null,
                     @CompletionField val productName: String? = null,
                     @Field(type = FieldType.keyword) val productType: String? = null,
                     val volume: String?, val price: BigDecimal?, val pricePerLiter: BigDecimal? = null,
                     val selection: String? = null, val shopCategory: String? = null, val body: Int? = null,
                     val freshness: Int? = null, val tannins: Int? = null, val bitterness: Int? = null,
                     val sweetness: Int? = null, val colour: String? = null, val nose: String? = null,
                     val flavour: String? = null,
                     val matches: List<String> = emptyList(), val country: String? = null, val district: String? = null,
                     val subDistrict: String? = null, val vintage: String? = null, val mainIngredient: String? = null,
                     val method: String? = null, val abv: String? = null, val sugarPerLiter: String? = null,
                     val acidPerLiter: String? = null, val storageHint: String? = null, @Field(type = FieldType.keyword) val producer: String? = null,
                     val engros: String? = null, val distributor: String? = null, val packaging: String? = null,
                     val cork: String? = null, val referenceUrl: String? = null)