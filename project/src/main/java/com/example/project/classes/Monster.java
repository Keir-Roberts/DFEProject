package com.example.project.classes;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

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
	
	@OneToMany(mappedBy="monster")
	private List<MonAbility> MonAbility;
	
	@Column
	private String description;
	
	@Transient
	private Type type;

	private boolean built = false;
	
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

	public List<MonAbility> getMonAbility() {
		return MonAbility;
	}

	public void setMonAbility(List<MonAbility> monAbility) {
		MonAbility = monAbility;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Monster(long id, String name, int attack, int health, String typeStr, String description) {
		super();
		this.id = id;
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.typeStr = typeStr;
		this.description = description;
	}

	public Monster(String name, int attack, int health, String typeStr, String description) {
		super();
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.typeStr = typeStr;
		this.description = description;
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
				+ typeStr + "\n";
	}
	
	
}
