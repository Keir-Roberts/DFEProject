package com.example.project.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.classes.Monster;
import com.example.project.dto.MonsterDTO;
import com.example.project.service.MonsterService;
import com.example.project.service.ValidateService;

@RestController
public class MonsterRestController {

	private MonsterService service;
	
	private ValidateService valid;

	public MonsterRestController(MonsterService service, ValidateService valid) {
		this.service = service;
		this.valid = valid;
	}

	@PostMapping("/mon/create")
	public ResponseEntity<MonsterDTO> build(@RequestBody Monster monster) throws Exception {
		valid.convertStrToEnum(monster);
		return new ResponseEntity<MonsterDTO>(this.service.mapToDTO(this.service.AddMonster(monster)), HttpStatus.CREATED);
	}

	@GetMapping("/mon/getAll")
	public List<MonsterDTO> getAll() {
		return this.service.mapListDTO(this.service.getAllMon());

	}

	@GetMapping("/mon/get/{id}")
	public MonsterDTO getMonid(@PathVariable("id") Long id) throws Exception {
		return this.service.mapToDTO(this.service.getID(id));
	}

	@GetMapping("/mon/getType/{type}")
	public List<MonsterDTO> getMonType(@PathVariable("type") String type) throws Exception {
		return this.service.mapListDTO(service.getType(type));
	}

	@GetMapping("/mon/getName/{name}")
	public List<MonsterDTO> getMonName(@PathVariable("name") String name) {
		return this.service.mapListDTO(service.getName(name));
	}

	@GetMapping("/mon/compare/{ID1}/{ID2}")
	public String compareMon(@PathVariable("ID1") Long id1, @PathVariable("ID2") Long id2) throws Exception {
		return service.compare(id1, id2);
	}

	@PutMapping("/mon/update/{id}")
	public MonsterDTO updateMon(@RequestBody Monster monster, @PathVariable("id") long id)
			throws Exception {
		return this.service.mapToDTO(service.Update(id, monster));
	}

	@PatchMapping("/mon/ChangeHealth/{id}/{change}")
	public MonsterDTO changeHealth(@PathVariable("id") long id, @PathVariable("change") int change)
			throws Exception {
		return this.service.mapToDTO(service.updateMonHealth(id, change));
	}

	@PatchMapping("/mon/ChangeAttack/{id}/{change}")
	public MonsterDTO changeAttack(@PathVariable("id") long id, @PathVariable("change") int change) throws Exception {
		return this.service.mapToDTO(service.updateMonAttack(id, change));
	}

	@DeleteMapping("/mon/Delete/{id}")
	public String deleteMon(@PathVariable("id") long id) throws Exception {
		return service.deleteMon(id);
	}

	@PostMapping("/mon/addAbility/{id}/{name}")
	public MonsterDTO addMA(@PathVariable("id") long id, @PathVariable("name") String name) throws Exception {
		return this.service.mapToDTO(service.addMonAbility(id, name));
	}
	
	@DeleteMapping("/mon/removeAbility/{id}/{name}")
	public MonsterDTO deleteMA(@PathVariable("id") long id, @PathVariable("name") String name) throws Exception {
		return this.service.mapToDTO(service.removeMonAbility(id, name));
	}
}