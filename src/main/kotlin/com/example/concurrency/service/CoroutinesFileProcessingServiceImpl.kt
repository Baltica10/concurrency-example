package com.example.concurrency.service

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class CoroutinesFileProcessingServiceImpl : FileProcessingService {

    companion object {
        val log: Log = LogFactory.getLog(CoroutinesFileProcessingServiceImpl::class.java)
    }

    override fun processFiles(files: List<MultipartFile>): List<Map<String, String>> {
        TODO("Not yet implemented")
    }

}