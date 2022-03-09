package com.example.project.enums;
import com.example.project.exceptions.NoTypeException;
public enum Type {
	ABOMINATION("Abomination", 2, 5, "RETALIATE", 10), BEAST("Beast", 3, 4, "MOMENTUM", 10), CELESTIAL("Celestial", 4, 3, "PERFECTIONIST", 10), CONSTRUCT("Construct", 2, 5, "STURDY", 10),
	DEMON("Demon", 5, 2, "DRAINING", 10), DRAGON("Dragon", 5, 2, "CRITICAL", 10), ELEMENTAL("Elemental", 6, 1, "REFLECT", 10), FAE("Fae", 4, 3, "EVADE", 10),
	NATURE("Nature", 3, 4, "REGENERATE", 10), UNDEAD("Undead", 1, 6, "REVIVE", 10);

	private final String type;
	private final int baseATK;
	private final int baseDEF;
	private final String innate;
	private final int points;

	Type(String type, int baseATK, int baseDEF, String innate, int points) {
		this.type = type;
		this.baseATK = baseATK;
		this.baseDEF = baseDEF;
		this.innate = innate;
		this.points = points;
	}
	
	public String getType() {
		return type;
	}

	public int getBaseATK() {
		return baseATK;
	}

	public int getBaseDEF() {
		return baseDEF;
	}

	public int getAttack(int atkIn) {
		return (baseATK + atkIn);
	}
	
	public int getDef(int defIn) {
		return (baseDEF + defIn);
	}

	public String getInnate() {
		return innate;
	}

	public int getPoints() {
		return points;
	}
	
	public static Type strType(String in) throws NoTypeException {
		for (Type type: Type.values()) {
			if(in.toLowerCase().equals(type.getType().toLowerCase())) return type;
		}
		throw new NoTypeException("Could not find a type called '" + in + "'." );
	}
	
	public int getCost() {
		return (baseATK + baseDEF + SkillPoints.ABILITYCOST.getPoints());
	}
}
