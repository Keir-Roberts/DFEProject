package com.example.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.classes.Ability;
import com.example.project.enums.Type;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.example.project.repo.monsterRepo;
import com.example.project.classes.Monster;

@Service
public class MonsterService {


	private monsterRepo repo;
	
	private ValidateService valid;

	private AbilityService abService;

	public MonsterService(monsterRepo repo, AbilityService abService, ValidateService valid) {
		this.valid = valid;
		this.repo = repo;
		this.abService = abService;
	}

	public Monster build(Monster monster) throws Exception {
		if (!monster.isBuilt()) {
			int attack = monster.getAttack();
			int health = monster.getHealth();
			String typeStr = monster.getTypeStr();
			Type type = Type.strType(typeStr);
			if (!monster.isBuilt()) {
				monster.setAttack(type.getAttack(attack));
				monster.setHealth(type.getDef(health));
				monster.setBuilt(true);
				addMonAbility(monster, abService.findByInnate(monster.getTypeStr()));
			}
			valid.convertEnumToStr(monster);
			return monster;
		} else
			return monster;
	}

	private Monster addMonAbility(Monster mon, Ability ability) throws Exception {
		valid.validAbilityAdd(mon, ability);
		List<Ability> abilities = mon.getAbilities();
		abilities.add(ability);
		mon.setAbilities(abilities);
		return repo.save(mon);
		
	}

	public Monster unbuild(Monster monster) throws Exception {
		if (monster.isBuilt()) {
			monster.setAttack(monster.getAttack() - monster.getType().getBaseATK());
			monster.setHealth(monster.getHealth() - monster.getType().getBaseDEF());
			valid.convertEnumToStr(monster);
			removeMonAbility(monster, abService.findByInnate(monster.getTypeStr()));
			monster.setBuilt(false);
			return monster;
		} else
			return monster;
	}

	public Monster AddMonster(Monster monster) throws Exception {
		this.repo.save(monster);
		valid.convertEnumToStr(monster);
		if (monster.isBuilt()) {
			return this.repo.save(monster);
		} else {
			return this.repo.save(build(monster));
		}
	}

	public List<Monster> getAllMon() {
		return this.repo.findAll();
	}

	public List<Monster> getType(String name) throws NoTypeException {
		List<Monster> output = new ArrayList<Monster>();
		List<Monster> all = this.repo.findAll();
		for (Monster monster : all) {
			if (monster.getTypeStr().toLowerCase().equals(name.toLowerCase()))
				output.add(monster);
		}
		if (output.size() == 0) {
			throw new NoTypeException("No monsters found that match the input: '" + name + "'.");
		}
		return output;
	}

	public Monster readID(Long id) throws NoTypeException {
		Monster mon = repo.findById(id).get();
		mon.setType(Type.strType(mon.getTypeStr()));
		return mon;
	}

	public Monster getID(Long id) throws NoTypeException, NoAbilityException {
		Monster monster = readID(id);
		try {
			valid.convertStrToEnum(monster);
		} catch (NoTypeException t) {
			throw t;
		}
		return monster;
	}

	public Monster updateMonAttack(Long id, int change) throws Exception {
		Monster mon = readID(id);
		valid.valStatChange("attack", mon, change);
		int atk = mon.getAttack();
		mon.setAttack(atk + change);
		valid.convertStrToEnum(mon);
		return repo.save(mon);
	}

	public Monster updateMonHealth(Long id, int change)
			throws Exception {
		Monster mon = readID(id);
		valid.convertStrToEnum(mon);
		valid.valStatChange("health", mon, change);
		int def = mon.getHealth();
		mon.setHealth(def + change);
		return repo.save(mon);
	}

	public Monster addMonAbility(Long id, String name)
			throws Exception {
		Monster mon = readID(id);
		Ability ability = abService.findName(name);
		valid.validAbilityRemove(mon, ability);
		List<Ability> abilities = mon.getAbilities();
		abilities.add(ability);
		mon.setAbilities(abilities);
		return repo.save(mon);
	}

