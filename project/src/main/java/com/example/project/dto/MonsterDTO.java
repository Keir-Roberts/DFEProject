package com.example.project.dto;

import java.util.List;

import com.example.project.classes.Ability;

public class MonsterDTO {

	private Long id;
	
	private String name;
	
	private int attack;
	
	private int health;
	
	private String type;
	
	private String description;
	
	private Boolean built;
	
	private List<Ability> abilities;

	public MonsterDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getBuilt() {
		return built;
	}

	public void setBuilt(Boolean built) {
		this.built = built;
	}

	public List<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(List<Ability> abilities) {
		this.abilities = abilities;
	}

	public MonsterDTO(Long id, String name, int attack, int health, String type, List<Ability> abilities, Boolean built,
			String description) {
		super();
		this.id = id;
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.type = type;
		this.description = description;
		this.built = built;
		this.abilities = abilities;
	}
	
		
}
