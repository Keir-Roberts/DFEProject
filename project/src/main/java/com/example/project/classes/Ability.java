package com.example.project.classes;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Ability {

	@Id
	private String name;
	
	@Column
	private String description;

	@OneToMany(mappedBy="ability")
	private List<MonAbility> MonAbility;
	
	public List<MonAbility> getMonAbility() {
		return MonAbility;
	}

	public void setMonAbility(List<MonAbility> monAbility) {
		MonAbility = monAbility;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Ability(String name, String description, String InnateTo) {
		super();
		this.name = name;
		this.description = description;
	}

	public Ability() {
		super();
	}
	
	
}
