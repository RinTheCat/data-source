package com.exercise.datasource.domain.cats;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatsRepository extends JpaRepository<Cat, Long> {

    List<Cat> findCatsByFullName(String fullName);

}
