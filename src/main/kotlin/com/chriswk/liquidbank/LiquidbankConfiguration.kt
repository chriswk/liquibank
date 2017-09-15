package com.chriswk.liquidbank

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.kafka.annotation.EnableKafka
import java.util.concurrent.TimeUnit

@Configuration
@EnableKafka
@EnableElasticsearchRepositories
class LiquidbankConfiguration {

    @Bean
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build()



}