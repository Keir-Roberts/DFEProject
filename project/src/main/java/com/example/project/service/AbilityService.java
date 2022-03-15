package com.example.project.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.project.classes.Ability;
import com.example.project.classes.Monster;
import com.example.project.dto.AbilityDTO;
import com.example.project.enums.Type;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.example.project.repo.abilityRepo;

@Service
public class AbilityService {

	abilityRepo abRepo;
	private ModelMapper mapper;
	
	public AbilityService(abilityRepo abRepo, ModelMapper mapper) {
		this.abRepo = abRepo;
		this.mapper = mapper;
	}
	
	public AbilityDTO mapToDTO(Ability ability) {
		return this.mapper.map(ability, AbilityDTO.class);
	}
	
	public AbilityDTO findByInnate(String type) throws NoTypeException {
		Type t = Type.strType(type);
		Ability out = abRepo.findById(t.getInnate()).get();
		return mapToDTO(out);
	}
	
	public AbilityDTO findName(String name) throws NoAbilityException {
		return mapToDTO(abRepo.findByName(name).get());
	}
	
	public AbilityDTO findById(Long id) {
		return mapToDTO(abRepo.findById(id).get());
	}
	
	public List<AbilityDTO> getAllAbiliity() {
		List<Ability> abilities = abRepo.findAll();
		List<AbilityDTO> out = new ArrayList<AbilityDTO>();
		for (Ability a : abilities) {
			out.add(mapToDTO(a));
		}
		return out;
	}
	
	public AbilityDTO addAbility(Ability abi) {
		return mapToDTO(abRepo.save(abi));
	}
	
	public AbilityDTO Update(Long id, Ability ability) throws Exception {
		Ability original = abRepo.findById(id).get();
		original.setName(ability.getName());
		original.setDescription(ability.getDescription());
		return mapToDTO(abRepo.save(original));
	}
	
	public String DeleteAbility(Long id) throws Exception{
		Ability out = abRepo.findById(id).get();
		String name = out.getName();
		List<Monster> monsters = new ArrayList<Monster>();
		if (id < 0) { return "Has to be a positive number";
		}
		for(Type t : Type.values()) {
			if(t.getInnate()==id) {
			return "Cannot delete Abilities that are innate to a Type"; 
		}
		out.setMonsters(monsters);
		abRepo.save(out);
		}
	abRepo.delete(out);
	return name + " has been deleted";
	}
}