package com.example.project.classes;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MonAbility {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "monsterID")
	Monster monster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "abilityName")
	Ability ability;

	public MonAbility(long id, Monster monster, Ability ability) {
		super();
		this.id = id;
		this.monster = monster;
		this.ability = ability;
	}

	public MonAbility(Monster monster, Ability ability) {
		super();
		this.monster = monster;
		this.ability = ability;
	}

	public MonAbility() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	public Ability getAbility() {
		return ability;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;
	}
	
	
}
