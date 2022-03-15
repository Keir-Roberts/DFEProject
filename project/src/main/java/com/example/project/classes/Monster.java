package com.example.project.classes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.modelmapper.ModelMapper;

import com.example.project.dto.AbilityDTO;
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
	private String type;
	
	@ManyToMany(fetch = FetchType.LAZY ,cascade = {CascadeType.ALL})
	@JoinTable(name = "mon_ability", joinColumns = @JoinColumn(name = "monster_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ability_id", referencedColumnName = "id"))
	private List<Ability> Abilities = new ArrayList<Ability>();
	
	@Column
	private String description;
	
	@Transient
	private Type typeEnum;

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

	public Type getTypeEnum() {
		return typeEnum;
	}

	public void setTypeEnum(Type type) {
		this.typeEnum = type;
	}

	public List<AbilityDTO> getAbilities() {
		List<AbilityDTO> out = new ArrayList<AbilityDTO>();
		ModelMapper mapper = new ModelMapper();
		for (Ability a: Abilities) {
			out.add(mapper.map(a, AbilityDTO.class));
		}
		return out;
	}

	public List<Ability> trueGetAbilities() {
		return Abilities;
	}
	
	public void setAbilities(List<Ability> abilities) {
		Abilities = abilities;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Monster(long id, String name, int attack, int health, String type, String description, boolean built) {
		super();
		this.id = id;
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.type = type;
		this.description = description;
		this.built = built;
	}

	public Monster(long id, String name, int attack, int health, String type, String description) {
		super();
		this.id = id;
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.type = type;
		this.description = description;
	}

	public Monster(long id, String name, int attack, int health, String type, List<Ability> abilities,
			String description, Type typeEnum, boolean built) {
		super();
		this.id = id;
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.typeEnum = typeEnum;
		Abilities = abilities;
		this.description = description;
		this.type = type;
		this.built = built;
	}

	public Monster(String name, int attack, int health, String type, String description) {
		super();
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Monster ID = " + id + ", Name = " + name + ", Attack = " + attack + ", Health = " + health + ", Type ="
				+ type + "\n";
	}
	
	
}
