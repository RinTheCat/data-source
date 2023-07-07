package com.exercise.datasource.controller;

import com.exercise.datasource.domain.Cat;
import com.exercise.datasource.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService service;

    @GetMapping("/api/cat")
    public List<Cat> getCats() throws ExecutionException, InterruptedException {
        return service.getAllCats().get();
    }

    @GetMapping("/api/cat/{name}")
    public List<Cat> getByName(@PathVariable String name) throws ExecutionException, InterruptedException {
        return service.getCatsByName(name).get();
    }

    @GetMapping("/api/concurrent/cat/{name}")
    public Map<String, List<Cat>> getAllAndByName(@PathVariable String name) throws ExecutionException, InterruptedException {
        var result1 = service.getAllCats();
        var result2 = service.getCatsByName(name);
        final Map<String, List<Cat>> result = Map.of(
                "all", result1.get(),
                "byName", result2.get()
        );
        return result;
    }
}
