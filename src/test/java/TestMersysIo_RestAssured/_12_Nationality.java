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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _12_Nationality {

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

//    @Test
//    public void NavigateToNationality(){
//
//        List<String> a = new ArrayList<>();
//
//        given()
//                .spec(reqSpec)
//                .body(a)
//
//                .when()
//                .post("/nationality/search")
//
//                .then()
//                .statusCode(200)
//                .log().body()
//                ;
//
//    }

    @Test
    public void AddNationality(){

        String id = null;
        String name = faker.name().firstName();
        List<String> translateName = new ArrayList<>();
        //String[] a =new String[0];

        Map<String, Object> addNatiionality = new HashMap<>();
        addNatiionality.put("id", id);
        addNatiionality.put("name", name);
        addNatiionality.put("translateName", translateName);

       NationalityName =
        given()
                .spec(reqSpec)
                .body(addNatiionality)

                .when()
                .post("school-service/api/nationality")

                .then()
                .statusCode(201)
                .log().body()
                .extract().path("name")
                ;


    }

    @Test
    public void SearchNationality(){

        given()
                .spec(reqSpec)
                .body(NationalityName)

                .when()
                .post("school-service/api/nationality/search")

                .then()
                .log().body()
                .statusCode(200)

        ;

    }

    @Test
    public void EditNationality(){
        given()

                .when()
                .put("school-service/api/nationality")

                .then()
                .statusCode(200)
        ;

    }

    @Test
    public void DeleteNationality(){
        given()

                .when()
                .put("school-service/api/nationality")

                .then()
                .statusCode(200)
        ;

    }







}
