package edu.uoc.epcsd.unitTest;


import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.user.UserNotFoundException;
import edu.uoc.epcsd.user.domain.Rol;
import edu.uoc.epcsd.user.domain.Role;
import edu.uoc.epcsd.user.domain.User;
import net.minidev.json.JSONUtil;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.glassfish.jersey.servlet.WebConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import edu.uoc.epcsd.user.application.request.LoginRequest;
import edu.uoc.epcsd.user.application.request.RegisterRequest;
import edu.uoc.epcsd.user.application.rest.UserRESTController;
import edu.uoc.epcsd.user.domain.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(value = UserRESTController.class)
@ContextConfiguration(classes = {UserRESTController.class, WebConfig.class})
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
class UserControllerUnitTest {
	
	static String USER_TEST_NAME = "EXAMPLE_TEST";
	static String USER_TEST_EMAIL = "email@exmaple.com";
	static String USER_TEST_PASSWORD = "PASSWORD";

	private static final String REST_USERS_PATH = "/users";

	@Autowired
	private MockMvc mvc;

    @MockBean
    private UserService service;

	// Create test user list
	public List<User> CreateTestUserList(){
		List<User> user = new ArrayList<User>();
		User example = CreateTestUser();
		user.add(example);
		return user;
	}

	public User CreateTestUser(){
		User example = new User();
		example.setName(USER_TEST_NAME);
		example.setEmail(USER_TEST_EMAIL);
		example.setPassword(USER_TEST_PASSWORD);
		return example;
	};

	// post /register
	@Test
	@DisplayName("RegisterUser")
	public void rest_call_returns_new_userid() throws Exception {
		Set<Role> roles = new HashSet<>(Collections.singletonList(new Role(null, "BUYER")));

		User user = new User(null, USER_TEST_NAME, "", USER_TEST_EMAIL,
				USER_TEST_PASSWORD, roles, false);

		Mockito.when(service.createUser(Mockito.any(User.class))).thenReturn(user.getId());

		Mockito.when(service.findUserByMail(USER_TEST_EMAIL)).thenReturn(Optional.empty());

		RegisterRequest request = new RegisterRequest(USER_TEST_NAME, "", USER_TEST_EMAIL, USER_TEST_PASSWORD);
		mvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(request))).andExpect(status().isOk());
	}

	// post /login
	@Test
	@DisplayName("LoginUser")
	public void rest_call_returns_session_token() throws Exception {
		User testUser = CreateTestUser();

		Mockito.when(service.findUserByMail(USER_TEST_EMAIL)).thenReturn(Optional.of(testUser));

		LoginRequest request = new LoginRequest(USER_TEST_EMAIL, USER_TEST_PASSWORD);
		mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapToJson(request))).andExpect(status().isOk());
	}

	// get /users
	@Test
	@DisplayName("FindAllUsers")
	public void rest_call_returns_all_users() throws Exception{

        given(service.findAllUsers()).willReturn(CreateTestUserList());

        MvcResult result = mvc.perform(get(REST_USERS_PATH)).andDo(print()).andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).contains(USER_TEST_NAME);
	}

	// get /users/{id}
	@Test
	@DisplayName("FindExistingUserById")
	public void rest_call_returns_user() throws Exception{

		User user = new User();
		user.setId(99L);
		user.setName("nameTest");
		user.setSurname("surnameUser");

		Mockito.when(service.findUserById(99L)).thenReturn(Optional.ofNullable(user));

		mvc.perform(get(REST_USERS_PATH+"/"+user.getId().toString())
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("name", is(user.getName())))
				.andExpect(jsonPath("surname", is(user.getSurname())));
	}

	// get /users/{id}
	@Test
	@DisplayName("FindNotExistingUserById")
	public void rest_call_returns_user_not_exists() throws Exception{

		User user = new User();
		user.setName("Test");
		user.setId(99L);

		Mockito.doThrow(new UserNotFoundException(user.getId())).when(service).findUserById(user.getId());

		mvc.perform(get(REST_USERS_PATH+"/"+user.getId().toString())
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());
	}

	// delete /users/{id}
	@Test
	@DisplayName("DeleteExistingUser")
	public void rest_call_deletes_user() throws Exception{

		User user = new User();
		user.setName("Test");
		user.setId(99L);

		doNothing().when(service).deleteUser(user.getId());

		mvc.perform(delete(REST_USERS_PATH+"/"+user.getId().toString())
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
	}

	// delete /users/{id}
	@Test
	@DisplayName("DeleteNotExistingUser")
	public void rest_call_deletes_user_not_exists() throws Exception{

		User user = new User();
		user.setName("Test");
		user.setId(99L);

		Mockito.doThrow(new UserNotFoundException(user.getId())).when(service).deleteUser(user.getId());
		
		mvc.perform(delete(REST_USERS_PATH+"/"+user.getId().toString())
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound());
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return objectMapper.writeValueAsString(obj);
	}
}
