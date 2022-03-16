package com.example.project.restTests;

import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
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
import com.example.project.repo.abilityRepo;
import com.example.project.repo.monsterRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:test-data-schema.sql",
		"classpath:monster-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class monsterControllerIntegerationTest {

	@Autowired
	private monsterRepo mrepo;
	
	@Autowired 
	private abilityRepo arepo;
	
	@Autowired
	private MockMvc mock;

	@Autowired
	private ModelMapper mapper;

	private MonsterDTO mapToDTO(Monster mon) {
		return this.mapper.map(mon, MonsterDTO.class);
	}

	@Autowired
	private ObjectMapper jsonifier;

	@BeforeEach
	public void addAbilities() {
		Monster mon1 = mrepo.findById(1L).get();
		mon1.setAbilities(List.of(arepo.findById(4L).get()));
		mrepo.save(mon1);
		
		Monster mon2 = mrepo.findById(2L).get();
		mon2.setAbilities(List.of(arepo.findById(4L).get(), arepo.findById(8L).get()));
		mrepo.save(mon2);
		
		Monster mon4 = mrepo.findById(4L).get();
		mon4.setAbilities(List.of(arepo.findById(6L).get(), arepo.findById(8L).get()));
		mrepo.save(mon4);
	}
	
	@Test
	@DirtiesContext
	public void testCreateMon() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/mon/create");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(this.jsonifier.writeValueAsString(test1));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isCreated();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.jsonifier.writeValueAsString(mapToDTO(test1)));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}

	@Test
	public void testGetAll() throws Exception {
		List<MonsterDTO> monList = new ArrayList<MonsterDTO>();
		monList.add(mapToDTO(test1));
		monList.add(mapToDTO(test2));
		monList.add(mapToDTO(test3));
		monList.add(mapToDTO(test4));
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/mon/getAll");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(this.jsonifier.writeValueAsString(monList));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}

	@Test
	public void testgetId() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/mon/get/1");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.jsonifier.writeValueAsString(mapToDTO(test1)));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);

	}

	@Test
	public void testGetType() throws Exception {
		List<MonsterDTO> monList = new ArrayList<MonsterDTO>();
		monList.add(mapToDTO(test2));
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/mon/getType/undead");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(this.jsonifier.writeValueAsString(monList));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);

	}

	@Test
	public void testGetName() throws Exception {
		List<MonsterDTO> monList = new ArrayList<MonsterDTO>();
		monList.add(mapToDTO(test3));
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/mon/getName/3");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(this.jsonifier.writeValueAsString(monList));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}

	@Test
	public void testCompare() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/mon/compare/1/2");
		mockRequest.accept(MediaType.TEXT_PLAIN);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().string("Comparing test1 and test2.\n"
				+ " Type: \n" + "      test1's type is Fae and test2's type is Undead.\n" + "\n" + " Attack: \n"
				+ "      They both have the same attack score of 7\n" + "\n" + " Health: \n"
				+ "      They both have the same health score of 7\n" + "\n" + " Abilities \n"
				+ "      test1's abilities are: [Ability [id = 4, name = evade, description = 25% chance to avoid all damage]]\n"
				+ "      test2's abilities are: [Ability [id = 4, name = evade, description = 25% chance to avoid all damage], Ability [id = 8, name = revive, description = once after going to <1 health, revert back to 1 health]]");
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	@DirtiesContext
	public void testUpdate() throws Exception {
			MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, "/mon/update/2");
			mockRequest.contentType(MediaType.APPLICATION_JSON);
			mockRequest.content(this.jsonifier.writeValueAsString(test1));
			mockRequest.accept(MediaType.APPLICATION_JSON);
			ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
			ResultMatcher matchContent = MockMvcResultMatchers.content()
					.json(this.jsonifier.writeValueAsString(mapToDTO(updated1)));
			this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
		}

	@Test
	public void testChangeHealth() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PATCH, "/mon/ChangeHealth/1/-2");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.jsonifier.writeValueAsString(mapToDTO(updated2)));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void testChangeAttack() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PATCH, "/mon/ChangeAttack/1/-2");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.jsonifier.writeValueAsString(mapToDTO(updated3)));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	@DirtiesContext
	public void testDelete() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.DELETE, "/mon/Delete/2");
		mockRequest.accept(MediaType.TEXT_PLAIN);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().string("test2 has been deleted");
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	@DirtiesContext
	public void testAddAbility() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/mon/addAbility/2/momentum");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.jsonifier.writeValueAsString(mapToDTO(updated4)));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void testRemoveAbility() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.DELETE, "/mon/removeAbility/2/evade");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.jsonifier.writeValueAsString(mapToDTO(updated5)));
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	private final Ability evade = new Ability(4L, "evade", "25% chance to avoid all damage");
	private final Ability revive = new Ability(8L, "revive", "once after going to <1 health, revert back to 1 health");
	private final Ability momentum = new Ability(6L, "momentum", "gain 1 bonus damage after each attack");
	private final List<Ability> test1List = List.of(evade);
	private final List<Ability> test2List = List.of(revive, evade);
	private final List<Ability> removedlist = List.of(revive);
	private final List<Ability> addedlist = List.of(revive, evade, momentum);
	private final List<Ability> test3List = new ArrayList<Ability>();
	private final List<Ability> test4List = List.of(momentum, revive);

	private final Monster test1 = new Monster(1L, "test1", 7, 7, "Fae", test1List, "test desc", Type.FAE, true);
	private final Monster test2 = new Monster(2L, "test2", 4, 7, "Undead", test2List, "2nd test desc has abilities",
			Type.UNDEAD, true);
	private final Monster test3 = new Monster(3L, "test3", 3, 4, "Nature", test3List, "3rd test desc", Type.NATURE,
			false);
	private final Monster test4 = new Monster(4L, "test4", 4, 7, "Beast", test4List, "4th test desc has abilities",
			Type.BEAST, true);
	private final Monster updated1 = new Monster(2L, "test1", 7, 7, "Fae", test1List, "test desc", Type.FAE, true);
	private final Monster updated2 = new Monster(1L, "test1", 7, 5, "Fae", test1List, "test desc", Type.FAE, true);
	private final Monster updated3 = new Monster(1L, "test1", 5, 7, "Fae", test1List, "test desc", Type.FAE, true);
	private final Monster updated4 = new Monster(2L, "test2", 4, 7, "Undead", addedlist, "2nd test desc has abilities",
			Type.UNDEAD, true);
	private final Monster updated5 = new Monster(2L, "test2", 4, 7, "Undead", removedlist, "2nd test desc has abilities",
			Type.UNDEAD, true);
	private final Monster delete5 = new Monster(5L, "delete", 8, 8, "Undead", removedlist, "to be deleted", Type.UNDEAD, true);
}
