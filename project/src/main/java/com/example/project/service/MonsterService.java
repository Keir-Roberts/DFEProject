package com.example.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.classes.Ability;
import com.example.project.enums.SkillPoints;
import com.example.project.enums.Type;
import com.example.project.exceptions.BuildPointException;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.existingAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.example.project.repo.monsterRepo;
import com.example.project.classes.Monster;

@Service
public class MonsterService {


	private monsterRepo repo;

	private AbilityService abService;

	public MonsterService(monsterRepo repo, AbilityService abService) {
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
			convertEnumToStr(monster);
			return monster;
		} else
			return monster;
	}

	private Monster addMonAbility(Monster mon, Ability ability) throws Exception {
		if (bpLeft(mon) < SkillPoints.ABILITYCOST.getPoints()) {
			throw new BuildPointException("This monster has " + bpLeft(mon) + "'build points remaining, and "
					+ SkillPoints.ABILITYCOST.getPoints() + " build points are required to add a new Ability.");
		}
		if (mon.getAbilities().contains(ability)) {
			throw new existingAbilityException(mon.getName() + " has already got ability " + ability.getName());
		}
		List<Ability> abilities = mon.getAbilities();
		abilities.add(ability);
		mon.setAbilities(abilities);
		return repo.save(mon);
		
	}

	public Monster unbuild(Monster monster) throws NoTypeException, NoAbilityException {
		if (monster.isBuilt()) {
			monster.setAttack(monster.getAttack() - monster.getType().getBaseATK());
			monster.setHealth(monster.getHealth() - monster.getType().getBaseDEF());
			convertEnumToStr(monster);
			removeMonAbility(monster, abService.findByInnate(monster.getTypeStr()));
			monster.setBuilt(false);
			return monster;
		} else
			return monster;
	}

	public Monster AddMonster(Monster monster) throws Exception {
		this.repo.save(monster);
		convertEnumToStr(monster);
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
			convertStrToEnum(monster);
		} catch (NoTypeException t) {
			throw t;
		}
		return monster;
	}

	public Monster updateMonAttack(Long id, int change) throws Exception {
		Monster mon = readID(id);
		int base = mon.getAttack() - mon.getType().getBaseATK();
		if (bpLeft(mon) < change) {
			throw new BuildPointException("This monster has " + bpLeft(mon)
					+ " build points remaining. The proposed action costs " + change + "build points");
		} else if (base + change <= 0) {
			throw new IndexOutOfBoundsException("This monster has a base attack of: " + base
					+ " and you cannot have an attack less than or equal to 0");
		}
		int atk = mon.getAttack();
		mon.setAttack(atk + change);
		convertStrToEnum(mon);
		return repo.save(mon);
	}

	public Monster updateMonHealth(Long id, int change)
			throws BuildPointException, NoTypeException, NoAbilityException {
		Monster mon = readID(id);
		int base = mon.getHealth() - mon.getType().getBaseDEF();
		if (bpLeft(mon) < change) {
			throw new BuildPointException("This monster has " + bpLeft(mon)
					+ " build points remaining. The proposed action costs " + change + "build points");
		} else if (base + change <= 0) {
			throw new IndexOutOfBoundsException("This monster has a base health of: " + base
					+ " and you cannot have an attack less than or equal to 0");
		}
		int def = mon.getHealth();
		mon.setHealth(def + change);
		convertStrToEnum(mon);
		return repo.save(mon);
	}

	public Monster addMonAbility(Long id, String name)
			throws Exception {
		Monster mon = readID(id);
		if (bpLeft(mon) < SkillPoints.ABILITYCOST.getPoints()) {
			throw new BuildPointException("This monster has " + bpLeft(mon) + "'build points remaining, and "
					+ SkillPoints.ABILITYCOST.getPoints() + " build points are required to add a new Ability.");
		}
		Ability ability = abService.findName(name);
		if (mon.getAbilities().contains(ability)) {
			throw new existingAbilityException(mon.getName() + " has already got ability " + name);
		}
		List<Ability> abilities = mon.getAbilities();
		abilities.add(ability);
		mon.setAbilities(abilities);
		return repo.save(mon);
	}

	public Monster removeMonAbility(Long id, String name) throws NoAbilityException, NoTypeException {
		Monster mon = readID(id);
		Ability ability = abService.findName(name);
		List<Ability> abilities = mon.getAbilities();
		abilities.remove(ability);
		mon.setAbilities(abilities);
		return repo.save(mon);
	}

	public Monster removeMonAbility(Monster mon, Ability ability) throws NoAbilityException, NoTypeException {
		List<Ability> abilities = mon.getAbilities();
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
		convertStrToEnum(monster);
		if (BPCheck(monster))
			;
		Monster original = readID(id);
		unbuild(original);
		original.setName(monster.getName());
		original.setTypeStr(monster.getTypeStr());
		original.setAttack(monster.getAttack());
		original.setHealth(monster.getHealth());
		original.setDescription(monster.getDescription());
		original.setTypeStr(monster.getTypeStr());
		convertStrToEnum(original);
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

	public Monster convertStrToEnum(Monster monster) throws NoTypeException {
		try {
			monster.setType(Type.strType(monster.getTypeStr()));
		} catch (NoTypeException t) {
			throw new NoTypeException("Cannot find a type called " + monster.getTypeStr());
		}
		return monster;
	}

	public Monster convertEnumToStr(Monster monster) {
		monster.setTypeStr(monster.getType().getType());
		return monster;
	}

	public Boolean BPCheck(Monster monster) throws BuildPointException {
		int bp = bpUsed(monster);
		if (bp > SkillPoints.POINTSMAX.getPoints())
			throw new BuildPointException("This monster has used a total of " + bp
					+ " 'build points' which exceeds the maximum of: " + SkillPoints.POINTSMAX.getPoints());
		else
			return true;
	}

	public int bpUsed(Monster monster) {
		if (monster.isBuilt()) {
			return (((monster.getAbilities().size() * SkillPoints.ABILITYCOST.getPoints()) + monster.getAttack() + monster.getHealth())
					- monster.getType().getCost());
		} else {
			return ((monster.getAbilities().size() * SkillPoints.ABILITYCOST.getPoints()) + monster.getAttack() + monster.getHealth());
		}
	}

	public int bpLeft(Monster monster) {
		return (SkillPoints.POINTSMAX.getPoints() - bpUsed(monster));
	}
}
