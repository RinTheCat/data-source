package com.exercise.datasource.service;

import com.exercise.datasource.router.ClientDatabase;
import com.exercise.datasource.router.ClientDatabaseContextHolder;
import com.exercise.datasource.domain.Cat;
import com.exercise.datasource.repository.MainRepository;
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

    private final MainRepository repository;

    @Async
    public CompletableFuture<List<Cat>> getAllCats() throws InterruptedException {
        log.info("All cats");
//        Thread.sleep(100);
        final List<Cat> result = repository.findAll();
        log.info("All cats, completed");
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<List<Cat>> getAllCats(ClientDatabase dataBase) throws InterruptedException {
        ClientDatabaseContextHolder.set(dataBase);
        log.info("All cats with specific db {}", dataBase);
//        Thread.sleep(100);
        final List<Cat> result = repository.findAll();
        log.info("All cats with specific db, completed");
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<List<Cat>> getCatsByName(String name) throws InterruptedException {
        log.info("Cats by name: {}", name);
//        Thread.sleep(100);
        final List<Cat> result = repository.findCatsByFullName(name);
        log.info("Cats by name: {}, completed", name);
        return CompletableFuture.completedFuture(result);
    }
}
