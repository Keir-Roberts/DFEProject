package com.example.project.repo;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.classes.Ability;

@Repository
public interface abilityRepo extends JpaRepository<Ability, Long>{

	@Query("SELECT * from ability a WHERE a.name = ?1")
	Optional<Ability> findByName(String name);
}