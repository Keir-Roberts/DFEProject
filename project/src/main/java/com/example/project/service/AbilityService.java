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
	
	public AbilityService(abilityRepo abRepo) {
		this.abRepo = abRepo;
	}
	
	public Ability findByInnate(String type) throws NoTypeException {
		Type t = Type.strType(type);
		System.out.println(t.getInnate());
		Ability out = abRepo.findById(t.getInnate()).get();
		return out;
	}
	
	public Ability findName(String name) throws NoAbilityException {
		return abRepo.findByName(name).get();
	}
	
	public Ability findById(Long id) {
		return abRepo.findById(id).get();
	}
}
