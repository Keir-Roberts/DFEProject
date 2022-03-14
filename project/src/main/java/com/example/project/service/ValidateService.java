package com.example.project.service;

import org.springframework.stereotype.Service;

import com.example.project.classes.Ability;
import com.example.project.classes.Monster;
import com.example.project.enums.SkillPoints;
import com.example.project.enums.Type;
import com.example.project.exceptions.BuildPointException;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.example.project.exceptions.existingAbilityException;

@Service
public class ValidateService {
	public Monster convertStrToEnum(Monster monster) throws NoTypeException {
		Type type = null;
		try {
			type = Type.strType(monster.getType());
		} catch (NoTypeException t) {
			throw new NoTypeException("Cannot find a type called " + monster.getType());
		}
		monster.setTypeEnum(type);
		return monster;
	}

	public Monster convertEnumToStr(Monster monster) {
		monster.setType(monster.getTypeEnum().getType());
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
			return (((monster.getAbilities().size() * SkillPoints.ABILITYCOST.getPoints()) + monster.getAttack()
					+ monster.getHealth()) - monster.getTypeEnum().getCost());
		} else {
			return ((monster.getAbilities().size() * SkillPoints.ABILITYCOST.getPoints()) + monster.getAttack()
					+ monster.getHealth());
		}
	}

	public int bpLeft(Monster monster) {
		return (SkillPoints.POINTSMAX.getPoints() - bpUsed(monster));
	}

	public void valStatChange(String stat, Monster mon, int change) throws Exception {
		int base = 0;
		if (stat.equals("attack")) {
			base = (mon.getAttack() - mon.getTypeEnum().getBaseATK());
		} else if (stat.equals("health")) {
			base = (mon.getHealth() - mon.getTypeEnum().getBaseDEF());
		}
		if (bpLeft(mon) < change) {
			throw new BuildPointException("This monster has " + bpLeft(mon)
					+ " build points remaining. The proposed action costs " + change + " build points");
		} else if (base + change <= 0) {
			throw new IndexOutOfBoundsException("This monster has a base " + stat + " of: " + base
					+ " and you cannot have an attack less than or equal to 0");
		}
	}

	public void validAbilityAdd(Monster mon, Ability ability) throws existingAbilityException, BuildPointException {
		if (bpLeft(mon) < SkillPoints.ABILITYCOST.getPoints()) {
			throw new BuildPointException("This monster has " + bpLeft(mon) + " 'build points' remaining, and "
					+ SkillPoints.ABILITYCOST.getPoints() + " 'build points' are required to add a new Ability.");
		}
		if (mon.getAbilities().contains(ability)) {
			throw new existingAbilityException(mon.getName() + " has already got ability " + ability.getName());
		}
	}

	public void validAbilityRemove(Monster mon, Ability ability) throws Exception {
		if (!(mon.getAbilities().contains(ability))) {
			throw new NoAbilityException("This monster does not have " + ability.getName());
		}
		if (mon.getTypeEnum().getInnate() == ability.getId()) {
			throw new Exception("Cannot remove innate abilities");
		}
	}
}
