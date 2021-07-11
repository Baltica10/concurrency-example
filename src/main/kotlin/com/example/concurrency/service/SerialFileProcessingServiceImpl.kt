package com.example.concurrency.service

import io.trbl.blurhash.BlurHash
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import javax.imageio.ImageIO

@Service
class SerialFileProcessingServiceImpl : FileProcessingService {

    companion object {
        val log: Log = LogFactory.getLog(SerialFileProcessingServiceImpl::class.java)
    }

    override fun processFiles(files: List<MultipartFile>): List<Map<String, String>> {
        log.info("Start serial processing")
        val startTime = LocalDateTime.now()

        val result: List<Map<String, String>> = files.stream()
            .map { mapOf<String, String>(it.originalFilename!! to BlurHash.encode(ImageIO.read(it.inputStream))) }
            .collect(Collectors.toList())

        log.info("Finished serial processing in ${ChronoUnit.SECONDS.between(startTime, LocalDateTime.now())} seconds")

        return result
    }

}