package com.exercise.datasource.controller;

import com.exercise.datasource.domain.Cat;
import com.exercise.datasource.repository.MainRepository;
import com.exercise.datasource.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService service;

    @GetMapping("/api/cat")
    public List<Cat> getCats() {
        return service.getAllCats();
    }

    @GetMapping("/api/cat/{name}")
    public List<Cat> getByName(@PathVariable String name) {
        return service.getCatsByName(name);
    }
}
