package com.example.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.classes.Ability;
import com.example.project.classes.MonAbility;
import com.example.project.classes.Monster;
import com.example.project.repo.monAbilityRepo;

@Service
public class MonAbilityService {

	monAbilityRepo monRepo;

	MonAbilityService(monAbilityRepo monRepo) {
		this.monRepo = monRepo;
	}

	public List<MonAbility> getAll() {
		return monRepo.findAll();
	}

	public MonAbility getID(Long id) {
		return monRepo.getById(id);
	}

	public MonAbility getFromMonAbi(Monster monster, Ability ability) {
		for (MonAbility m : monRepo.findAll()) {
			if ((m.getMonster().getId() == monster.getId()) && (m.getAbility().getName().equals(ability.getName()))) return m;
		}
		return null;
	}
	public List<Ability> getAbilityFromMon(Monster monster) {
		List<Ability> out = new ArrayList<Ability>();
		for (MonAbility m : monRepo.findAll()) {
			if (m.getMonster().getId()== monster.getId()) {
				out.add(m.getAbility());
			}

		}
		return out;
	}
	public List<Ability> getAbilityFromMonID(Long id) {
		List<Ability> out = new ArrayList<Ability>();
		for (MonAbility m : monRepo.findAll()) {
			if (m.getMonster().getId() == id) {
				out.add(m.getAbility());
			}

		}
		return out;
	}

public List<Monster> getMonFromAbility(String name) {
	List<Monster> out = new ArrayList<Monster>();
	for (MonAbility m : monRepo.findAll()) {
		if (m.getAbility().getName().equals(name)) {
			out.add(m.getMonster());
		}

	}
	return out;
}

	public MonAbility addMonAbility(Monster monster, Ability ability) {
		MonAbility out = new MonAbility(monster, ability);
		return monRepo.save(out);
	}

public MonAbility deleteMonAbility(Long id) {
	MonAbility out = monRepo.getById(id);
	monRepo.deleteById(id);
	return out;
}

public List<MonAbility> deleteFromMon(Monster monster) {
	List<MonAbility> chop = new ArrayList<MonAbility>();
	for(MonAbility m : monRepo.findAll()) {
		if (m.getMonster().equals(monster)) chop.add(m);
		monRepo.delete(m);
	}
	return chop;
}

public List<MonAbility> deleteFromAbility(Ability ability) {
	List<MonAbility> chop = new ArrayList<MonAbility>();
	for(MonAbility m : monRepo.findAll()) {
		if(m.getAbility().equals(ability)) chop.add(m);
		monRepo.delete(m);
	}
	return chop;
}

public MonAbility updateMonAbility(Long id, Monster m, Ability a) {
	MonAbility out = new MonAbility(id, m, a);
	return monRepo.save(out);
}

public int abilityNum(Long id) {
	return getAbilityFromMonID(id).size();
}
}

