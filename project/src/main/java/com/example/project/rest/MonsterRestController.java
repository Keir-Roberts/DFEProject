package com.example.project.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.classes.Monster;
import com.example.project.exceptions.BuildPointException;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.example.project.service.MonsterService;

@RestController
public class MonsterRestController {

	public MonsterService service;

	public MonsterRestController(MonsterService service) {
		this.service = service;
	}

	@PostMapping("/createMon")
	public Monster build(@RequestBody Monster monster) throws BuildPointException, NoTypeException, NoAbilityException {
		service.convertStrToEnum(monster);
		return service.AddMonster(monster);
	}

	@GetMapping("/getAllMon")
	public List<Monster> getAll() {
		return service.getAllMon();

	}

	@GetMapping("/getMon/{id}")
	public Monster getMonid(@PathVariable("id") Long id) throws NoTypeException {
		return service.readID(id);
	}

	@GetMapping("/getMonType/{type}")
	public List<Monster> getMonType(@PathVariable("type") String type) throws Exception {
		return service.getType(type);
	}

	@GetMapping("/getMonName/{name}")
	public List<Monster> getMonName(@PathVariable("name") String name) {
		return service.getName(name);
	}

	@GetMapping("/compareMon/{ID1}/{ID2}")
	public String compareMon(@PathVariable("ID1") Long id1, @PathVariable("ID2") Long id2) throws NoTypeException {
		return service.compare(id1, id2);
	}

	@PutMapping("/updateMon/{id}")
	public Monster updateMon(@RequestBody Monster monster, @PathVariable("id") long id)
			throws BuildPointException, NoTypeException, NoAbilityException {
		return service.Update(id, monster);
	}

	@PatchMapping("/monChangeHealth/{id}/{change}")
	public Monster changeHealth(@PathVariable("id") long id, @PathVariable("change") int change)
			throws BuildPointException, NoTypeException, NoAbilityException {
		return service.updateMonHealth(id, change);
	}

	@PatchMapping("/monChangeAttack/{id}/{change}")
	public Monster changeAttack(@PathVariable("id") long id, @PathVariable("change") int change)
			throws Exception {
		return service.updateMonAttack(id, change);
	}
	
	@DeleteMapping("/monDelete/{id}")
	public String deleteMon(@PathVariable("id") long id) throws NoTypeException {
		return service.deleteMon(id);
	}
}