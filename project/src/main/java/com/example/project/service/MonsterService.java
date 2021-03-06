package com.example.project.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.project.classes.Ability;
import com.example.project.enums.Type;
import com.example.project.exceptions.NoTypeException;
import com.example.project.repo.abilityRepo;
import com.example.project.repo.monsterRepo;
import com.example.project.classes.Monster;
import com.example.project.dto.MonsterDTO;

@Service
public class MonsterService {

	private monsterRepo repo;

	private ValidateService valid;
	
	private abilityRepo abRepo;

	private ModelMapper mapper;
	
	public MonsterService(monsterRepo repo, abilityRepo abRepo, ValidateService valid, ModelMapper mapper) {
		this.valid = valid;
		this.repo = repo;
		this.abRepo = abRepo;
		this.mapper = mapper;
	}

	public MonsterDTO mapToDTO(Monster mon) {
        return this.mapper.map(mon, MonsterDTO.class);
    }
	
	public List<MonsterDTO> mapListDTO(List<Monster> list) {
		List<MonsterDTO> out = new ArrayList<MonsterDTO>();
		for(Monster m : list) {
			out.add(mapToDTO(m));
	}
	return out;	
	}
	public Monster build(Monster monster) throws Exception {
		if (!monster.isBuilt()) {
			int attack = monster.getAttack();
			int health = monster.getHealth();
			String type = monster.getType();
			Type typeEnum = Type.strType(type);
			monster.setTypeEnum(typeEnum);
			monster.setAttack(typeEnum.getAttack(attack));
			monster.setHealth(typeEnum.getDef(health));
			monster.setBuilt(true);
			addMonAbility(monster, abRepo.findById(typeEnum.getInnate()).get());
			return monster;
		} else
			return monster;
	}

	public Monster addMonAbility(Monster mon, Ability ability) throws Exception {
		valid.validAbilityAdd(mon, ability);
		List<Ability> abilities = mon.trueGetAbilities();
		abilities.add(ability);
		mon.setAbilities(abilities);
		return repo.save(mon);

	}

	public Monster unbuild(Monster monster) throws Exception {
		if (monster.isBuilt()) {
			String type = monster.getType();
			monster.setAttack(monster.getAttack() - monster.getTypeEnum().getBaseATK());
			monster.setHealth(monster.getHealth() - monster.getTypeEnum().getBaseDEF());
			monster.setTypeEnum(Type.NULL);
			removeMonAbility(monster, abRepo.findById(Type.strType(type).getInnate()).get());
			monster.setTypeEnum(Type.strType(type));
			monster.setBuilt(false);
			return monster;
		} else
			return monster;
	}

	public Monster AddMonster(Monster monster) throws Exception {
		this.repo.save(monster);
		monster.setType(monster.getTypeEnum().getType());
		if (monster.isBuilt()) {
			monster.setTypeEnum(Type.strType(monster.getType()));
			return this.repo.save(monster);
		} else {
			monster = build(monster);
			return this.repo.save(monster);
		}
	}

	public List<Monster> getAllMon() {
		return this.repo.findAll();
	}

	public List<Monster> getType(String name) throws NoTypeException {
		List<Monster> output = new ArrayList<Monster>();
		List<Monster> all = this.repo.findAll();
		for (Monster monster : all) {
			if (monster.getType().toLowerCase().equals(name.toLowerCase()))
				output.add(monster);
		}
		if (output.size() == 0) {
			throw new NoTypeException("No monsters found that match the input: '" + name + "'.");
		}
		return output;
	}

	public Monster readID(Long id) throws Exception {
		Monster mon = repo.findById(id).get();
		return mon;
	}

	public Monster getID(Long id) throws Exception {
		Monster monster = readID(id);
		try {
			monster.setTypeEnum(Type.strType(monster.getType()));
		} catch (NoTypeException t) {
			throw t;
		}
		return monster;
	}

	public Monster updateMonAttack(Long id, int change) throws Exception {
		Monster mon = getID(id);
		valid.valStatChange("attack", mon, change);
		int atk = mon.getAttack();
		mon.setAttack(atk + change);
		mon.setTypeEnum(Type.strType(mon.getType()));
		return repo.save(mon);
	}

	public Monster updateMonHealth(Long id, int change) throws Exception {
		Monster mon = getID(id);
		valid.valStatChange("health", mon, change);
		int def = mon.getHealth();
		mon.setHealth(def + change);
		mon.setTypeEnum(Type.strType(mon.getType()));
		return repo.save(mon);
	}

	public Monster addMonAbility(Long id, String name) throws Exception {
		Monster mon = getID(id);
		Ability ability = abRepo.findByName(name).get();
		valid.validAbilityAdd(mon, ability);
		List<Ability> abilities = mon.trueGetAbilities();
		abilities.add(ability);
		mon.setAbilities(abilities);
		return repo.save(mon);
	}

	public Monster removeMonAbility(Long id, String name) throws Exception {
		Monster mon = getID(id);
		Ability ability = abRepo.findByName(name).get();
		valid.validAbilityRemove(mon, ability);
		List<Ability> abilities = mon.trueGetAbilities();
		abilities.remove(ability);
		mon.setAbilities(abilities);
		return repo.save(mon);
	}

	public Monster removeMonAbility(Monster mon, Ability ability) throws Exception {
		List<Ability> abilities = mon.trueGetAbilities();
		if (mon.getTypeEnum().getInnate() == ability.getId()) {
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
			if (monster.getName().contains(name))
				output.add(monster);
		}
		return output;
	}

	public String compare(Long id1, Long id2) throws Exception {
		Monster m1 = this.getID(id1);
		String n1 = m1.getName();
		Monster m2 = this.getID(id2);
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
		int attDif = m1.getAttack() - m2.getAttack();
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
				+ m1.getTypeEnum().getType() + " and " + n2 + "'s type is " + m2.getTypeEnum().getType() + "."
				+ "\n\n Attack: \n      " + attack + "\n\n Health: \n      " + health + "\n\n Abilities \n      " + n1
				+ "'s abilities are: " + m1.getAbilities() + "\n      " + n2 + "'s abilities are: " + m2.getAbilities();
		return output;
	}

	public Monster Update(Long id, Monster monster) throws Exception {
		monster.setTypeEnum(Type.strType(monster.getType()));
		if (valid.BPCheck(monster));
		Monster original = getID(id);
		unbuild(original);
		original.setName(monster.getName());
		original.setType(monster.getType());
		original.setAttack(monster.getAttack());
		original.setHealth(monster.getHealth());
		original.setDescription(monster.getDescription());
		original.setType(monster.getType());
		original.setTypeEnum(Type.strType(original.getType()));
		original.setBuilt(monster.isBuilt());
		build(original);
		return repo.save(original);
	}

	public String deleteMon(long id) throws Exception {
		Monster out = readID(id);
		String name = out.getName();
		List<Ability> abi = new ArrayList<Ability>();
		out.setAbilities(abi);
		repo.delete(readID(id));
		return name + " has been deleted";
	}

}
