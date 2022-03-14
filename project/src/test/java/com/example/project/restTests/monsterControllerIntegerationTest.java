package com.example.project.restTests;

import org.springframework.test.context.jdbc.Sql.ExecutionPhase;	
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.project.classes.Ability;
import com.example.project.classes.Monster;
import com.example.project.dto.MonsterDTO;
import com.example.project.enums.Type;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:monster-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class monsterControllerIntegerationTest {

	@Autowired
	private MockMvc mock;
	
	@Autowired
	private ModelMapper mapper;
	
	private MonsterDTO mapToDTO(Monster mon) {
		return this.mapper.map(mon, MonsterDTO.class);
	}
	
	@Autowired
    private ObjectMapper jsonifier;
	
	
	
@Test
public void testCreateMon() throws Exception {
	MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/mon/create");
	mockRequest.contentType(MediaType.APPLICATION_JSON);
	mockRequest.content(this.jsonifier.writeValueAsString(test1));
	mockRequest.accept(MediaType.APPLICATION_JSON);
	ResultMatcher matchStatus = MockMvcResultMatchers.status().isCreated();
	ResultMatcher matchContent = MockMvcResultMatchers.content().json(this.jsonifier.writeValueAsString(mapToDTO(test1)));
	this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
}

private final Ability evade = new Ability(4L, "evade", "25% chance to avoid all damage"); 
private final Ability revive = new Ability(8L, "revive", "once after going to <1 health, revert back to 1 health");
private final Ability momentum = new Ability(6L, "momentum", "gain 1 bonus damage after each attack");
private final List<Ability> test1List = List.of(evade);
private final List<Ability> test2List = List.of(revive, evade);
private final List<Ability> test3List = new ArrayList<Ability>();
private final List<Ability> test4List = List.of(momentum, revive);

private final Monster test1 = new Monster(1L, "test1", 7, 7, "Fae", test1List, "test desc", Type.FAE, true);
private final Monster test2 = new Monster(2L, "test2", 4, 7, "Undead", test2List, "2nd test desc has abilities", Type.UNDEAD, true);
private final Monster test3 = new Monster(3L, "test3", 3, 4, "Nature", test3List, "3rd test desc", Type.NATURE, false);
private final Monster test4 = new Monster(4L, "test4", 4, 7, "Beast", test4List, "4th test desc has abilities", Type.BEAST, true);

}
