package com.examle.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.classes.Monster;

public interface monsterRepo extends JpaRepository<Monster, Long> {

}
