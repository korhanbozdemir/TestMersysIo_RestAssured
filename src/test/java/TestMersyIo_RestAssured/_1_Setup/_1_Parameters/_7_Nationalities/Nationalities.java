package TestMersyIo_RestAssured._1_Setup._1_Parameters._7_Nationalities;

import TestMersyIo_RestAssured._0_Variables.Variables;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Nationalities extends Variables {

    Map<String, String> Nationalities;

    @BeforeClass
    public void Setup(){

        baseURI = "https://test.mersys.io/";
        Map<String, String> userCredential=new HashMap<>();
        userCredential.put("username",username);
        userCredential.put("password",password);
        userCredential.put("rememberMe","true");

        Cookies cookies=
                given()
                        .body(userCredential)
                        .contentType(ContentType.JSON)
                        .when()
                        .post(urlLog)
                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();
        ;

        reqSpec = new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();

        System.out.println("Login Test: Successfully passed !");

    }




    @Test(priority = 1)
    public void createNationalities() {

        Nationalities = new HashMap<>();

        NationalitiesName = "serdar" + faker.number().digits(3);
        Nationalities.put("name", NationalitiesName);

        NationalitiesID =

                given()
                        .spec(reqSpec)
                        .body(Nationalities)
                        //.log().body()
                        .when()
                        .post(url+"nationality")
                        .then()
                        //.log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("Create Nationalities Test: Successfully passed !");
    }

    @Test(priority = 2, dependsOnMethods = "createNationalities")
    public void createNationalitiesNegative() {

        Nationalities.put("name", NationalitiesName);

        given()

                .spec(reqSpec)
                .body(Nationalities)
                //.log().body()
                .when()
                .post(url+"nationality")
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", containsString("already"));

        System.out.println("Create Nationalities Negative Test: Successfully passed !");
    }

    @Test(priority = 3, dependsOnMethods = "createNationalitiesNegative")
    public void updateNationalities() {

        NationalitiesName = "Test" + faker.number().digits(5);

        Nationalities.put("id", NationalitiesID);
        Nationalities.put("name", NationalitiesName);

        given()

                .spec(reqSpec)
                .body(Nationalities)
                // .log().body()
                .when()
                .put(url+"nationality")
                .then()
                //.log().body() // show incoming body as log
                .statusCode(200)
                .body("name", equalTo(NationalitiesName));

        System.out.println("Update Nationalities Test: Successfully passed !");
    }

    @Test(priority = 4, dependsOnMethods = "updateNationalities")
    public void deleteNationalities(){

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"nationality/" + NationalitiesID)
                .then()
                //.log().body()
                .statusCode(200);

        System.out.println("Delete Nationalities Test: Successfully passed !");

    }

    @Test (priority = 5, dependsOnMethods = "deleteNationalities")
    public void deleteNationalitiesNegative(){

        given()

                .spec(reqSpec)
                .when()
                .delete(url+"nationality/" + NationalitiesID)
                .then()
                //.log().body()
                .statusCode(400)
                .body("message", equalTo("Nationality not  found"));

        System.out.println("Delete Nationalities Negative Test: Successfully passed !");
    }
}