	public Monster removeMonAbility(Long id, String name) throws Exception {
		Monster mon = readID(id);
		Ability ability = abService.findName(name);
		List<Ability> abilities = mon.getAbilities();
		if (mon.getType().getInnate() == ability.getId()) {
			throw new Exception("Cannot remove innate abilities");
		}
		abilities.remove(ability);
		mon.setAbilities(abilities);
		return repo.save(mon);
	}

	public Monster removeMonAbility(Monster mon, Ability ability) throws Exception {
		List<Ability> abilities = mon.getAbilities();
		if (mon.getType().getInnate() == ability.getId()) {
			throw new Exception("Cannot remove innate abilities");
		}
		abilities.remove(ability);
		mon.setAbilities(abilities);
		return repo.save(mon);
	}
	public List<Monster> getName(String name) {
		List<Monster> output = new ArrayList<Monster>();
		List<Monster> all = this.repo.findAll();
		for (Monster monster : all) {
			if (monster.getType().getType().contains(name))
				output.add(monster);
		}
		return output;
	}

	public String compare(Long id1, Long id2) throws NoTypeException {
		Monster m1 = this.readID(id1);
		if (m1 == null)
			return "Error: Cannot find a monster with ID: " + id1;
		String n1 = m1.getName();
		Monster m2 = this.readID(id2);
		if (m2 == null)
			return "Error: Cannot find a monster with ID: " + id2;
		String n2 = m2.getName();
		int defDif = m1.getHealth() - m2.getHealth();
		String health;
		if (defDif > 0) {
			health = n1 + " has the higher health of " + m1.getHealth() + " which is " + defDif + " points larger than "
					+ n2 + "'s health of " + m2.getHealth() + ".";
		} else if (defDif == 0) {
			health = "They both have the same health score of " + m1.getHealth();
		} else {
			health = n2 + " has the higher health of " + m2.getHealth() + " which is " + ((-1) * (defDif))
					+ " points larger than " + n1 + "'s health of " + m1.getHealth() + ".";
		}
		int attDif = m1.getHealth() - m2.getHealth();
		String attack;
		if (attDif > 0) {
			attack = n1 + " has the higher attack of " + m1.getAttack() + " which is " + attDif + " points larger than "
					+ n2 + "'s attack of " + m2.getAttack() + ".";
		} else if (attDif == 0) {
			attack = "They both have the same attack score of " + m1.getAttack();
		} else {
			attack = n2 + " has the higher attack of " + m2.getAttack() + " which is " + ((-1) * (attDif))
					+ " points larger than " + n1 + "'s attack of " + m1.getAttack() + ".";
		}
		String output = "Comparing " + n1 + " and " + n2 + "." + "\n Type: \n      " + n1 + "'s type is "
				+ m1.getType().getType() + " and " + n2 + "'s type is " + m2.getType().getType() + "."
				+ "\n\n Attack: \n      " + attack + "\n\n Health: \n      " + health + "\n\n Abilities \n      " + n1
				+ "'s abilities are: " + m1.getAbilities() + "\n      " + n2 + "'s abilities are: "
				+ m2.getAbilities();
		return output;
	}

	public Monster Update(Long id, Monster monster) throws Exception {
		valid.convertStrToEnum(monster);
		if (valid.BPCheck(monster))
			;
		Monster original = readID(id);
		unbuild(original);
		original.setName(monster.getName());
		original.setTypeStr(monster.getTypeStr());
		original.setAttack(monster.getAttack());
		original.setHealth(monster.getHealth());
		original.setDescription(monster.getDescription());
		original.setTypeStr(monster.getTypeStr());
		valid.convertStrToEnum(original);
		original.setBuilt(monster.isBuilt());
		build(original);
		return repo.save(original);
	}

	public String deleteMon(long id) throws NoTypeException {
		Monster out = readID(id);
		String name = out.getName();
		repo.delete(readID(id));
		return name + " has been deleted";
	}

}
