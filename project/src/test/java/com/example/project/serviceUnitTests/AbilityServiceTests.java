package com.example.project.serviceUnitTests;

import static org.junit.Assert.fail;
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
		Optional<Ability> test = Optional.of(testAbility);
		Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(test);
		Ability result = null;
		try {
			result = service.findByInnate("undead");
		} catch (NoTypeException t) {
			fail("Should recognise type");
		}	
		Assertions.assertThat(result).isEqualTo(testAbility);
	}
	
	@Test
	public void testFName() {
		Ability testAbility = new Ability(0L, "test", "test");
		Optional<Ability> test = Optional.of(testAbility);
		Mockito.when(this.repo.findByName(Mockito.anyString())).thenReturn(test);
		Ability result = null;
		try {
			result = service.findName("test");
		} catch (NoAbilityException t) {
			fail("Should recognise type");
		}	
		Assertions.assertThat(result).isEqualTo(testAbility);
	
	}
	
	@Test
	public void testFId() throws NoAbilityException {
		Ability testAbility = new Ability(0L, "test", "test");
		Optional<Ability> test = Optional.of(testAbility);
		Mockito.when(this.repo.findById(Mockito.anyLong())).thenReturn(test);
		Ability result = null;
		result = service.findById(1L);	
		Assertions.assertThat(result).isEqualTo(testAbility);
	}
}
