package com.example.project.service;

import org.springframework.stereotype.Service;

import com.example.project.classes.Ability;
import com.example.project.enums.Type;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.example.project.repo.abilityRepo;

@Service
public class AbilityService {

	abilityRepo abRepo;
	
	AbilityService(abilityRepo abRepo) {
		this.abRepo = abRepo;
	}
	
	public Ability findByInnate(String type) throws NoTypeException {
		Type t = Type.strType(type);
		for(Ability a: abRepo.findAll()) {
			if(t.getInnate().toLowerCase().equals(a)) return a;
		}
		return null;
	}
	
	public Ability findName(String name) throws NoAbilityException {
		for(Ability a: abRepo.findAll()) {
			if(a.getName().toLowerCase().equals(name.toLowerCase())) return a;
		}
		throw new NoAbilityException("No ability found with name: " + name);
	}
}
