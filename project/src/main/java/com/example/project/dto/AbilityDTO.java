package com.example.project.dto;

public class AbilityDTO {

	private Long id;
	
	private String name;
	
	private String description;

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

	public AbilityDTO(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public AbilityDTO(long id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public AbilityDTO() {
		super();
	}

	@Override
	public String toString() {
		return "Ability [id = " + id + ", name = " + name + ", description = " + description + "]";
	}
	
	
}
