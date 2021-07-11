package com.example.concurrency.service;

import io.trbl.blurhash.BlurHash;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ThreadPoolFileProcessingServiceImpl implements FileProcessingService {

  private static final Logger log = LoggerFactory.getLogger(ThreadPoolFileProcessingServiceImpl.class);

  @NotNull
  @Override
  public List<Map<String, String>> processFiles(@NotNull List<? extends MultipartFile> files) {
    log.info("Start thread pool processing");
    var startTime = LocalDateTime.now();

    try {
      ExecutorService pool = Executors.newFixedThreadPool(files.size());
      List<Future<Map<String, String>>> tasks = pool.invokeAll(
          files.stream()
              .map(it -> (Callable<Map<String, String>>) () -> {
                return Map.of(Objects.requireNonNull(it.getOriginalFilename()), BlurHash.encode(ImageIO.read(it.getInputStream())));
              })
              .collect(Collectors.toList())
      );
      pool.shutdown();
      log.info("ExecutorService shutdown is {}", pool.awaitTermination(2, TimeUnit.MINUTES));

      List<Map<String, String>> result = tasks.stream()
          .map(it -> {
            Map<String, String> hash = null;
            try {
              hash = it.get();
            } catch (InterruptedException | ExecutionException ex) {
              log.warn(ex.getMessage());
              ex.printStackTrace();
            }
            return hash;
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

      log.info("Finished thread pool processing in {} seconds", ChronoUnit.SECONDS.between(startTime, LocalDateTime.now()));

      return result;

    } catch (InterruptedException ex) {
      log.warn(ex.getMessage());
      ex.printStackTrace();
    }

    return List.of(new HashMap<>());
  }
}