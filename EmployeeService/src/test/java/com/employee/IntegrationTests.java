package com.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.employee.controller.EmployeeServiceController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmployeeServiceApplication.class)
@AutoConfigureMockMvc
public class IntegrationTests {
	@Autowired
	private EmployeeServiceController employeeServiceController;
	private String token;
	@Autowired
	private MockMvc mockMvc;

	private JSONArray employeesJsonArray;

	@Before
	public void initialize() throws Exception {

		String loginInfo = "{\n" + "    \"username\": \"admin\",\n" + "    \"password\": \"admin\"\n" + "}";

		MvcResult loginResult = 
				mockMvc.perform(post("/api/v1/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(loginInfo)
						.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andReturn();
		token = new JSONObject(loginResult.getResponse().getContentAsString()).getString("token");

		MvcResult employeesResult = mockMvc.perform(get("/api/v1/employee").header("token", token)).andDo(print())
				.andExpect(status().isOk()).andReturn();

		employeesJsonArray = new JSONArray(employeesResult.getResponse().getContentAsString());
		assertNotEquals(0, employeesJsonArray.length());
	}

	@Test
	public void contexLoads() throws Exception {
		assertThat(employeeServiceController).isNotNull();
	}

	@Test
	public void helloMessageTest() throws Exception {
		MvcResult result = 
				mockMvc.perform(get("/api/v1/hello"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		assertEquals("Hello!", result.getResponse().getContentAsString());
	}

	// ---- LOGIN
	@Test
	public void loginWithCorrectCredentials() throws Exception {
		String loginInfo = "{\n" + "    \"username\": \"admin\",\n" + "    \"password\": \"admin\"\n" + "}";

		MvcResult result = 
				mockMvc.perform(post("/api/v1/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(loginInfo)
						.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andReturn();
		token = new JSONObject(result.getResponse().getContentAsString()).getString("token");

		assertNotNull(token);
		assertNotEquals(0, token.length());
	}

	@Test
	public void loginWithIncorrectCredentials() throws Exception {
		String loginInfo = "{\n" + "    \"username\": \"xyz\",\n" + "    \"password\": \"xyz\"\n" + "}";

		mockMvc.perform(post("/api/v1/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginInfo)
				.characterEncoding("utf-8"))
		.andExpect(status().is4xxClientError())
		.andReturn();
	}

	// ---- GET Employees
	@Test
	public void getEmployeeWithInvalidToken() throws Exception {
		mockMvc.perform(get("/api/v1/employee")
				.header("token", "nothing"))
		.andDo(print())
		.andExpect(status().is4xxClientError())
		.andReturn();
	}

	@Test
	public void getEmployeeAllEmployees() throws Exception {
		MvcResult result = 
				mockMvc.perform(get("/api/v1/employee")
						.header("token", token))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		JSONArray jsonResult = new JSONArray(result.getResponse().getContentAsString());
		assertNotEquals(0, jsonResult.length());
	}

	@Test
	public void getEmployeeOneEmployee() throws Exception {
		String id = employeesJsonArray.getJSONObject(0).getString("id");

		MvcResult result = 
				mockMvc.perform(get("/api/v1/employee/" + id)
						.header("token", token))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		assertNotEquals(0, jsonResult.length());
		assertEquals(id, jsonResult.getString("id"));
		assertTrue(jsonResult.has("name"));
		assertTrue(jsonResult.has("email"));
		assertTrue(jsonResult.has("birthday"));
	}

	@Test
	public void getEmployeeInvalidEmployee() throws Exception {
		String id = "NONE";

		MvcResult result = 
				mockMvc.perform(get("/api/v1/employee/" + id)
						.header("token", token))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn();

		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		assertNotEquals(0, jsonResult.length());
		assertTrue(jsonResult.has("message"));
	}
	
	//---- CREATE employees
	@Test
	public void createEmployee() throws Exception {
		JSONObject jsonEmployee = new JSONObject();
		jsonEmployee.put("name", "John Smith");
		jsonEmployee.put("email", "john@smith.com");
		jsonEmployee.put("birthday", "1990-01-01");
		
		MvcResult result = 
				mockMvc.perform(post("/api/v1/employee")
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", token)
						.content(jsonEmployee.toString())
						.characterEncoding("utf-8"))
				.andExpect(status().isCreated())
				.andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(jsonResult);
		assertNotEquals(0, jsonResult.length());
		assertTrue(jsonResult.has("id"));
		assertTrue(jsonResult.has("name"));
		assertTrue(jsonResult.has("email"));
		assertTrue(jsonResult.has("birthday"));
	}
	
	@Test
	public void createEmployeeWithNonUniqueEmail() throws Exception {
		//emails must be unique
		String email = employeesJsonArray.getJSONObject(0).getString("email");
		
		JSONObject jsonEmployee = new JSONObject();
		jsonEmployee.put("name", "Some Employee");
		jsonEmployee.put("email", email);
		jsonEmployee.put("birthday", "1980-01-01");
		
		MvcResult result = 
				mockMvc.perform(post("/api/v1/employee")
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", token)
						.content(jsonEmployee.toString())
						.characterEncoding("utf-8"))
				.andExpect(status().isBadRequest())
				.andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(jsonResult);
		assertNotEquals(0, jsonResult.length());
		assertTrue(jsonResult.has("message"));
	}
	
	@Test
	public void createEmployeeWithInvalidBirthday() throws Exception {
		//birthday must be in yyyy-mm-dd format
		
		JSONObject jsonEmployee = new JSONObject();
		jsonEmployee.put("name", "Some Employee");
		jsonEmployee.put("email", "some@some.com");
		jsonEmployee.put("birthday", "19800101");
		
		MvcResult result = 
				mockMvc.perform(post("/api/v1/employee")
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", token)
						.content(jsonEmployee.toString())
						.characterEncoding("utf-8"))
				.andExpect(status().isBadRequest())
				.andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(jsonResult);
		assertNotEquals(0, jsonResult.length());
		assertTrue(jsonResult.has("message"));
	}
	
	//----- UPDATE employee
	@Test
	public void updateEmployee() throws Exception {
		//emails must be unique
		JSONObject jsonEmployeeBeforeUpdate = employeesJsonArray.getJSONObject(0);
		
		jsonEmployeeBeforeUpdate.put("name", "New Name");
		jsonEmployeeBeforeUpdate.put("email", "new@example.com");
		jsonEmployeeBeforeUpdate.put("birthday", "1970-01-01");
		
		MvcResult result = 
				mockMvc.perform(put("/api/v1/employee")
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", token)
						.content(jsonEmployeeBeforeUpdate.toString())
						.characterEncoding("utf-8"))
				.andExpect(status().isOk())
				.andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(jsonResult);
		assertNotEquals(0, jsonResult.length());
		assertEquals(jsonEmployeeBeforeUpdate.get("name"), jsonResult.get("name"));
		assertEquals(jsonEmployeeBeforeUpdate.get("email"), jsonResult.get("email"));
		assertEquals(jsonEmployeeBeforeUpdate.get("birthday"), jsonResult.get("birthday"));
	}
	
	@Test
	public void updateEmployeeWithInvalidData() throws Exception {
		JSONObject jsonEmployeeBeforeUpdate = employeesJsonArray.getJSONObject(0);
		
		jsonEmployeeBeforeUpdate.put("name", "New Name");
		jsonEmployeeBeforeUpdate.put("email", "INVALID EMAIL");
		jsonEmployeeBeforeUpdate.put("birthday", "INVALID BIRTHDAY");
		
		MvcResult result = 
				mockMvc.perform(put("/api/v1/employee")
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", token)
						.content(jsonEmployeeBeforeUpdate.toString())
						.characterEncoding("utf-8"))
				.andExpect(status().isBadRequest())
				.andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(jsonResult);
		assertNotEquals(0, jsonResult.length());
		assertTrue(jsonResult.has("message"));
	}
	
	@Test
	public void updateEmployeeWithInvalidDepartment() throws Exception {
		//department must exist
		String invalidDepartment = "{\"id\":99,\"name\":\"NONE\"}";
				
		JSONObject jsonEmployeeBeforeUpdate = employeesJsonArray.getJSONObject(0);
		jsonEmployeeBeforeUpdate.put("department", invalidDepartment);
		
		MvcResult result = 
				mockMvc.perform(put("/api/v1/employee")
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", token)
						.content(jsonEmployeeBeforeUpdate.toString())
						.characterEncoding("utf-8"))
				.andExpect(status().isBadRequest())
				.andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(jsonResult);
		assertNotEquals(0, jsonResult.length());
		assertTrue(jsonResult.has("message"));
	}
	
	@Test
	public void deleteEmployee() throws Exception {
		MvcResult employeesResultBeforeDelete = 
				mockMvc.perform(get("/api/v1/employee")
						.header("token", token))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		JSONArray employeeArrayBeforeDelete = new JSONArray(employeesResultBeforeDelete.getResponse().getContentAsString());
		
		String idToDelete = employeeArrayBeforeDelete.getJSONObject(0).getString("id");
		
		mockMvc.perform(delete("/api/v1/employee/" + idToDelete)
				.contentType(MediaType.APPLICATION_JSON)
				.header("token", token)
				.characterEncoding("utf-8"))
		.andExpect(status().isOk())
		.andReturn();
		
		MvcResult employeesResultAfterDelete = 
				mockMvc.perform(get("/api/v1/employee")
						.header("token", token))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		JSONArray employeeArrayAfterDelete = new JSONArray(employeesResultAfterDelete.getResponse().getContentAsString());
		
		assertNotNull(employeeArrayAfterDelete);
		assertNotEquals(0, employeeArrayAfterDelete.length());
		assertEquals(employeeArrayBeforeDelete.length()-1, employeeArrayAfterDelete.length());
	}
	
	//--- Create Department
	@Test
	public void createDepartment() throws Exception {
		JSONObject jsonDepartment = new JSONObject();
		jsonDepartment.put("name", "Sample Department");
		
		MvcResult result = 
				mockMvc.perform(post("/api/v1/department")
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", token)
						.content(jsonDepartment.toString())
						.characterEncoding("utf-8"))
				.andExpect(status().isCreated())
				.andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(jsonResult);
		assertNotEquals(0, jsonResult.length());
		assertTrue(jsonResult.has("id"));
		assertTrue(jsonResult.has("name"));
	}
	
	//Event Tests
	/*@Test
	public void createEvent() throws Exception {
		JSONObject jsonDepartment = new JSONObject();
		jsonDepartment.put("name", "Sample Department");
		
		MvcResult result = 
				mockMvc.perform(post("/api/v1/department")
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", token)
						.content(jsonDepartment.toString())
						.characterEncoding("utf-8"))
				.andExpect(status().isCreated())
				.andReturn();
		JSONObject jsonResult = new JSONObject(result.getResponse().getContentAsString());
		
		assertNotNull(jsonResult);
		assertNotEquals(0, jsonResult.length());
		assertTrue(jsonResult.has("id"));
		assertTrue(jsonResult.has("name"));
	}*/
}
