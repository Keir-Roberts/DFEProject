package com.example.project.classes;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.project.enums.Ability;
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
	private Type type;
	
	@Column
	private List<Ability> abilities;
	
	@Column
	private String description;

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

	public Monster(long id, String name, int attack, int health, Type type, List<Ability> abilities,
			String description) {
		super();
		this.id = id;
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.type = type;
		this.abilities = abilities;
		this.description = description;
	}

	public Monster(String name, int attack, int health, Type type, List<Ability> abilities, String description) {
		super();
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.type = type;
		this.abilities = abilities;
		this.description = description;
	}

	public Monster() {
		super();
	}
	
	
}
