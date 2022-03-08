package com.example.project.classes;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.project.enums.Ability;
import com.example.project.enums.SkillPoints;
import com.example.project.enums.Type;

@Entity
public class Monster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String name;
	
	@Column
	private int attack;
	
	@Column
	private int health;
	
	@Column
	private String typeStr;
	
	@Column
	private List<String> abilityStr;
	
	private List<Ability> abilities;
	
	private int BPLeft;
	
	public int getBPLeft() {
		setBPLeft();
		return BPLeft;
	}

	public void setBPLeft() {
		BPLeft = SkillPoints.POINTSMAX.getPoints() - this.getBuildPoints();
	}

	@Column
	private String description;
	
	private Type type;

	private boolean built = false;
	
	private int buildPoints;
	
	public void  setBuildPoints() {
		if (built) {
			buildPoints = ((SkillPoints.ABILITYCOST.getPoints() * abilities.size()) + attack + health - type.getPoints());
		} else {
			buildPoints = ((3 * abilities.size()) + attack + health);
		}
	}
	
	public int getBuildPoints() {
		setBuildPoints();
		return buildPoints;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(List<Ability> abilities) {
		this.abilities = abilities;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Monster(long id, String name, int attack, int health, String typeStr, List<String> abilityStr,
			String description) {
		super();
		this.id = id;
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.typeStr = typeStr;
		this.abilityStr = abilityStr;
		this.description = description;
	}

	public Monster(String name, int attack, int health, String typeStr, List<String> abilityStr, String description) {
		super();
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.typeStr = typeStr;
		this.abilityStr = abilityStr;
		this.description = description;
	}

	public List<String> getAbilityStr() {
		return abilityStr;
	}

	public void setAbilityStr(List<String> abilityStr) {
		this.abilityStr = abilityStr;
	}

	public Monster() {
		super();
	}

	public boolean isBuilt() {
		return built;
	}

	public void setBuilt(boolean built) {
		this.built = built;
	}

	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	@Override
	public String toString() {
		return "Monster ID=" + id + ", name=" + name + ", attack=" + attack + ", health=" + health + ", typeStr="
				+ typeStr + ", abilityStr=" + abilityStr + "\n";
	}
	
	
}
