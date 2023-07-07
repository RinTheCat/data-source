package com.exercise.datasource.service;

import com.exercise.datasource.domain.cats.Cat;
import com.exercise.datasource.domain.cats.CatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    private final CatsRepository repository;

    @Async
    public CompletableFuture<List<Cat>> getAllCats() throws InterruptedException {
        log.info("All cats");
        Thread.sleep(10000);
        final List<Cat> result = repository.findAll();
        log.info("All cats, completed");
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<List<Cat>> getCatsByName(String name) throws InterruptedException {
        log.info("Cats by name: {}", name);
        Thread.sleep(10000);
        final List<Cat> result = repository.findCatsByFullName(name);
        log.info("Cats by name: {}, completed", name);
        return CompletableFuture.completedFuture(result);
    }
}
