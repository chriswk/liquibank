package com.chriswk.liquidbank.stock

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Paths
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Component
class StockDownloader(@Autowired val okHttpClient: OkHttpClient, @Value("\${wine.stock.url}") val wineStockUrl: String, @Value("\${wine.stock.path}") val storagePath: String) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)


    fun refreshCsv() : Boolean {
        val cacheTime = LocalDate.now().minusWeeks(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        logger.info("Checking ${storagePath}. Downloading from ${wineStockUrl}")

        val curFile = Paths.get(storagePath).toFile()
        if (curFile != null && (!curFile.exists() || curFile.lastModified() < cacheTime)) {
            curFile.parentFile.mkdirs()
            val url = HttpUrl.parse(wineStockUrl)
            val request = Request.Builder().url(url).get().build()
            logger.info("Downloading {}", request)
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful()) {
                writeResponseToCsv(response.body(), curFile)
                logger.info("Done downloading")
                return true
            } else {
                logger.error("Failed to download due to " + response.code())
            }
        } else {
            logger.info("File already existed. Was last changed ${Instant.ofEpochMilli(curFile.lastModified())}.")
        }
        return false
    }

    private fun writeResponseToCsv(body: ResponseBody?, curFile: File) {
        if(body != null) {
            logger.info("${body.contentType()}")
            val latin1Data = body.source().readString(Charset.forName("ISO-8859-1"))
            curFile.writeText(latin1Data)
        }
    }


}