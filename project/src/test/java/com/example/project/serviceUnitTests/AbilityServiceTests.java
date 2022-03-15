package com.example.project.serviceUnitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.project.classes.Ability;
import com.example.project.dto.AbilityDTO;
import com.example.project.exceptions.NoAbilityException;
import com.example.project.exceptions.NoTypeException;
import com.example.project.repo.abilityRepo;
import com.example.project.service.AbilityService;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AbilityServiceTests {

	@MockBean
	private abilityRepo repo;

	@Autowired
	private AbilityService service;

	@Test
	public void testFindInnate() {
		Ability testAbility = new Ability(0L, "test", "test");
		AbilityDTO testAbilityDTO = new AbilityDTO(0L, "test", "test");
		Optional<Ability> test = Optional.of(testAbility);
		Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(test);
		AbilityDTO result = null;
		try {
			result = service.findByInnate("undead");
		} catch (NoTypeException t) {
			fail("Should recognise type");
		}
		Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(testAbilityDTO);
		Mockito.verify(repo, Mockito.times(1)).findById(8L);
	}

	@Test
	public void testFName() {
		Ability testAbility = new Ability(0L, "test", "test");
		Ability testAbilityDTO = new Ability(0L, "test", "test");
		Optional<Ability> test = Optional.of(testAbility);
		Mockito.when(this.repo.findByName(Mockito.anyString())).thenReturn(test);
		AbilityDTO result = null;
		try {
			result = service.findName("test");
		} catch (NoAbilityException t) {
			fail("Should recognise type");
		}
		Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(testAbilityDTO);
		Mockito.verify(repo, Mockito.times(1)).findByName("test");
	}

	@Test
	public void testFId() throws NoAbilityException {
		Ability testAbility = new Ability(0L, "test", "test");
		AbilityDTO testDTO = new AbilityDTO(0L, "test", "test");
		Optional<Ability> test = Optional.of(testAbility);
		Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(test);
		AbilityDTO result = null;
		result = service.findById(1L);
		Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(testDTO);
		Mockito.verify(repo, Mockito.times(1)).findById(1L);
	}

	@Test
	public void testGetAll() {
		List<Ability> list = new ArrayList<Ability>();
		Mockito.when(this.repo.findAll()).thenReturn(list);
		assertThat(service.getAllAbiliity()).isEqualTo(list);
		Mockito.verify(repo, Mockito.times(1)).findAll();
	}

	@Test
	public void testAddAbility() {
		Ability test = new Ability(1L, "test", "test");
		Mockito.when(this.repo.save(test)).thenReturn(test);
		assertThat(service.addAbility(test)).usingRecursiveComparison().isEqualTo(test);
	}

	@Test
	public void testUpdate() {
		Ability test1 = new Ability(1L, "test", "test");
		Optional<Ability> opTest1 = Optional.of(test1);
		Ability test2 = new Ability(2L, "testUpdated", "testUpdated");
		Ability expected = new Ability(1L, "testUpdated", "testUpdated");
		Mockito.when(repo.findById(1L)).thenReturn(opTest1);
		Mockito.when(repo.save(test1)).thenReturn(test1);
		try {
			assertThat(service.Update(1L, test2)).usingRecursiveComparison().isEqualTo(expected);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		Mockito.verify(repo, Mockito.times(1)).findById(1L);
		Mockito.verify(repo, Mockito.times(1)).save(test1);
	}

	@Test
	public void testDeleteSuccess() {
		Ability test1 = new Ability(17L, "test", "test1");
		Mockito.when(repo.findById(17L)).thenReturn(Optional.of(test1));
		try {
			assertThat(service.DeleteAbility(17L)).isEqualTo("test has been deleted");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		Mockito.verify(repo, Mockito.times(1)).findById(17L);
	}
	
	@Test
	public void testDeleteInnate() {
		Ability evade = new Ability(4L, "Evade", "25% chance to avoid all damage");
		Mockito.when(repo.findById(4L)).thenReturn(Optional.of(evade));
		try {
			assertThat(service.DeleteAbility(4L)).isEqualTo("Cannot delete Abilities that are innate to a Type");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		Mockito.verify(repo, Mockito.times(1)).findById(4L);
	}
}
