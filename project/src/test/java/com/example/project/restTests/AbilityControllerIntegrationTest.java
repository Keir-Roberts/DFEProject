package com.example.project.restTests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.project.classes.Ability;
import com.example.project.repo.abilityRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AbilityControllerIntegrationTest {

	@Autowired
	private MockMvc mock;
	
	@Autowired
	private ObjectMapper jsonifier;
	
	@Autowired
	private abilityRepo repo;
	
	private final Ability nul = new Ability(16L, "null", "null");
	private final Ability sturdy = new Ability(1L, "sturdy", "Reduces damage by 1" );
	private final Ability critical = new Ability(2L, "critical", "25% chance to deal double damage");
	private final Ability perfectionist = new Ability(3L, "perfectionist", "Deal 3 bonus damage when unharmed");
	private final Ability evade = new Ability(4L, "evade", "25% chance to avoid all damage");
	private final Ability draining = new Ability(5L, "draining", "heal self by half damage done");
	private final Ability momentum = new Ability(6L, "momentum", "gain 1 bonus damage after each attack");
	private final Ability regenerate = new Ability(7L, "regenerate", "heals 2 after each attack");
	private final Ability revive = new Ability(8L, "revive", "once after going to <1 health, revert back to 1 health");
	private final Ability atkBaton = new Ability(9L, "atkbaton", "give the next monster in this monster's team half of this monster's attack when defeated");
	private final Ability defBaton = new Ability(10L, "defbaton", "give the next monster in this monster's team half of this monster's health when defeated");
	private final Ability pact = new Ability(11L, "pact", "gain attack and defense equal to half of the attack and health of the next monster in this party but the next monster takes 2 damage and has 1 less attack");
	private final Ability reflect = new Ability(12L, "reflect", "when this monster takes damage, deal damage to the opponent equal to half the damage taken rounded up");
	private final Ability ruthless = new Ability(13L, "ruthless", "this monster deals 3x damage to those it is strong against instead of 2x. If the opponent has defensive, they cancel out");
	private final Ability defensive = new Ability(14L, "defensive", "this monster does not take extra damage from enemies that are strong against it. if the opponent has ruthless, they cancel out");
	private final Ability retaliate = new Ability(15L, "retaliate", "this monster deals 1 extra damage per piece of health missing");
	private final List<Ability> all = List.of(nul, sturdy, critical, perfectionist, evade, draining, momentum, regenerate, revive, atkBaton, defBaton, pact, reflect, ruthless, defensive, retaliate);
	private final Ability test = new Ability(17L, "test", "test");
	private final Ability updated = new Ability(16L, "test", "test");
	private final Ability delete = new Ability(18L, "delete", "delete");
	@Test
	public void testFindInnate() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/ability/findInnate/fae");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(this.jsonifier.writeValueAsString(evade));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void testGetAll() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/ability/getAll");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(this.jsonifier.writeValueAsString(all));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void testFindName() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/ability/findName/evade");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(this.jsonifier.writeValueAsString(evade));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void testFindId() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/ability/findID/4");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(this.jsonifier.writeValueAsString(evade));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void testCreate() throws Exception {
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/ability/create");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(this.jsonifier.writeValueAsString(test));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isCreated();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.jsonifier.writeValueAsString(test));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	repo.delete(test);
	}
	
	@Test
	public void testUpdate() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, "/ability/update/16");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(this.jsonifier.writeValueAsString(test));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.jsonifier.writeValueAsString(updated));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void testDelete() throws Exception {
		repo.save(delete);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.DELETE, "/ability/delete/18");
		mockRequest.accept(MediaType.TEXT_PLAIN);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().string("delete has been deleted");
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
}

