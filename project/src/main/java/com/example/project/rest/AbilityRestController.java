package com.example.project.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.classes.Ability;
import com.example.project.dto.AbilityDTO;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.example.project.service.AbilityService;

@RestController
public class AbilityRestController {

	public AbilityService service;
	
	public AbilityRestController(AbilityService service) {
		this.service = service;
	}
	
	@PostMapping("/ability/create") 
	public ResponseEntity<AbilityDTO> createAbility(@RequestBody Ability ability) {
	return new ResponseEntity<AbilityDTO>(this.service.addAbility(ability), HttpStatus.CREATED);
}
	
	@GetMapping("/ability/getAll")
	public List<AbilityDTO> getAll() {
		return this.service.getAllAbiliity();
	}
	
	@GetMapping("/ability/findInnate/{type}")
	public AbilityDTO getInnate(@PathVariable("type") String type) throws NoTypeException {
		return this.service.findByInnate(type);
	}
	
	@GetMapping("/ability/findName/{name}")
	public AbilityDTO getName(@PathVariable("name") String name) throws NoAbilityException {
	return this.service.findName(name);
	}
	
	@GetMapping("/ability/findID/{id}")
	public AbilityDTO getID(@PathVariable("id") Long id) {
		return this.service.findById(id);
	}
	
	@PutMapping("/ability/update/{id}")
	public AbilityDTO update(@RequestBody Ability ability, @PathVariable("id") Long id) throws Exception {
		return this.service.Update(id, ability);
	}
	
	@DeleteMapping("/ability/delete/{id}")
	public String delete(@PathVariable("id") Long id) throws Exception {
		return this.service.DeleteAbility(id);
	}
}