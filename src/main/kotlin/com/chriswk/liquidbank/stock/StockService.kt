package com.chriswk.liquidbank.stock

import org.elasticsearch.action.search.SearchType
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.Terms
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux

@Service
class StockService(val elasticSearchTemplate: ElasticsearchTemplate) {
    data class Producer(val name: String, val productCount: Long)
    fun producerAggregate(): Flux<Producer> {
        val builder : NativeSearchQueryBuilder = NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSearchType(SearchType.DEFAULT)
                .withIndices("stock").withTypes("stockitem")
                .addAggregation(AggregationBuilders.terms("producers").field("producer").size(100))
        val aggregations = elasticSearchTemplate.query(builder.build(), { it.aggregations })
        val producers = aggregations.asMap().get("producers") as Terms
        return producers.buckets.toFlux().map {
            Producer(it.keyAsString, it.docCount)
        }
    }

}