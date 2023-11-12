package TestMersysIo_RestAssured;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class _04_Field {

    RequestSpecification reqSpec;
    ResponseSpecification responseSpec;
    String NationalityName;
    Faker faker = new Faker();

    @BeforeClass
    public void login(){

        baseURI = "https://test.mersys.io/";
        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .body(userCredential)
                        .contentType(ContentType.JSON)

                        .when()
                        .post("/auth/login")

                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        reqSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)  // statusCode(200)
                .log(LogDetail.BODY)  //log().body()
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void AddField(){
        given()
                .when()
                .then()
                ;
    }

    @Test
    public void SearchField(){
        given()
                .when()
                .then()
        ;
    }

    @Test
    public void EditField(){
        given()
                .when()
                .then()
        ;
    }

    @Test
    public void DeleteField(){
        given()
                .when()
                .then()
        ;
    }





}
