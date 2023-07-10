package com.exercise.datasource.repository.rabbits;

import com.exercise.datasource.domain.Rabbit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RabbitsRepository extends JpaRepository<Rabbit, Long> {

    List<Rabbit> findRabbitByFullName(String fullName);

}