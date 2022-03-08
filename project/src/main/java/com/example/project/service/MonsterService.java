package com.example.project.service;

import java.util.ArrayList;
import java.util.List;

import com.example.project.enums.Ability;
import com.example.project.enums.SkillPoints;
import com.example.project.enums.Type;
import com.example.project.exceptions.BuildPointException;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.examle.project.repo.monsterRepo;
import com.example.project.classes.Monster;

public class MonsterService {

	private monsterRepo repo;

	public MonsterService(monsterRepo repo) {
		this.repo = repo;
	}

	public Monster build(Monster monster) {
		if (!monster.isBuilt()) {
			int attack = monster.getAttack();
			int health = monster.getHealth();
			List<Ability> abilities = monster.getAbilities();
			Type type = monster.getType();
			if (!monster.isBuilt()) {
				monster.setAttack(type.getAttack(attack));
				monster.setHealth(type.getDef(health));
				abilities.add(type.getInnate());
				monster.setBuilt(true);
			}
			convertEnumToStr(monster);
			return monster;
		} else
			return monster;
	}

	public Monster unbuild(Monster monster) {
		if (monster.isBuilt()) {
			monster.setAttack(monster.getAttack() - monster.getType().getBaseATK());
			monster.setHealth(monster.getHealth() - monster.getType().getBaseDEF());
			List<Ability> abilities = monster.getAbilities();
			abilities.remove(monster.getType().getInnate());
			monster.setAbilities(abilities);
			convertEnumToStr(monster);
			return monster;
		} else
			return monster;
	}

