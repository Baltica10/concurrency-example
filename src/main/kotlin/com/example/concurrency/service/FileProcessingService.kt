package com.example.concurrency.service

import org.springframework.web.multipart.MultipartFile

interface FileProcessingService {

    fun processFiles(files: List<MultipartFile>): List<Map<String, String>>
}