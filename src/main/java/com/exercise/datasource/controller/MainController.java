package com.exercise.datasource.controller;

import com.exercise.datasource.domain.Cat;
import com.exercise.datasource.repository.MainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainRepository repository;

    @GetMapping("/api/cat")
    public List<Cat> getCats() {
        return repository.findAll();
    }

    @GetMapping("/api/cat/{name}")
    public List<Cat> getByName(@PathVariable String name) {
        return repository.findCatsByFullName(name);
    }
}
