package com.example.concurrency.controller

import com.example.concurrency.service.FileProcessingService
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class InputFileController(
    @Qualifier("serialFileProcessingServiceImpl")
    private val serialFileProcessingService: FileProcessingService,

    @Qualifier("threadPoolFileProcessingServiceImpl")
    private val threadPoolFileProcessingService: FileProcessingService
) {

    @PostMapping(path = ["/serial"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun serialProcessFiles(
        @ApiParam(
            value = "files",
            required = true
        ) @RequestPart("files") files: List<MultipartFile>
    ): ResponseEntity<List<Map<String, String>>> {
        return ResponseEntity.ok(serialFileProcessingService.processFiles(files))
    }

    @PostMapping(path = ["/thread-pool"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun threadPoolProcessFiles(
        @ApiParam(
            value = "files",
            required = true
        ) @RequestPart("files") files: List<MultipartFile>
    ): ResponseEntity<List<Map<String, String>>> {
        return ResponseEntity.ok(threadPoolFileProcessingService.processFiles(files))
    }

}