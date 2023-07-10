package com.exercise.datasource.repository.cats;

import com.exercise.datasource.domain.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatsRepository extends JpaRepository<Cat, Long> {

    List<Cat> findCatsByFullName(String fullName);

}
