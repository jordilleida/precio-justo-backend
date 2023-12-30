package edu.uoc.tfg.unitTest;


import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.user.UserNotFoundException;
import edu.uoc.epcsd.user.domain.Rol;
import edu.uoc.epcsd.user.domain.Role;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.service.RoleService;
import edu.uoc.epcsd.user.domain.service.TokenService;
import edu.uoc.epcsd.user.infrastructure.kafka.KafkaUserMessagingService;
import org.glassfish.jersey.servlet.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import edu.uoc.epcsd.user.application.request.LoginRequest;
import edu.uoc.epcsd.user.application.request.RegisterRequest;
import edu.uoc.epcsd.user.application.rest.UserRESTController;
import edu.uoc.epcsd.user.domain.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.security.core.GrantedAuthority;



@WebMvcTest(value = UserRESTController.class)
@ContextConfiguration(classes = {UserRESTController.class, WebConfig.class})
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerUnitTest {
	
	static String USER_TEST_NAME = "EXAMPLE";
	static String USER_TEST_SURNAME = "TEST";
	static String USER_TEST_EMAIL = "email@exmaple.com";
	static String USER_TEST_PASSWORD = "PASSWORD";
	private static final String REST_USERS_PATH = "/users";
	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService service;

	@MockBean
	private TokenService tokenService;

	@MockBean
	private RoleService roleService;

	@MockBean
	private KafkaUserMessagingService kafkaUserMessagingService;

	@BeforeEach
	void setUp() {
		// Crear una lista de GrantedAuthorities
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		// Crear UserDetails
		UserDetails mockUser = org.springframework.security.core.userdetails.User
				.withUsername("jordi@uoc.edu")
				.password("123")
				.authorities(authorities)
				.build();

		// Crear y establecer TestingAuthenticationToken en el contexto de seguridad
		TestingAuthenticationToken testingAuthenticationToken =
				new TestingAuthenticationToken(mockUser, null, authorities);

		SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
	}

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
		example.setSurname(USER_TEST_SURNAME);
		example.setEmail(USER_TEST_EMAIL);
		example.setPassword(USER_TEST_PASSWORD);
		return example;
	};

	// post /register
	@Test
	@DisplayName("RegisterUser")
	public void rest_call_returns_new_userid() throws Exception {
		Set<Role> roles = new HashSet<>(Collections.singletonList(new Role(1L, Rol.BUYER)));

		User user = new User(null, USER_TEST_NAME, USER_TEST_SURNAME, USER_TEST_EMAIL,
				USER_TEST_PASSWORD, roles, false);

		Mockito.when(service.findUserByMail(USER_TEST_EMAIL)).thenReturn(Optional.empty());
		Mockito.when(service.createUser(Mockito.any(User.class))).thenReturn(user);

		RegisterRequest request = new RegisterRequest(USER_TEST_NAME, USER_TEST_SURNAME, USER_TEST_EMAIL, USER_TEST_PASSWORD);

		mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapToJson(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(user.getName())))
				.andExpect(jsonPath("$.email", is(user.getEmail())));

		Mockito.verify(kafkaUserMessagingService, Mockito.times(1)).sendMessage(Mockito.any(User.class));
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

	// get /users/{email}
	@Test
	@DisplayName("FindExistingUserByEmail")
	public void rest_call_returns_user() throws Exception {

		User user = new User();
		user.setId(1L);
		user.setEmail(USER_TEST_EMAIL);
		user.setPassword(USER_TEST_PASSWORD);
		user.setName(USER_TEST_NAME);
		user.setSurname(USER_TEST_SURNAME);

		Mockito.when(service.findUserByMail(USER_TEST_EMAIL)).thenReturn(Optional.of(user));

		mvc.perform(get(REST_USERS_PATH + "/" + user.getEmail())
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(user.getName())))
				.andExpect(jsonPath("$.surname", is(user.getSurname())));
	}

	// get /users/{email}
	@Test
	@DisplayName("FindNotExistingUserByEmail")
	public void rest_call_returns_user_not_exists() throws Exception {
		String nonExistingEmail = "nonexistingemail@example.com";

		// Configurar el servicio para que retorne un Optional vacío cuando se busque este email
		Mockito.when(service.findUserByMail(nonExistingEmail)).thenReturn(Optional.empty());

		mvc.perform(get(REST_USERS_PATH + "/" + nonExistingEmail)
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
