package com.example.project.rest;

import java.util.List;

import javax.websocket.server.PathParam;

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
		service.BPCheck(monster);
		return service.AddMonster(monster);
	}

	@GetMapping("/getAllMon")
	public List<Monster> getAll() {
		return service.getAllMon();

	}

	@GetMapping("/GetMon/{id}")
	public Monster getMonid(@PathVariable("id") Long id) {
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

	@GetMapping("/compareMon")
	public String compareMon(@PathParam("ID1") Long id1, @PathParam("ID2") Long id2) {
		return service.compare(id1, id2);
	}

	@PutMapping("/updateMon/{id}")
	public Monster updateMon(@RequestBody Monster monster, @PathVariable("id") long id)
			throws BuildPointException, NoTypeException, NoAbilityException {
		return service.Update(id, monster);
	}

	@PatchMapping("/monAddAbility/{id}")
	public Monster addMonAbility(@PathVariable("id") long id, @PathParam("Ability name") String name)
			throws NoAbilityException, NoTypeException, BuildPointException {
		return service.addMonAbility(id, name);
	}

	@PatchMapping("/monRemoveAbility/{id}")
	public Monster removeMonAbility(@PathVariable("id") long id, @PathParam("Ability name") String name)
			throws NoAbilityException, NoTypeException {
		return service.removeMonAbility(id, name);
	}

	@PatchMapping("/monChangeHealth/{id}")
	public Monster changeHealth(@PathVariable("id") long id, @PathParam("Health Change") int change)
			throws BuildPointException, NoTypeException, NoAbilityException {
		return service.updateMonHealth(id, change);
	}

	@PatchMapping("/monChangeAttack/{id}")
	public Monster changeAttack(@PathVariable("id") long id, @PathParam("Attack Change") int change)
			throws BuildPointException, NoTypeException, NoAbilityException {
		return service.updateMonAttack(id, change);
	}
	
	@DeleteMapping("/monDelete/{id}")
	public Monster deleteMon(@PathVariable("id") long id) {
		return service.deleteMon(id);
	}
}