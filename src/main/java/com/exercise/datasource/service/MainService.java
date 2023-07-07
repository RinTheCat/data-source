package com.exercise.datasource.service;

import com.exercise.datasource.domain.Cat;
import com.exercise.datasource.repository.MainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final MainRepository repository;

    public List<Cat> getAllCats() {
        log.info("Запрос по поиску всех котов");
        final List<Cat> result = repository.findAll();
        log.info("Завершение запроса по поиску всех котов");
        return result;
    }

    public List<Cat> getCatsByName(String name) {
        log.info("Запрос по поиску всех котов с именем {}", name);
        final List<Cat> result = repository.findCatsByFullName(name);
        log.info("Запрос по поиску всех котов с именем {} завершен", name);
        return result;
    }
}