	public Monster AddMonster(Monster monster) {
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
			if (monster.getTypeStr().equals(name))
				output.add(monster);
		}
		if (output.size() == 0) {
			throw new NoTypeException("No monsters found that match the input: '" + name + "'.");
		}
		return output;
	}

	public Monster readID(Long id) {
		return repo.getById(id);
	}

	public Monster getID(Long id) throws NoTypeException, NoAbilityException {
		Monster monster = repo.getById(id);
		try {
			convertStrToEnum(monster);
		} catch (NoTypeException t) {
			throw t;
		} catch (NoAbilityException a) {
			throw a;
		}
		return monster;
	}

	public Monster updateMonAttack(Long id, int change) throws BuildPointException, NoTypeException, NoAbilityException {
		Monster mon = repo.getById(id);
		if (mon.getBPLeft() < change) {
			throw new BuildPointException("This monster has " + mon.getBPLeft() + " build points remaining. The proposed action costs " + change + "build points");
		}
		int atk = mon.getAttack();
		mon.setAttack(atk + change);
		convertStrToEnum(mon);
		return repo.save(mon);
	}
	
	public Monster updateMonHealth(Long id, int change) throws BuildPointException, NoTypeException, NoAbilityException {
		Monster mon = repo.getById(id);
		if (mon.getBPLeft() < change) {
			throw new BuildPointException("This monster has " + mon.getBPLeft() + " build points remaining. The proposed action costs " + change + "build points");
		}
		int def = mon.getHealth();
		mon.setHealth(def + change);
		convertStrToEnum(mon);
		return repo.save(mon);
	}
	
	public Monster addMonAbility(Long id, String name) throws NoAbilityException, NoTypeException, BuildPointException {
		Monster mon = repo.getById(id);
		if (mon.getBPLeft() < SkillPoints.ABILITYCOST.getPoints()) {
			throw new BuildPointException("This monster has " + mon.getBPLeft() + "'build points remaining, and "
					+ SkillPoints.ABILITYCOST.getPoints() + " build points are required to add a new Ability.");
		}
		Ability.abilityStr(name);
		List<String> abilities = mon.getAbilityStr();
		abilities.add(name);
		mon.setAbilityStr(abilities);
		convertStrToEnum(mon);
		return repo.save(mon);
	}

	public Monster removeMonAbility(Long id, String name) throws NoAbilityException, NoTypeException {
		Monster mon = repo.getById(id);
		Ability.abilityStr(name);
		List<String> abilities = mon.getAbilityStr();
		abilities.remove(name);
		mon.setAbilityStr(abilities);
		convertStrToEnum(mon);
		mon.setBuildPoints();
		mon.setBPLeft();
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

	public String compare(Long id1, Long id2) {
		Monster m1 = this.repo.getById(id1);
		if (m1 == null)
			return "Error: Cannot find a monster with ID: " + id1;
		String n1 = m1.getName();
		Monster m2 = this.repo.getById(id2);
		if (m2 == null)
			return "Error: Cannot find a monster with ID: " + id2;
		String n2 = m2.getName();
		int defDif = m1.getHealth() - m2.getHealth();
		String health;
		if (defDif > 0) {
			health = n1 + "has the higher health of " + m1.getHealth() + " which is " + defDif + " points larger than "
					+ n2 + "'s health of " + m2.getHealth() + ".";
		} else if (defDif == 0) {
			health = "They both have the same health score of " + m1.getHealth();
		} else {
			health = n2 + "has the higher health of " + m2.getHealth() + " which is " + ((-1) * (defDif))
					+ " points larger than " + n1 + "'s health of " + m1.getHealth() + ".";
		}
		int attDif = m1.getHealth() - m2.getHealth();
		String attack;
		if (attDif > 0) {
			attack = n1 + "has the higher attack of " + m1.getAttack() + " which is " + attDif + " points larger than "
					+ n2 + "'s attack of " + m2.getAttack() + ".";
		} else if (attDif == 0) {
			attack = "They both have the same attack score of " + m1.getAttack();
		} else {
			attack = n2 + "has the higher attack of " + m2.getAttack() + " which is " + ((-1) * (attDif))
					+ " points larger than " + n1 + "'s attack of " + m1.getAttack() + ".";
		}
		String output = "Comparing " + n1 + " and " + n2 + "." + "\n Type: \n      " + n1 + "'s type is "
				+ m1.getType().getType() + " and " + n2 + "'s type is " + m2.getType().getType() + "."
				+ "\n\n Attack: \n      " + attack + "\n\n Health: \n      " + health + "\n\n Abilities \n      " + n1
				+ "'s abilities are: " + m1.getAbilities() + "\n      " + n2 + "'s abilities are: " + m2.getAbilities();
		return output;
	}

	public Monster Update(Long id, Monster monster) throws BuildPointException, NoTypeException, NoAbilityException {
		convertStrToEnum(monster);
		if (BPCheck(monster));
		Monster original = readID(id);
		unbuild(original);
		original.setName(monster.getName());
		original.setTypeStr(monster.getTypeStr());
		original.setAbilityStr(monster.getAbilityStr());
		original.setAttack(monster.getAttack());
		original.setHealth(monster.getHealth());
		original.setDescription(monster.getDescription());
		original.setTypeStr(monster.getTypeStr());
		convertStrToEnum(original);
		build(original);
		return repo.save(original);
	}

	public Monster deleteMon(long id) {
		Monster out =repo.getById(id);
		repo.delete(repo.getById(id));
		return out;
	}

	public Monster convertStrToEnum(Monster monster) throws NoTypeException, NoAbilityException {
		try {
			monster.setType(Type.strType(monster.getTypeStr()));
		} catch (NoTypeException t) {
			throw new NoTypeException("Cannot find a type called " + monster.getTypeStr());
		}
		String errorOut = null;
		try {
			List<Ability> abilities = new ArrayList<Ability>();
			for (String s : monster.getAbilityStr()) {
				errorOut = s;
				abilities.add(Ability.abilityStr(s));
			}
			monster.setAbilities(abilities);
		} catch (NoAbilityException t) {
			throw new NoAbilityException("Cannot find an ability called '" + errorOut);
		}
		monster.setBuildPoints();
		return monster;
	}

	public Monster convertEnumToStr(Monster monster) {
		List<String> abilityStr = new ArrayList<String>();
		for (Ability a : monster.getAbilities()) {
			abilityStr.add(a.getName());
		}
		monster.setAbilityStr(abilityStr);
		monster.setTypeStr(monster.getType().getType());
		return monster;
	}
	public Boolean BPCheck(Monster monster) throws BuildPointException {
		int bp = monster.getBuildPoints();
		if (bp > SkillPoints.POINTSMAX.getPoints())
			throw new BuildPointException("This monster has used a total of " + bp
					+ " 'build points' which exceeds the maximum of: " + SkillPoints.POINTSMAX.getPoints());
		else
			return true;
	}
}
