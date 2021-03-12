package br.com.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend/";
	}

	@Test
	public void deveRetornarTarefas() {
		RestAssured.given().when().get("/todo").then().statusCode(200);
	}

	@Test
	public void deveAdicionarTarefaComSucesso() {
		RestAssured.given().body("{ \"task\": \"Teste via api\", \"dueDate\": \"2022-12-21\" }")
				.contentType(ContentType.JSON).when().post("/todo").then().statusCode(201);
	}

	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given().body("{ \"task\": \"Teste via api\", \"dueDate\": \"2010-12-21\" }")
				.contentType(ContentType.JSON).when().post("/todo").then().statusCode(400)
				.body("message", CoreMatchers.is("Due date must not be in past"));
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		Integer id = RestAssured.given()
			.body("{ \"task\": \"Teste para remoção\", \"dueDate\": \"2022-12-21\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.log().all()
			.statusCode(201)
			.extract().path("id")
			;
		//System.out.println(id);
		
		// remover
		RestAssured.given()
		.when()
			.delete("/todo/" + id)
		.then()
			.statusCode(204)
		;
	}
}
