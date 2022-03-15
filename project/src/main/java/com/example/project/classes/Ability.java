package com.example.project.classes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Ability {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@ManyToMany(fetch = FetchType.LAZY ,cascade = {CascadeType.ALL})
	@JoinTable(name = "mon_ability", joinColumns = @JoinColumn(name = "ability_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "monster_id", referencedColumnName = "id"))
	private List<Monster> monsters = new ArrayList<Monster>();
	
	
	public List<Monster> getMonsters() {
		return monsters;
	}

	public void setMonsters(List<Monster> monsters) {
		this.monsters = monsters;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Ability(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public Ability(long id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Ability() {
		super();
	}

	@Override
	public String toString() {
		return "Ability [id = " + id + ", name = " + name + ", description = " + description + "]";
	}
	
	
}
