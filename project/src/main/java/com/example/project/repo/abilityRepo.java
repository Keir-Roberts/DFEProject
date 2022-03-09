package com.example.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.classes.Ability;

public interface abilityRepo extends JpaRepository<Ability, Long>{

}
