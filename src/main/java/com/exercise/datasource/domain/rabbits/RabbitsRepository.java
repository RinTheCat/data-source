package com.exercise.datasource.domain.rabbits;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RabbitsRepository extends JpaRepository<Rabbit, Long> {

    List<Rabbit> findRabbitByFullName(String fullName);

}