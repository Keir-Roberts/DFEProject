package com.example.project.service;

import java.util.List;

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
		Ability out = abRepo.findById(t.getInnate()).get();
		return out;
	}
	
	public Ability findName(String name) throws NoAbilityException {
		return abRepo.findByName(name).get();
	}
	
	public Ability findById(Long id) {
		return abRepo.findById(id).get();
	}
	
	public List<Ability> getAllAbiliity() {
		return abRepo.findAll();
	}
	
	public Ability addAbility(Ability abi) {
		return abRepo.save(abi);
	}
	
	public Ability Update(Long id, Ability ability) throws Exception {
		Ability original = abRepo.findById(id).get();
		original.setName(ability.getName());
		original.setDescription(ability.getDescription());
		return abRepo.save(original);
	}
	
	public String DeleteAbility(Long id) throws Exception{
	Ability out = abRepo.findById(id).get();
	String name = out.getName();
	abRepo.delete(out);
	return name + " has been deleted";
	}
}
